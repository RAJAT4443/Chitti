package com.Chitti.AiVoiceMail.service.stt.impl;

import com.Chitti.AiVoiceMail.models.UserCustomizations;
import com.Chitti.AiVoiceMail.service.stt.SpeechToTextService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Primary
public class AzureSpeechToTextService implements SpeechToTextService {

    private static final String TYPE = "Azure";

    @Override
    public String convertSpeechToText(MultipartFile audioFile, UserCustomizations userCustomizations) throws Exception {
        return null;
    }

    @Override
    public String getType() {
        return TYPE.toLowerCase();
    }
}
