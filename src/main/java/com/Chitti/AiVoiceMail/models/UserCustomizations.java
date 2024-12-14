package com.Chitti.AiVoiceMail.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

@Document(collection = "user_customizations")
public class UserCustomizations implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private String id;

    private String preferredTone;

    private List<String> categories;

    private String defaultLanguage;

    private String greetings;

    private String voicePreference;

    private Long createdAt;

    private Long updatedAt;

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

    public String getGreetings() {
        return greetings;
    }

    public void setGreetings(String greetings) {
        this.greetings = greetings;
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
}

