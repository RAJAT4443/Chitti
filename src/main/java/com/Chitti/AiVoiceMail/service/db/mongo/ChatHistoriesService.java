package com.Chitti.AiVoiceMail.service.db.mongo;

import com.Chitti.AiVoiceMail.models.ChatHistories;
import com.Chitti.AiVoiceMail.repos.mongo.ChatHistoriesRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class ChatHistoriesService {

    private final ChatHistoriesRepository chatHistoriesRepository;

    public ChatHistoriesService(ChatHistoriesRepository chatHistoriesRepository) {
        this.chatHistoriesRepository = chatHistoriesRepository;
    }

    @Cacheable(value = "chatHistory", key = "'session:' + #sessionId")
    public ChatHistories getChatHistoryBySessionId(String sessionId) {
        return chatHistoriesRepository.findChatHistoriesBySessionId(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Chat history not found for sessionId: " + sessionId));
    }
    @CacheEvict(value = "chatHistory", key = "'session:' + #chatHistories.sessionId")
    public ChatHistories saveChatHistory(ChatHistories chatHistories) {
        try {
            return chatHistoriesRepository.save(chatHistories);
        } catch (Exception e) {
            throw new RuntimeException("Error saving chat history: " + e.getMessage(), e);
        }
    }

    @CacheEvict(value = "chatHistory", key = "'session:' + #sessionId")
    public void deleteChatHistory(String id) {
        if (!chatHistoriesRepository.existsById(id)) {
            throw new IllegalArgumentException("Chat history not found for ID: " + id);
        }
        chatHistoriesRepository.deleteById(id);
    }
}
