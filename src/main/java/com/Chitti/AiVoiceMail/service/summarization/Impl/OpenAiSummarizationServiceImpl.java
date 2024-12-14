package com.Chitti.AiVoiceMail.service.summarization.Impl;

import com.Chitti.AiVoiceMail.dtos.Configurations;
import com.Chitti.AiVoiceMail.entities.UserDetails;
import com.Chitti.AiVoiceMail.models.ChatHistories;
import com.Chitti.AiVoiceMail.models.UserCustomizations;
import com.Chitti.AiVoiceMail.service.db.mongo.ChatHistoriesService;
import com.Chitti.AiVoiceMail.service.db.mongo.UserCustomizationsService;
import com.Chitti.AiVoiceMail.service.db.mysql.ApplicationConfigsService;
import com.Chitti.AiVoiceMail.service.summarization.SummarizationService;
import com.Chitti.AiVoiceMail.utilities.Utilities;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class OpenAiSummarizationServiceImpl implements SummarizationService {

//    input - sessionid
//    chat history from mongodb
//
//    based on this history summarize and add actionable input as a respone

//    @Value("${openai.key}")
//    private static String YOUR_OPENAI_API_KEY;

    private static final String TYPE = "openAi";

    private static final String OPENAI_API_KEY = "test_key";
    private final ChatHistoriesService chatHistoriesService;
    private final ApplicationConfigsService applicationConfigsService;
    private final UserCustomizationsService userCustomizationsService;

    public OpenAiSummarizationServiceImpl(ChatHistoriesService chatHistoriesService, ApplicationConfigsService applicationConfigsService, UserCustomizationsService userCustomizationsService) {
        this.chatHistoriesService = chatHistoriesService;
        this.applicationConfigsService = applicationConfigsService;
        this.userCustomizationsService = userCustomizationsService;
    }


    public String summarizeSession(String sessionId) {
        try {
            // Step 1: Fetch chat history for the given session ID
            ChatHistories chatHistories = chatHistoriesService.getChatHistoryBySessionId(sessionId);

            if (chatHistories == null || chatHistories.getMessages().isEmpty()) {
                throw new IllegalArgumentException("No chat history found for sessionId: " + sessionId);
            }
            UserDetails userDetails = new UserDetails();
            // Step 2: Generate summary and actionable insights
            return generateSummaryAndActionableInsights(chatHistories, userDetails);

        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }


    String userMessage = """
                Please summarize the following chat session and provide actionable insights:
                {
                    "chat" : {{messages}}
                }
                Your response must be in the following JSON format:
                {
                  "category": "<One of: Scheduling, Personal Update, Business Inquiry, Other>",
                  "summary": "<Summarized actionable insights>"
                }
            """;


//    String systemMessage = """
//            You are a chat summarizer. Your role is to generate a concise and organized summary with actionable insights based on the provided chat history.
//            Respond in JSON format:
//            {
//              "category": "<One of: Scheduling, Personal Update, Business Inquiry, Other>",
//              "summary": "<Crisp,clear  and actionable summary of the chat>"
//            }
//            Guidelines:
//            - Focus on the key points of the chat and avoid unnecessary details.
//            - Categorize the chat accurately under the provided categories.
//            - Include essential details like dates, times, or specific actions, especially for scheduling-related messages.
//            - Summarize incomplete or vague inputs based on available information, and highlight any missing details.
//            """;

    String systemMessage = """
            {
              "summarization_context": {
                "categories": {{categories}},
                "greeting": "Hello {{user_id}}! Here's your chat summary.",
                "voice_tone": "{{preferred_tone}}",
                "max_message_length": {{max_message_length}},
              },
              "prompt_instructions": {
                "scope": "You are a chat summarizer tasked with providing concise and organized summaries with actionable insights based on the provided chat history.",
                "categorization": "Classify the chat into one of the categories provided in the assistant configuration.",
                "long_message_handling": "Summarize lengthy messages exceeding the maximum message length specified in the assistant configuration.",
                "repeated_instructions": "Combine repeated or overlapping points and present them as a single actionable insight.",
                "abrupt_ending_handling": "If the chat is incomplete or vague, summarize based on the available information and note any missing details.",
                "interruptions_handling": "If there are pauses or interruptions, summarize the main content available.",
                "vague_input_handling": "Highlight unclear points and suggest seeking clarification.",
                "response_format": "Always respond in JSON format with `category` and `summary` fields."
              },
            }
            """;


    public String generateSummaryAndActionableInsights(ChatHistories chatHistories, UserDetails userDetails) throws IOException {

        OkHttpClient client = new OkHttpClient();
        Configurations config = fetchConfigurations();
        Map<String, String> placeholderValues = preparePlaceholderValues(chatHistories, userDetails);

        String finalSystemPrompt = Utilities.replacePlaceholders(systemMessage, placeholderValues);
        String finalUserPrompt = Utilities.replacePlaceholders(userMessage, placeholderValues);

        System.out.println("Final System Prompt: " + finalSystemPrompt);
        System.out.println("Final User Prompt: " + finalUserPrompt);

        // Build JSON payload with the refined system and user messages
        JSONObject payload = new JSONObject();
        payload.put("model", config.getModel());
        payload.put("messages", new JSONArray()
                .put(new JSONObject().put("role", "system").put("content", finalSystemPrompt))
                .put(new JSONObject().put("role", "user").put("content", finalUserPrompt))
        );
        payload.put("max_tokens", config.getMaxTokens());
        payload.put("temperature", 0.5);

        // Make the API request
        Request request = new Request.Builder()
                .url(config.getEndpoint())
//                .addHeader("Authorization", "Bearer"+ OPENAI_API_KEY)
                .addHeader("Authorization", "Bearer " + config.getSecretKey())
                .post(RequestBody.Companion.create(payload.toString(), MediaType.parse("application/json")))
                .build();

        // Parse the response
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Failed to generate summary: " + response);
            }

            JSONObject responseBody = new JSONObject(response.body().string());
            String assistantContent = responseBody
                    .getJSONArray("choices")
                    .getJSONObject(0)
                    .getJSONObject("message")
                    .getString("content")
                    .trim();

            // Extract the category and summary from the JSON response
            JSONObject assistantResponse = new JSONObject(assistantContent);
            String category = assistantResponse.optString("category", "Unknown");
            String summary = assistantResponse.optString("summary", "No summary provided.");

            return "Category: " + category + "\nSummary: " + summary;
        }
    }

    private Map<String, String> preparePlaceholderValues(ChatHistories chatHistories, UserDetails userDetails) {
        Map<String, String> placeholderValues = new HashMap<>();
        placeholderValues.put("max_message_length", String.valueOf(applicationConfigsService.getConfigValue("openai.prompt.maxTokens")));
        placeholderValues.putAll(Utilities.extractFields(chatHistories));
        placeholderValues.putAll(Utilities.extractFields(userDetails));

        UserCustomizations userCustomizations = userCustomizationsService.getUserCustomization(userDetails.getUserId());
        placeholderValues.putAll(Utilities.extractFields(userCustomizations));
        return placeholderValues;
    }

    private Configurations fetchConfigurations() {
        return new Configurations(
                applicationConfigsService.getConfigValue("openai.api.endpoint"),
                applicationConfigsService.getConfigValue("openai.assistant.prompt"),
                applicationConfigsService.getConfigValue("openai.prompt.model"),
                applicationConfigsService.getConfigValue("openai.api.key"),
                Integer.parseInt(applicationConfigsService.getConfigValue("openai.prompt.default.maxTokens")),
                Double.parseDouble(applicationConfigsService.getConfigValue("openai.prompt.default.temperature")),
                applicationConfigsService.getConfigValue("openai.assistant.input.prompt")
        );
    }

    @Override
    public String getType() {
        return TYPE.toLowerCase();
    }


}
