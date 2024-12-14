package com.Chitti.AiVoiceMail.service.summarization;

import java.io.IOException;

public interface SummarizationService {


    public String summarizeSession(String sessionId);

    public  String generateSummaryAndActionableInsights(String chatHistory)throws IOException;




}
