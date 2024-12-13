package com.Chitti.AiVoiceMail.service.stt;


import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class SpeechToTextServiceFactory {

    private final Map<String, SpeechToTextService> speechToTextServiceMap;

    public SpeechToTextServiceFactory(List<SpeechToTextService> speechToTextServices) {
        this.speechToTextServiceMap = speechToTextServices.stream()
                .collect(
                        java.util.stream.Collectors.toMap(
                                SpeechToTextService::getType,
                                speechToTextService -> speechToTextService
                        )
                );
    }

    public SpeechToTextService getSpeechToTextService(String type) {
        return speechToTextServiceMap.get(type.toLowerCase());
    }

}
