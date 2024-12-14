package com.Chitti.AiVoiceMail.service.summarization;

import java.io.IOException;

public interface SummarizationService {


    String summarizeSession(String sessionId);

    String generateSummaryAndActionableInsights(String chatHistory) throws IOException;

    String getType();

}
