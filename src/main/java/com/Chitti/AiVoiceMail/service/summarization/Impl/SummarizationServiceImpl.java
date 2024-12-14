package com.Chitti.AiVoiceMail.service.summarization.Impl;

import com.Chitti.AiVoiceMail.models.ChatHistories;
import com.Chitti.AiVoiceMail.service.db.mongo.ChatHistoriesService;
import com.Chitti.AiVoiceMail.service.summarization.SummarizationService;
import okhttp3.*;
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


    private static final String OPENAI_API_KEY = "YOUR_OPENAI_API_KEY";

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


    public String generateSummaryAndActionableInsights(String chatHistory) throws IOException {
        String prompt = "Summarize the following chat session and provide actionable insights:\n\n" + chatHistory;

        OkHttpClient client = new OkHttpClient();

        // Create JSON payload for OpenAI API
        JSONObject payload = new JSONObject();
        payload.put("model", "text-davinci-003");
        payload.put("prompt", prompt);
        payload.put("max_tokens", 300);
        payload.put("temperature", 0.7);

        // Build HTTP request
        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/completions")
                .addHeader("Authorization", "Bearer " + OPENAI_API_KEY)
                .post(RequestBody.Companion.create(payload.toString(), MediaType.parse("application/json")))
                .build();

        // Execute request and parse response
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Failed to generate summary: " + response);
            }

            JSONObject responseBody = new JSONObject(response.body().string());
            return responseBody.getJSONArray("choices").getJSONObject(0).getString("text").trim();
        }
    }


}
