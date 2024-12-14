package com.Chitti.AiVoiceMail.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.List;

@Document(collection = "user_customizations")
public class UserCustomizations implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Field("_id") // Maps the MongoDB _id field
    private String id;

    @Field("preferred_tone") // Maps to the preferred_tone field in MongoDB
    private String preferredTone;

    @Field("categories") // Maps to the categories field in MongoDB
    private List<String> categories;

    @Field("default_language") // Maps to the default_language field in MongoDB
    private String defaultLanguage;

    @Field("voice_preference") // Maps to the voice_preference field in MongoDB
    private String voicePreference;

    @Field("created_at") // Maps to the created_at field in MongoDB
    private Long createdAt;

    @Field("updated_at") // Maps to the updated_at field in MongoDB
    private Long updatedAt;

    @Field("user_id") // Maps to the user_id field in MongoDB
    private String userId;

    @Field("assistant_name") // Maps to the assistant_name field in MongoDB
    private String assistantName;

    @Field("greeting") // Maps to the greeting field in MongoDB
    private String greeting;

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPreferredTone() {
        return preferredTone;
    }

    public void setPreferredTone(String preferredTone) {
        this.preferredTone = preferredTone;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public String getDefaultLanguage() {
        return defaultLanguage;
    }

    public void setDefaultLanguage(String defaultLanguage) {
        this.defaultLanguage = defaultLanguage;
    }

    public String getVoicePreference() {
        return voicePreference;
    }

    public void setVoicePreference(String voicePreference) {
        this.voicePreference = voicePreference;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAssistantName() {
        return assistantName;
    }

    public void setAssistantName(String assistantName) {
        this.assistantName = assistantName;
    }

    public String getGreeting() {
        return greeting;
    }

    public void setGreeting(String greeting) {
        this.greeting = greeting;
    }
}

