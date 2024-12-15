package com.Chitti.AiVoiceMail.service.voiceMailAssistant;

import com.Chitti.AiVoiceMail.dtos.AudioResponseDto;
import com.Chitti.AiVoiceMail.dtos.VoiceMailAssistantRequestDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.CompletableFuture;

public interface VoiceMailAssistant {

    public CompletableFuture<AudioResponseDto> processVoiceMail(MultipartFile audioFile, VoiceMailAssistantRequestDto voiceMailAssistantRequestDto) throws Exception;


}
