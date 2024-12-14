package com.Chitti.AiVoiceMail.service.summarization;

import com.Chitti.AiVoiceMail.entities.UserDetails;
import com.Chitti.AiVoiceMail.models.ChatHistories;

import java.io.IOException;

public interface SummarizationService {


    String summarizeSession(String sessionId);

    String generateSummaryAndActionableInsights(ChatHistories chatHistory, UserDetails userDetails) throws IOException;

    String getType();

}
