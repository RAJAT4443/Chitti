package com.Chitti.AiVoiceMail.service.tts;

public interface TtsService {
    public String convertTextToSpeechViaApi(String text, String fileName) throws Exception;

    public String getType();

}
