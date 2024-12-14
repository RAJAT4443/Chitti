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

    public String getRole() {
        return this.role;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "OpenAiMessageDto{" +
                "role='" + role + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

}
