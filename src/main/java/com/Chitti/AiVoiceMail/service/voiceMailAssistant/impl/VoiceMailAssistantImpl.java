package com.Chitti.AiVoiceMail.service.voiceMailAssistant.impl;

import com.Chitti.AiVoiceMail.dtos.AudioResponseDto;
import com.Chitti.AiVoiceMail.dtos.VoiceMailAssistantRequestDto;
import com.Chitti.AiVoiceMail.entities.UserDetails;
import com.Chitti.AiVoiceMail.models.ChatHistories;
import com.Chitti.AiVoiceMail.models.UserCustomizations;
import com.Chitti.AiVoiceMail.service.db.mongo.ChatHistoriesService;
import com.Chitti.AiVoiceMail.service.db.mongo.UserCustomizationsService;
import com.Chitti.AiVoiceMail.service.db.mysql.ApplicationConfigsService;
import com.Chitti.AiVoiceMail.service.db.mysql.UserDetailsService;
import com.Chitti.AiVoiceMail.service.stt.SpeechToTextService;
import com.Chitti.AiVoiceMail.service.stt.SpeechToTextServiceFactory;
import com.Chitti.AiVoiceMail.service.summarization.SummarizationService;
import com.Chitti.AiVoiceMail.service.summarization.SummarizationServiceFactory;
import com.Chitti.AiVoiceMail.service.tts.TtsService;
import com.Chitti.AiVoiceMail.service.tts.TtsServiceFactory;
import com.Chitti.AiVoiceMail.service.voiceMailAssistant.AssistantResponseFactory;
import com.Chitti.AiVoiceMail.service.voiceMailAssistant.AssistantResponseService;
import com.Chitti.AiVoiceMail.service.voiceMailAssistant.VoiceMailAssistant;
import com.Chitti.AiVoiceMail.utilities.Utilities;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@Primary
public class VoiceMailAssistantImpl implements VoiceMailAssistant {

    private final ApplicationConfigsService applicationConfigsService;

    private final SpeechToTextServiceFactory speechToTextServiceFactory;

    private final TtsServiceFactory ttsServiceFactory;

    private final SummarizationServiceFactory summarizationServiceFactory;

    private final UserDetailsService userDetailsService;

    private final UserCustomizationsService userCustomizationsService;

    private final ChatHistoriesService chatHistoriesService;

    private final AssistantResponseFactory assistantResponseFactory;

    public VoiceMailAssistantImpl(SpeechToTextServiceFactory speechToTextServiceFactory, TtsServiceFactory ttsServiceFactory, SummarizationServiceFactory summarizationServiceFactory, ApplicationConfigsService applicationConfigsService, UserDetailsService userDetailsService, UserCustomizationsService userCustomizationsService, ChatHistoriesService chatHistoriesService, AssistantResponseFactory assistantResponseFactory) {
        this.speechToTextServiceFactory = speechToTextServiceFactory;
        this.ttsServiceFactory = ttsServiceFactory;
        this.summarizationServiceFactory = summarizationServiceFactory;
        this.applicationConfigsService = applicationConfigsService;
        this.userDetailsService = userDetailsService;
        this.userCustomizationsService = userCustomizationsService;
        this.chatHistoriesService = chatHistoriesService;
        this.assistantResponseFactory = assistantResponseFactory;
    }

    @Override
    public CompletableFuture<AudioResponseDto> processVoiceMail(MultipartFile audioFile, VoiceMailAssistantRequestDto voiceMailAssistantRequestDto) throws Exception {

        try {
            String ttsServiceName = applicationConfigsService.getConfigValue("tts.service.name");
            String sttServiceName = applicationConfigsService.getConfigValue("stt.service.name");
            String summarizationServiceName = applicationConfigsService.getConfigValue("summarization.service.name");
            String assistantResponseServiceName = applicationConfigsService.getConfigValue("ai.response.service.name");

            SpeechToTextService speechToTextService = speechToTextServiceFactory.getSpeechToTextService(sttServiceName);

            TtsService ttsService = ttsServiceFactory.getTtsService(ttsServiceName);
            SummarizationService summarizationService = summarizationServiceFactory.getSummarizationService(summarizationServiceName);
            AssistantResponseService assistantResponseService = assistantResponseFactory.getAssistantResponseService(assistantResponseServiceName);

            UserDetails userDetails = userDetailsService.getUserDetailsByPhone(voiceMailAssistantRequestDto.getBParty());

            ChatHistories chatHistories = getChatHistoryBySessionId(voiceMailAssistantRequestDto.getSessionId(), userDetails.getUserId());

            UserCustomizations userCustomizations = userCustomizationsService.getUserCustomizationsByUserId(userDetails.getUserId());

            String callerText = speechToTextService.convertSpeechToText(audioFile, userCustomizations);

            String aiResponse = assistantResponseService.generateResponse(callerText, chatHistories, userDetails);

            CompletableFuture<AudioResponseDto> ttsResponse = ttsService.convertTextToSpeechViaApi(aiResponse, generateFileName(chatHistories.getSessionId(), chatHistories.getMessages().size()), userDetails.getUserId(), chatHistories.getSessionId());

//            String summaryResponse = summarizationService.generateSummaryAndActionableInsights(chatHistories, userDetails);

            return ttsResponse;

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("An error occurred while processing the voice mail: " + e.getMessage());
        }
    }

    private String generateFileName(String sessionId, int size) {
        return sessionId + "_" + (size/2) + ".mp3";
    }

    private ChatHistories getChatHistoryBySessionId(String sessionId, String userId) {
        ChatHistories chatHistories = chatHistoriesService.getChatHistoryBySessionId(sessionId);
        return Optional.of(chatHistories).orElse(generateChatHistory(userId));
    }

    public ChatHistories generateChatHistory(String userId){
        ChatHistories chatHistories = new ChatHistories();
        chatHistories.setSessionId(Utilities.generateSessionId());
        chatHistories.setMessages(new ArrayList<>());
        chatHistories.setUserId(userId);
        chatHistories.setTimestamp(System.currentTimeMillis());
        chatHistories.setLastUpdated(System.currentTimeMillis());
        return chatHistories;
    }
}
