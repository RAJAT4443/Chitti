package com.Chitti.AiVoiceMail.service.summarization;


import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SummarizationServiceFactory {

    private final Map<String, SummarizationService> summarizationServiceMap;

    public SummarizationServiceFactory(List<SummarizationService> summarizationServiceList) {
        this.summarizationServiceMap = summarizationServiceList.stream()
                .collect(
                        java.util.stream.Collectors.toMap(
                                summarizationService -> summarizationService.getType().toLowerCase(),
                                summarizationService -> summarizationService
                        )
                );
    }

    public SummarizationService getSummarizationService(String type) {
        return summarizationServiceMap.get(type.toLowerCase());
    }

}
