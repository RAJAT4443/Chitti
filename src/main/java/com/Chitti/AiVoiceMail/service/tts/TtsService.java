package com.Chitti.AiVoiceMail.service.tts;

import com.Chitti.AiVoiceMail.dtos.AudioResponseDto;

import java.util.concurrent.CompletableFuture;

public interface TtsService {
    public CompletableFuture<AudioResponseDto> convertTextToSpeechViaApi(String text, String fileName, String userId, String sessionId) throws Exception;

    public String getType();

}
