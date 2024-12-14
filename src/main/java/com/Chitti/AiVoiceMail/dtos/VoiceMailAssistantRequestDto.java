package com.Chitti.AiVoiceMail.dtos;

public class VoiceMailAssistantRequestDto {
    private String aParty;
    private String bParty;
    private String sessionId;

    public String getBParty() {
        return bParty;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getAParty() {
        return aParty;
    }

    public void setAParty(String aParty) {
        this.aParty = aParty;
    }

    public void setBParty(String bParty) {
        this.bParty = bParty;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public VoiceMailAssistantRequestDto(String aParty, String bParty, String sessionId) {
        this.aParty = aParty;
        this.bParty = bParty;
        this.sessionId = sessionId;
    }
}
