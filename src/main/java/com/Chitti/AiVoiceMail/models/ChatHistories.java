package com.Chitti.AiVoiceMail.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.List;

@Document(collection = "chat_histories")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatHistories implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;
    @Field("session_id")
    private String sessionId;

    @Field("user_id")
    private String userId;


    @Field("timestamp")
    private Long timestamp;

    @Field("messages")
    private List<Message> messages;

    @Field("last_updated")
    private Long lastUpdated;

    public void addMessage(Message message) {
        this.messages.add(message);
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public Long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

}

