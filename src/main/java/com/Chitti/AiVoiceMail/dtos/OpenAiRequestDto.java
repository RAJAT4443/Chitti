package com.Chitti.AiVoiceMail.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class OpenAiRequestDto {

    @JsonProperty("model")
    private String model;
    @JsonProperty("messages")
    private List<OpenAiMessageDto> messages;
    @JsonProperty("max_tokens")
    private int max_tokens;
    @JsonProperty("temperature")
    private double temperature;

    public OpenAiRequestDto(String model, List<OpenAiMessageDto> messages, int maxTokens, double temperature) {
        this.model = model;
        this.messages = messages;
        this.max_tokens = maxTokens;
        this.temperature = temperature;
    }

    // Getters and Setters
    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<OpenAiMessageDto> getMessages() {
        return messages;
    }

    public void setMessages(List<OpenAiMessageDto> messages) {
        this.messages = messages;
    }

    public int getMax_tokens() {
        return max_tokens;
    }

    public void setMax_tokens(int max_tokens) {
        this.max_tokens = max_tokens;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    @Override
    public String toString() {
        return "OpenAiRequestDto{" +
                "model='" + model + '\'' +
                ", messages=" + messages +
                ", max_tokens=" + max_tokens +
                ", temperature=" + temperature +
                '}';
    }
}
