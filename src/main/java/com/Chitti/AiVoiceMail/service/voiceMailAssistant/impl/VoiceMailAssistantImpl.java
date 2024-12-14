package com.Chitti.AiVoiceMail.service.voiceMailAssistant.impl;

import com.Chitti.AiVoiceMail.dtos.VoiceMailAssistantRequestDto;
import com.Chitti.AiVoiceMail.entities.UserDetails;
import com.Chitti.AiVoiceMail.models.UserCustomizations;
import com.Chitti.AiVoiceMail.service.db.mongo.UserCustomizationsService;
import com.Chitti.AiVoiceMail.service.db.mysql.ApplicationConfigsService;
import com.Chitti.AiVoiceMail.service.db.mysql.UserDetailsService;
import com.Chitti.AiVoiceMail.service.stt.SpeechToTextService;
import com.Chitti.AiVoiceMail.service.stt.SpeechToTextServiceFactory;
import com.Chitti.AiVoiceMail.service.summarization.SummarizationService;
import com.Chitti.AiVoiceMail.service.summarization.SummarizationServiceFactory;
import com.Chitti.AiVoiceMail.service.tts.TtsService;
import com.Chitti.AiVoiceMail.service.tts.TtsServiceFactory;
import com.Chitti.AiVoiceMail.service.voiceMailAssistant.VoiceMailAssistant;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Primary
public class VoiceMailAssistantImpl implements VoiceMailAssistant {

    private final ApplicationConfigsService applicationConfigsService;

    private final SpeechToTextServiceFactory speechToTextServiceFactory;

    private final TtsServiceFactory ttsServiceFactory;

    private final SummarizationServiceFactory summarizationServiceFactory;

    private final UserDetailsService userDetailsService;

    private final UserCustomizationsService userCustomizationsService;

    public VoiceMailAssistantImpl(SpeechToTextServiceFactory speechToTextServiceFactory, TtsServiceFactory ttsServiceFactory, SummarizationServiceFactory summarizationServiceFactory, ApplicationConfigsService applicationConfigsService, UserDetailsService userDetailsService, UserCustomizationsService userCustomizationsService) {
        this.speechToTextServiceFactory = speechToTextServiceFactory;
        this.ttsServiceFactory = ttsServiceFactory;
        this.summarizationServiceFactory = summarizationServiceFactory;
        this.applicationConfigsService = applicationConfigsService;
        this.userDetailsService = userDetailsService;
        this.userCustomizationsService = userCustomizationsService;
    }

    @Override
    public String processVoiceMail(MultipartFile audioFile, VoiceMailAssistantRequestDto voiceMailAssistantRequestDto) throws Exception {

        try {
            String ttsServiceName = applicationConfigsService.getConfigValue("tts.service.name");
            String sttServiceName = applicationConfigsService.getConfigValue("stt.service.name");
            String summarizationServiceName = applicationConfigsService.getConfigValue("summarization.service.name");


            SpeechToTextService speechToTextService = speechToTextServiceFactory.getSpeechToTextService(sttServiceName);
            TtsService ttsService = ttsServiceFactory.getTtsService(ttsServiceName);
            SummarizationService summarizationService = summarizationServiceFactory.getSummarizationService(summarizationServiceName);

            UserDetails userDetails = userDetailsService.getUserDetailsByPhone(voiceMailAssistantRequestDto.getBParty());

            UserCustomizations userCustomizations = userCustomizationsService.getUserCustomizationsByUserId(userDetails.getUserId());

            String callerText = speechToTextService.convertSpeechToText(audioFile, userCustomizations);

//            String aiResponse = summarizationService.summarizeText(callerText, userCustomizations);



        } catch (Exception e) {
            throw new Exception("An error occurred while processing the voice mail: " + e.getMessage());
        }

        return null;
    }
}
