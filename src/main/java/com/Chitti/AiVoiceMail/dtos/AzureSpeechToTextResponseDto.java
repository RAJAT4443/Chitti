package com.Chitti.AiVoiceMail.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AzureSpeechToTextResponseDto {


    @JsonProperty("combinedPhrases")
    private List<AzureResponsePhrase> combinedPhrases;

    // Add other necessary fields from the API response
    @JsonProperty("status")
    private String status;

    @JsonProperty("createdDateTime")
    private String createdDateTime;

    // Getters and Setters
    public List<AzureResponsePhrase> getCombinedPhrases() {
        return combinedPhrases;
    }

    public void setCombinedPhrases(List<AzureResponsePhrase> combinedPhrases) {
        this.combinedPhrases = combinedPhrases;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(String createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

}
