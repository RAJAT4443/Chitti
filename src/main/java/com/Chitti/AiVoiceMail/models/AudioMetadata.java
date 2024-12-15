package com.Chitti.AiVoiceMail.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serial;
import java.io.Serializable;

@Document(collection = "audio_metadata")
public class AudioMetadata implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    private String id;
    private String sessionId;
    private String userId;
    private String filePath;
    private int durationSeconds;
    private String fileFormat;
    private String transcriptionStatus;
    private Long createdAt;

    public AudioMetadata() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getDurationSeconds() {
        return durationSeconds;
    }

    public void setDurationSeconds(int durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

    public String getFileFormat() {
        return fileFormat;
    }

    public void setFileFormat(String fileFormat) {
        this.fileFormat = fileFormat;
    }

    public String getTranscriptionStatus() {
        return transcriptionStatus;
    }

    public void setTranscriptionStatus(String transcriptionStatus) {
        this.transcriptionStatus = transcriptionStatus;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "AudioMetadata{" +
                "id='" + id + '\'' +
                ", sessionId='" + sessionId + '\'' +
                ", userId='" + userId + '\'' +
                ", filePath='" + filePath + '\'' +
                ", durationSeconds=" + durationSeconds +
                ", fileFormat='" + fileFormat + '\'' +
                ", transcriptionStatus='" + transcriptionStatus + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}

