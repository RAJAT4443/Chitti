package com.Chitti.AiVoiceMail.service.voiceMailAssistant.impl;

import com.Chitti.AiVoiceMail.dtos.OpenAiMessageDto;
import com.Chitti.AiVoiceMail.dtos.OpenAiRequestDto;
import com.Chitti.AiVoiceMail.dtos.OpenAiResponseDto;
import com.Chitti.AiVoiceMail.entities.UserDetails;
import com.Chitti.AiVoiceMail.models.ChatHistories;
import com.Chitti.AiVoiceMail.service.db.mongo.ChatHistoriesService;
import com.Chitti.AiVoiceMail.service.db.mysql.ApplicationConfigsService;
import com.Chitti.AiVoiceMail.service.voiceMailAssistant.AssistantResponseService;
import com.Chitti.AiVoiceMail.utilities.Utilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Primary
public class OpenAiAssistantResponseService implements AssistantResponseService {
    @Autowired
    private ApplicationConfigsService applicationConfigService;

    @Autowired
    private ChatHistoriesService chatHistoriesService;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * Generates a response for the given input text by dynamically building the prompt
     * and interacting with the OpenAI API.
     *
     * @param inputText     The input message from the user.
     * @param chatHistories The chat history object for maintaining the session context.
     * @param userDetails   The user details object containing personalization data.
     * @return The processed response text.
     * @throws Exception If there is an issue during prompt generation or API call.
     */
    @Override
    public String generateResponse(String inputText, ChatHistories chatHistories, UserDetails userDetails) throws Exception {
        // 1. Fetch configurations dynamically
        String openAiEndpoint = applicationConfigService.getConfigValue("openai.api.endpoint");
        String promptTemplate = applicationConfigService.getConfigValue("openai.assistant.prompt");
        String promptModel = applicationConfigService.getConfigValue("openai.prompt.model");
        String secretKey = applicationConfigService.getConfigValue("openai.api.key");
        int promptMaxTokens = Integer.parseInt(applicationConfigService.getConfigValue("openai.prompt.maxTokens"));
        double promptTemperature = Double.parseDouble(applicationConfigService.getConfigValue("openai.prompt.temperature"));
        String inputTextPromptTemplate = applicationConfigService.getConfigValue("openai.input.text.prompt");


        // 2. Extract fields from objects and merge them into a single map
        Map<String, String> placeholderValues = new HashMap<>();
        placeholderValues.putAll(Utilities.extractFields(chatHistories));
        placeholderValues.putAll(Utilities.extractFields(userDetails));

        // 3. Replace placeholders in the template
        String finalPrompt = Utilities.replacePlaceholders(promptTemplate, placeholderValues);
        String finalInputTextPrompt = Utilities.replacePlaceholders(inputTextPromptTemplate, placeholderValues);


        // 4. Build the API request

        List<OpenAiMessageDto> openAiMessageDtos = new ArrayList<>(chatHistories.getMessages().stream()
                .filter(message -> message.getRole().equals("user") || message.getRole().equals("assistant"))
                .map(message -> new OpenAiMessageDto(message.getRole(), message.getContent()))
                .toList());

        openAiMessageDtos.add(new OpenAiMessageDto("system", finalPrompt));
        openAiMessageDtos.add(new OpenAiMessageDto("user", finalInputTextPrompt));

        OpenAiRequestDto requestDto = new OpenAiRequestDto(
                promptModel,
                openAiMessageDtos,
                promptMaxTokens,
                promptTemperature
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(secretKey);

        HttpEntity<OpenAiRequestDto> entity = new HttpEntity<>(requestDto, headers);

        // 5. Make the API call and handle the response
        OpenAiResponseDto responseDto;
        try {
            responseDto = restTemplate.postForObject(
                    openAiEndpoint,
                    entity,
                    OpenAiResponseDto.class
            );
        } catch (Exception e) {
            throw new RuntimeException("Error during OpenAI API call: " + e.getMessage(), e);
        }

        // 6. Write the latest chat to the chat history asynchronously
        asyncUpdateChatHistory(chatHistories, inputText, responseDto.getFirstChoiceText());

        // 7. Clean up the response text for text-to-speech
        String processedResponse = responseDto.getFirstChoiceText().trim().replaceAll("\\s+", " ");

        // 8. Return the cleaned-up response
        return processedResponse;

    }

    /**
     * Asynchronously updates the chat history with the user input and assistant response.
     *
     * @param chatHistories The chat history object.
     * @param inputText The user input.
     * @param assistantResponse The assistant's response.
     */
    @Async
    public void asyncUpdateChatHistory(ChatHistories chatHistories, String inputText, String assistantResponse) {
        chatHistories.addMessage(new ChatHistories.Message("user", inputText));
        chatHistories.addMessage(new ChatHistories.Message("assistant", assistantResponse));
        chatHistoriesService.addChatHistory(chatHistories);
    }

}
