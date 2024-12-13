package com.Chitti.AiVoiceMail.service.db.mongo;

import com.Chitti.AiVoiceMail.models.AudioMetadata;
import com.Chitti.AiVoiceMail.repos.mongo.AudioMetadataRepository;
import org.springframework.stereotype.Service;

@Service
public class AudioMetadataService {
    private final AudioMetadataRepository audioMetadataRepository;

    public AudioMetadataService(AudioMetadataRepository audioMetadataRepository) {
        this.audioMetadataRepository = audioMetadataRepository;
    }

    public AudioMetadata getAudioMetadata(String id) {
        return audioMetadataRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Audio metadata not found for ID: " + id));
    }

    public AudioMetadata saveAudioMetadata(AudioMetadata audioMetadata) {
        try {
            return audioMetadataRepository.save(audioMetadata);
        } catch (Exception e) {
            throw new RuntimeException("Error saving audio metadata: " + e.getMessage(), e);
        }
    }

    public void deleteAudioMetadata(String id) {
        if (!audioMetadataRepository.existsById(id)) {
            throw new IllegalArgumentException("Audio metadata not found for ID: " + id);
        }
        audioMetadataRepository.deleteById(id);
    }
}
