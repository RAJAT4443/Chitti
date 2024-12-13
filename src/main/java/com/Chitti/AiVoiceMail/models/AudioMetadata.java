package com.Chitti.AiVoiceMail.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "audio_metadata")
public class AudioMetadata {
    @Id
    private String id;
    private String sessionId;
    private String userId;
    private String filePath;
    private int durationSeconds;
    private String fileFormat;
    private String transcriptionStatus;
    private List<ProcessingLog> processingLogs;
    private LocalDateTime createdAt;

    public AudioMetadata() {
    }

    public static class ProcessingLog {
        private String stage;
        private LocalDateTime timestamp;

        // Getters and Setters
        public String getStage() {
            return stage;
        }

        public void setStage(String stage) {
            this.stage = stage;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getDurationSeconds() {
        return this.durationSeconds;
    }

    public void setDurationSeconds(int durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

    public String getFileFormat() {
        return this.fileFormat;
    }

    public void setFileFormat(String fileFormat) {
        this.fileFormat = fileFormat;
    }

    public String getTranscriptionStatus() {
        return this.transcriptionStatus;
    }

    public void setTranscriptionStatus(String transcriptionStatus) {
        this.transcriptionStatus = transcriptionStatus;
    }

    public List<ProcessingLog> getProcessingLogs() {
        return this.processingLogs;
    }

    public void setProcessingLogs(List<ProcessingLog> processingLogs) {
        this.processingLogs = processingLogs;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

