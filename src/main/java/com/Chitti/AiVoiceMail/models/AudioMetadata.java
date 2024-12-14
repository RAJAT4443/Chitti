package com.Chitti.AiVoiceMail.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

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
    private LocalDateTime createdAt;

    public AudioMetadata() {
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

