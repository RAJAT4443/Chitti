package com.Chitti.AiVoiceMail.service.voiceMailAssistant;

import com.Chitti.AiVoiceMail.entities.UserDetails;
import com.Chitti.AiVoiceMail.models.ChatHistories;

public interface AssistantResponseService {
    String generateResponse(String inputText, ChatHistories chatHistories, UserDetails userDetails) throws Exception;

    String getType();

}
