package com.Chitti.AiVoiceMail.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class AzureSpeechToTextRequestDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("locales")
    private List<String> locales;

    // Constructor
    public AzureSpeechToTextRequestDto(List<String> locales) {
        this.locales = locales;
    }

    // Getters and Setters
    public List<String> getLocales() {
        return locales;
    }

    public void setLocales(List<String> locales) {
        this.locales = locales;
    }

}
