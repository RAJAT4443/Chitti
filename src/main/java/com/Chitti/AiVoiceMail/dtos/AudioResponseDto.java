package com.Chitti.AiVoiceMail.dtos;

import com.Chitti.AiVoiceMail.models.AudioMetadata;

public class AudioResponseDto {
    private byte[] audioBytes;
    private AudioMetadata audioMetadata;

    // Getters and Setters
    public byte[] getAudioBytes() {
        return audioBytes;
    }

    public void setAudioBytes(byte[] audioBytes) {
        this.audioBytes = audioBytes;
    }

    public AudioMetadata getAudioMetadata() {
        return audioMetadata;
    }

    public void setAudioMetadata(AudioMetadata audioMetadata) {
        this.audioMetadata = audioMetadata;
    }
}
