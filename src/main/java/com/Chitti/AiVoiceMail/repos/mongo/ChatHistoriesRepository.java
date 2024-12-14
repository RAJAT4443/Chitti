package com.Chitti.AiVoiceMail.repos.mongo;

import com.Chitti.AiVoiceMail.models.ChatHistories;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatHistoriesRepository extends MongoRepository<ChatHistories, String> {
    Optional<ChatHistories> findChatHistoriesBySessionId(String sessionId);
}
