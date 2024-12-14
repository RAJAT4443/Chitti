package com.Chitti.AiVoiceMail.service.voiceMailAssistant.impl;

import com.Chitti.AiVoiceMail.entities.UserDetails;
import com.Chitti.AiVoiceMail.models.ChatHistories;
import com.Chitti.AiVoiceMail.service.voiceMailAssistant.AssistantResponseService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class OpenAiAssistantResponseService implements AssistantResponseService {
    @Override
    public String generateResponse(String inputText, ChatHistories chatHistories, UserDetails userDetails) throws Exception {
        return null;
    }
}
