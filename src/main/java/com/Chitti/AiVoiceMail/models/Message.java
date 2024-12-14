package com.Chitti.AiVoiceMail.models;

import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

public class Message implements Serializable {

    private static final long serialVersionUID = 1L;
    @Field("role") // Maps to "role" in MongoDB
    private String role;

    @Field("content") // Maps to "content" in MongoDB
    private String content;

    @Field("timestamp") // Maps to "timestamp" in MongoDB
    private Long timestamp;

    // Default constructor
    public Message() {
    }

    // Constructor
    public Message(String role, String content) {
        this.role = role;
        this.content = content;
    }

    public Message(String role, String content, Long timestamp) {
        this.role = role;
        this.content = content;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "{" +
                "role:'" + role + '\'' +
                ", content:'" + content + '\'' +
                ", timestamp:" + timestamp +
                '}';
    }
}
