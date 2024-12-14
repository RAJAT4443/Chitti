package com.Chitti.AiVoiceMail.service.summarization.Impl;

import com.Chitti.AiVoiceMail.models.ChatHistories;
import com.Chitti.AiVoiceMail.service.db.mongo.ChatHistoriesService;
import com.Chitti.AiVoiceMail.service.summarization.SummarizationService;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class SummarizationServiceImpl implements SummarizationService {

//    input - sessionid
//    chat history from mongodb
//
//    based on this history summarize and add actionable input as a respone

//    @Value("${openai.key}")
//    private static String YOUR_OPENAI_API_KEY;



   private static final String OPENAI_API_KEY ="test";
    private final ChatHistoriesService chatHistoriesService;


    public SummarizationServiceImpl(ChatHistoriesService chatHistoriesService) {
        this.chatHistoriesService = chatHistoriesService;
    }


    public String summarizeSession(String sessionId) {
        try {
            // Step 1: Fetch chat history for the given session ID
            ChatHistories chatHistories = chatHistoriesService.getChatHistoryBySessionId(sessionId);

            if (chatHistories == null || chatHistories.getMessages().isEmpty()) {
                throw new IllegalArgumentException("No chat history found for sessionId: " + sessionId);
            }

            StringBuilder str= new StringBuilder();
            List<ChatHistories.Message> strList=chatHistories.getMessages();
            for(ChatHistories.Message message:strList) {
                str.append(message);
            }
            // Step 2: Generate summary and actionable insights
            return generateSummaryAndActionableInsights(str.toString());

        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }


    String userMessage = """
    Please summarize the following chat session and provide actionable insights:

    Chat History:
    Hi, I’d like to discuss two things. First, I need to reschedule our call to Wednesday instead of Thursday. Second, I wanted to let you know that the client approved our budget proposal.
    Sure, I’ll make a note of that. Is there anything else?
    No, that’s it for now. Thanks!

    Your response must be in the following JSON format:
    {
      "category": "<One of: Scheduling, Personal Update, Business Inquiry, Other>",
      "summary": "<Summarized actionable insights>"
    }
""";




    String systemMessage = """
    You are a chat summarizer. Your role is to generate a concise and organized summary with actionable insights based on the provided chat history.
    Respond in JSON format:
    {
      "category": "<One of: Scheduling, Personal Update, Business Inquiry, Other>",
      "summary": "<Crisp,clear  and actionable summary of the chat>"
    }
    Guidelines:
    - Focus on the key points of the chat and avoid unnecessary details.
    - Categorize the chat accurately under the provided categories.
    - Include essential details like dates, times, or specific actions, especially for scheduling-related messages.
    - Summarize incomplete or vague inputs based on available information, and highlight any missing details.
    """;




    public String generateSummaryAndActionableInsights(String chatHistory) throws IOException {
        String endpoint = "https://api.openai.com/v1/chat/completions";
        OkHttpClient client = new OkHttpClient();

        // Build JSON payload with the refined system and user messages
        JSONObject payload = new JSONObject();
        payload.put("model", "gpt-4-turbo");
        payload.put("messages", new JSONArray()
                .put(new JSONObject().put("role", "system").put("content", systemMessage))
                .put(new JSONObject().put("role", "user").put("content", userMessage))
        );
        payload.put("max_tokens", 150);
        payload.put("temperature", 0.5);

        // Make the API request
        Request request = new Request.Builder()
                .url(endpoint)
//                .addHeader("Authorization", "Bearer"+ OPENAI_API_KEY)
                .addHeader("Authorization", "Bearer " + OPENAI_API_KEY)
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


}
