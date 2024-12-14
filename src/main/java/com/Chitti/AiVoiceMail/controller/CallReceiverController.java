package com.Chitti.AiVoiceMail.controller;

import com.Chitti.AiVoiceMail.entities.UserDetails;
import com.Chitti.AiVoiceMail.models.ChatHistories;
import com.Chitti.AiVoiceMail.service.db.mongo.AudioMetadataService;
import com.Chitti.AiVoiceMail.service.db.mongo.ChatHistoriesService;
import com.Chitti.AiVoiceMail.service.db.mysql.UserDetailsService;
import com.Chitti.AiVoiceMail.service.stt.SpeechToTextService;
import com.Chitti.AiVoiceMail.service.stt.SpeechToTextServiceFactory;
import com.Chitti.AiVoiceMail.service.voiceMailAssistant.AssistantResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping({"/call-receiver"})
public class CallReceiverController {

    @Autowired
    private AudioMetadataService audioMetadataService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private SpeechToTextServiceFactory speechToTextServiceFactory;

    @Autowired
    private ChatHistoriesService chatHistoriesService;

    @Autowired
    private AssistantResponseService openAiAssistantResponseService;

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<ByteArrayResource> transcribeAudio(@RequestParam("audio") MultipartFile audioFile) {
        try {
            if (!audioFile.isEmpty() && audioFile.getContentType().startsWith("audio/")) {
                audioFile.getResource();
                SpeechToTextService speechToTextService = speechToTextServiceFactory.getSpeechToTextService("azure");
                String transcription = speechToTextService.convertSpeechToText(audioFile, null);
                return ResponseEntity.ok(transcription);
            } else {
                return ResponseEntity.badRequest().body("Invalid audio file.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/test/{sessionId}")
    public void test(@PathVariable String sessionId,
                     @RequestParam(name = "inputText") String inputText) throws Exception {
        ChatHistories chatHistories = chatHistoriesService.getChatHistoryBySessionId(sessionId);
        UserDetails userDetails = userDetailsService.getUserDetailsById(1L);
        System.out.println(openAiAssistantResponseService.generateResponse(inputText, chatHistories, userDetails));
//        System.out.println(audioMetadataService.getAudioMetadata("audio1"));
//        System.out.println(userDetailsService.getUserDetailsById(1L));

    }
}