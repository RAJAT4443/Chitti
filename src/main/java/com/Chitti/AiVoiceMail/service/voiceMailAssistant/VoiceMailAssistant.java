package com.Chitti.AiVoiceMail.service.voiceMailAssistant;

import com.Chitti.AiVoiceMail.dtos.VoiceMailAssistantRequestDto;
import org.springframework.web.multipart.MultipartFile;

public interface VoiceMailAssistant {

    public String processVoiceMail(MultipartFile audioFile, VoiceMailAssistantRequestDto voiceMailAssistantRequestDto) throws Exception;


}
