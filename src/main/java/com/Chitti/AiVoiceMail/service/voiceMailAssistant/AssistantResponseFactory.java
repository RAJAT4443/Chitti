package com.Chitti.AiVoiceMail.service.voiceMailAssistant;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AssistantResponseFactory {

    private final Map<String, AssistantResponseService> assistantResponseServiceMap;

    public AssistantResponseFactory(List<AssistantResponseService> assistantResponseServiceList) {
        this.assistantResponseServiceMap = assistantResponseServiceList.stream()
                .collect(
                        java.util.stream.Collectors
                                .toMap(assistantResponseService -> assistantResponseService.getType(), assistantResponseService -> assistantResponseService)
                );
    }

    public AssistantResponseService getAssistantResponseService(String type) {
        return assistantResponseServiceMap.get(type.toLowerCase());
    }

}
