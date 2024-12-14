package com.Chitti.AiVoiceMail.dtos;

import java.util.List;

public class OpenAiRequestDto {

    private String model;
    private List<OpenAiMessageDto> messages;
    private int max_tokens;
    private double temperature;

    public OpenAiRequestDto(String model, List<OpenAiMessageDto> messages, int maxTokens, double temperature) {
        this.model = model;
        this.messages = messages;
        this.max_tokens = maxTokens;
        this.temperature = temperature;
    }

}
