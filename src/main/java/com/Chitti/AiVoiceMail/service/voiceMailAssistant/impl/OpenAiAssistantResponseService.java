package com.Chitti.AiVoiceMail.service.voiceMailAssistant.impl;

import com.Chitti.AiVoiceMail.dtos.Configurations;
import com.Chitti.AiVoiceMail.dtos.OpenAiMessageDto;
import com.Chitti.AiVoiceMail.dtos.OpenAiRequestDto;
import com.Chitti.AiVoiceMail.dtos.OpenAiResponseDto;
import com.Chitti.AiVoiceMail.entities.UserDetails;
import com.Chitti.AiVoiceMail.models.ChatHistories;
import com.Chitti.AiVoiceMail.models.Message;
import com.Chitti.AiVoiceMail.models.UserCustomizations;
import com.Chitti.AiVoiceMail.service.db.mongo.ChatHistoriesService;
import com.Chitti.AiVoiceMail.service.db.mongo.UserCustomizationsService;
import com.Chitti.AiVoiceMail.service.db.mysql.ApplicationConfigsService;
import com.Chitti.AiVoiceMail.service.voiceMailAssistant.AssistantResponseService;
import com.Chitti.AiVoiceMail.utilities.Utilities;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Primary
public class OpenAiAssistantResponseService implements AssistantResponseService {

    private final ApplicationConfigsService applicationConfigService;

    private final ChatHistoriesService chatHistoriesService;

    private final RestTemplate restTemplate;

    private final UserCustomizationsService userCustomizationsService;

    public OpenAiAssistantResponseService(ApplicationConfigsService applicationConfigService, ChatHistoriesService chatHistoriesService, RestTemplate restTemplate, UserCustomizationsService userCustomizationsService) {
        this.applicationConfigService = applicationConfigService;
        this.chatHistoriesService = chatHistoriesService;
        this.restTemplate = restTemplate;
        this.userCustomizationsService = userCustomizationsService;
    }

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
        // Fetch required configurations
        Configurations config = fetchConfigurations();

        // Fetch user customizations and merge placeholders
        Map<String, String> placeholderValues = preparePlaceholderValues(inputText, chatHistories, userDetails);

        // Build and replace placeholders in the prompt
        String finalPrompt = Utilities.replacePlaceholders(config.getPromptTemplate(), placeholderValues);
        String finalInputTextPrompt = Utilities.replacePlaceholders(config.getInputTextPromptTemplate(), placeholderValues);

        // Build the OpenAI request
        OpenAiRequestDto requestDto = buildOpenAiRequest(chatHistories, finalPrompt, finalInputTextPrompt, config);

        System.out.println("Request DTO: " + requestDto);

        // Call OpenAI API and handle the response
        OpenAiResponseDto responseDto = callOpenAiApi(config.getEndpoint(), config.getSecretKey(), requestDto);

        System.out.println("Response DTO: " + responseDto);

        // Update chat history asynchronously
        asyncUpdateChatHistory(chatHistories, inputText, responseDto.getFirstChoiceText());

        // Process and return the assistant's response
        return cleanResponseText(responseDto.getFirstChoiceText());
    }

    /**
     * Fetches configurations from the applicationConfigService.
     */
    private Configurations fetchConfigurations() {
        return new Configurations(
                applicationConfigService.getConfigValue("openai.api.endpoint"),
                applicationConfigService.getConfigValue("openai.assistant.prompt"),
                applicationConfigService.getConfigValue("openai.prompt.model"),
                applicationConfigService.getConfigValue("openai.api.key"),
                Integer.parseInt(applicationConfigService.getConfigValue("openai.prompt.default.maxTokens")),
                Double.parseDouble(applicationConfigService.getConfigValue("openai.prompt.default.temperature")),
                applicationConfigService.getConfigValue("openai.assistant.input.prompt")
        );
    }

    /**
     * Prepares placeholder values by extracting fields from input objects.
     */
    private Map<String, String> preparePlaceholderValues(String inputText, ChatHistories chatHistories, UserDetails userDetails) {
        Map<String, String> placeholderValues = new HashMap<>();
        placeholderValues.put("max_message_length", String.valueOf(applicationConfigService.getConfigValue("openai.prompt.maxTokens")));
        placeholderValues.putAll(Utilities.extractFields(chatHistories));
        placeholderValues.putAll(Utilities.extractFields(userDetails));

        UserCustomizations userCustomizations = userCustomizationsService.getUserCustomization(userDetails.getUserId());
        placeholderValues.putAll(Utilities.extractFields(userCustomizations));

        placeholderValues.put("inputText", inputText);
        placeholderValues.put("isFirstInteraction", String.valueOf(chatHistories.getSessionId().isEmpty()));
        return placeholderValues;
    }

    /**
     * Builds the OpenAI request DTO.
     */
    private OpenAiRequestDto buildOpenAiRequest(ChatHistories chatHistories, String systemPrompt, String userPrompt, Configurations config) {
        List<OpenAiMessageDto> openAiMessageDtos = chatHistories.getMessages().stream()
                .filter(message -> "user".equals(message.getRole()) || "assistant".equals(message.getRole()))
                .map(message -> new OpenAiMessageDto(message.getRole(), message.getContent()))
                .collect(Collectors.toList());

        openAiMessageDtos.add(new OpenAiMessageDto("system", systemPrompt));
        openAiMessageDtos.add(new OpenAiMessageDto("user", userPrompt));

        return new OpenAiRequestDto(config.getModel(), openAiMessageDtos, config.getMaxTokens(), config.getTemperature());
    }

    /**
     * Makes the API call to OpenAI and handles the response.
     */
    private OpenAiResponseDto callOpenAiApi(String endpoint, String secretKey, OpenAiRequestDto requestDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(secretKey);

        HttpEntity<OpenAiRequestDto> entity = new HttpEntity<>(requestDto, headers);
        try {
            return restTemplate.postForObject(endpoint, entity, OpenAiResponseDto.class);
        } catch (Exception e) {
            throw new RuntimeException("Error during OpenAI API call: " + e.getMessage(), e);
        }
    }

    /**
     * Cleans the response text for further processing.
     */
    private String cleanResponseText(String responseText) {
        return responseText.trim().replaceAll("\\s+", " ");
    }

    /**
     * Asynchronously updates the chat history with the user input and assistant response.
     */
    @Async
    public void asyncUpdateChatHistory(ChatHistories chatHistories, String inputText, String assistantResponse) {
        chatHistories.addMessage(new Message("user", inputText));
        chatHistories.addMessage(new Message("assistant", assistantResponse));
        chatHistoriesService.addChatHistory(chatHistories);
    }

}
