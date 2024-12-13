package com.Chitti.AiVoiceMail.service.stt;

import com.Chitti.AiVoiceMail.models.UserCustomizations;
import org.springframework.web.multipart.MultipartFile;

public interface SpeechToTextService {

    public String convertSpeechToText(MultipartFile audioFile, UserCustomizations userCustomizations) throws Exception;

    String getType();
}
