package com.Chitti.AiVoiceMail;

import com.Chitti.AiVoiceMail.pojo.ChatHistory;

public class SummarizationServiceTest {

    public static void main(String[] args) {
        ChatHistory chatHistory = ChatHistoryServiceTest.getDummyChatHistory();

        String concatenatedMessages = chatHistory.getMessages().stream()
                .map(ChatHistory.Message::getContent)
                .reduce("", (partial, msg) -> partial + "\n" + msg);

        System.out.println("Concatenated Messages for Summarization:");
        System.out.println(concatenatedMessages);

        // Simulate passing to GPT
        String prompt = "Summarize the following chat session and provide actionable insights:\n" + concatenatedMessages;
        System.out.println("Generated Prompt:");
        System.out.println(prompt);

//        String resp= SummarizationService.generateSummaryAndActionableInsights(concatenatedMessages);

//        OpenAiSummarizationServiceImpl summarizationService = new OpenAiSummarizationServiceImpl(null, applicationConfigsService, userCustomizationsService); // Pass null for ChatHistoriesService as it's not needed for this test
        try {
//            String summary = summarizationService.generateSummaryAndActionableInsights(concatenatedMessages);
            System.out.println("\nGenerated Summary and Actionable Insights:");
//            System.out.println(summary);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("\nError while generating summary.");
        }


    }


}
