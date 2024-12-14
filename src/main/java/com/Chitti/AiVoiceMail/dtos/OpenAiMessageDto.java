package com.Chitti.AiVoiceMail.dtos;

public class OpenAiMessageDto {
    private String role;
    private String content;

    public OpenAiMessageDto(String role, String content) {
        this.role = role;
        this.content = content;
    }

    public String getContent() {
        return this.content;
    }
}
