package com.Chitti.AiVoiceMail.service;

public interface TtsService {


    public String convertTextToSpeechViaApi(String text, String fileName) throws Exception;
}
