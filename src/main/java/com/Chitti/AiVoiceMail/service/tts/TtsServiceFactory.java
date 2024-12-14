package com.Chitti.AiVoiceMail.service.tts;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class TtsServiceFactory {

    private final Map<String, TtsService> ttsServiceMap;

    public TtsServiceFactory(List<TtsService> ttsServiceList) {
        this.ttsServiceMap = ttsServiceList.stream()
                .collect(
                        java.util.stream.Collectors.toMap(
                                ttsService -> ttsService.getType().toLowerCase(),
                                ttsService -> ttsService
                        )
                );
    }

    public TtsService getTtsService(String type) {
        return ttsServiceMap.get(type.toLowerCase());
    }

}
