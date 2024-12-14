package com.Chitti.AiVoiceMail;

import com.Chitti.AiVoiceMail.pojo.ChatHistory;

import java.time.Instant;
import java.util.Arrays;

public class ChatHistoryServiceTest {

    public static ChatHistory getDummyChatHistory() {
        ChatHistory chatHistory = new ChatHistory();

        chatHistory.setId("64b7f3c5b4d8c1234567890a");
        chatHistory.setSessionId("session_123450");
        chatHistory.setUserId("user_12345");
        chatHistory.setTimestamp(Instant.parse("2024-12-13T12:00:00Z"));

        // Messages
        ChatHistory.Message message1 = new ChatHistory.Message();
        message1.setRole("user");
        message1.setContent("Hi, I need to reschedule my meeting for Thursday.");
        message1.setTimestamp(Instant.parse("2024-12-13T12:01:00Z"));

        ChatHistory.Message message2 = new ChatHistory.Message();
        message2.setRole("assistant");
        message2.setContent("Sure, when would you like to reschedule?");
        message2.setTimestamp(Instant.parse("2024-12-13T12:01:10Z"));

        chatHistory.setMessages(Arrays.asList(message1, message2));

        chatHistory.setSummary("User requested to reschedule a meeting for Thursday.");
        chatHistory.setLastUpdated(Instant.parse("2024-12-13T12:01:10Z"));

        // Metadata
        ChatHistory.Metadata metadata = new ChatHistory.Metadata();
        metadata.setAppVersion("1.0.0");
        chatHistory.setMetadata(metadata);

        return chatHistory;
    }
}
