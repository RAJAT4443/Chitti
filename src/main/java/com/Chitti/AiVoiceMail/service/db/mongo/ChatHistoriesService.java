package com.Chitti.AiVoiceMail.service.db.mongo;

import com.Chitti.AiVoiceMail.models.ChatHistories;
import com.Chitti.AiVoiceMail.repos.mongo.ChatHistoriesRepository;
import org.springframework.stereotype.Service;

@Service
public class ChatHistoriesService {

    private final ChatHistoriesRepository chatHistoriesRepository;

    public ChatHistoriesService(ChatHistoriesRepository chatHistoriesRepository) {
        this.chatHistoriesRepository = chatHistoriesRepository;
    }

    public ChatHistories getChatHistoryBySessionId(String sessionId) {
        return chatHistoriesRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Chat history not found for sessionId: " + sessionId));
    }

    public ChatHistories saveChatHistory(ChatHistories chatHistories) {
        try {
            return chatHistoriesRepository.save(chatHistories);
        } catch (Exception e) {
            throw new RuntimeException("Error saving chat history: " + e.getMessage(), e);
        }
    }

    public void deleteChatHistory(String id) {
        if (!chatHistoriesRepository.existsById(id)) {
            throw new IllegalArgumentException("Chat history not found for ID: " + id);
        }
        chatHistoriesRepository.deleteById(id);
    }
}
