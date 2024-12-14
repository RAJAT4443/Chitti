package com.Chitti.AiVoiceMail.dtos;

public class Configurations {
    private String endpoint;
    private String promptTemplate;
    private String model;
    private String secretKey;
    private int maxTokens;
    private double temperature;
    private String inputTextPromptTemplate;

    public Configurations(String endpoint, String promptTemplate, String model, String secretKey, int maxTokens, double temperature, String inputTextPromptTemplate) {
        this.endpoint = endpoint;
        this.promptTemplate = promptTemplate;
        this.model = model;
        this.secretKey = secretKey;
        this.maxTokens = maxTokens;
        this.temperature = temperature;
        this.inputTextPromptTemplate = inputTextPromptTemplate;
    }

    // Getters and Setters
    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getPromptTemplate() {
        return promptTemplate;
    }

    public void setPromptTemplate(String promptTemplate) {
        this.promptTemplate = promptTemplate;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public int getMaxTokens() {
        return maxTokens;
    }

    public void setMaxTokens(int maxTokens) {
        this.maxTokens = maxTokens;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public String getInputTextPromptTemplate() {
        return inputTextPromptTemplate;
    }

    public void setInputTextPromptTemplate(String inputTextPromptTemplate) {
        this.inputTextPromptTemplate = inputTextPromptTemplate;
    }
}
