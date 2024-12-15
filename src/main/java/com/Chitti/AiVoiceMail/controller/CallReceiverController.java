package com.Chitti.AiVoiceMail.controller;

import com.Chitti.AiVoiceMail.dtos.AudioResponseDto;
import com.Chitti.AiVoiceMail.dtos.VoiceMailAssistantRequestDto;
import com.Chitti.AiVoiceMail.entities.UserDetails;
import com.Chitti.AiVoiceMail.models.ChatHistories;
import com.Chitti.AiVoiceMail.service.db.mongo.AudioMetadataService;
import com.Chitti.AiVoiceMail.service.db.mongo.ChatHistoriesService;
import com.Chitti.AiVoiceMail.service.db.mysql.UserDetailsService;
import com.Chitti.AiVoiceMail.service.stt.SpeechToTextServiceFactory;
import com.Chitti.AiVoiceMail.service.voiceMailAssistant.AssistantResponseService;
import com.Chitti.AiVoiceMail.service.voiceMailAssistant.VoiceMailAssistant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.CompletableFuture;

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

    @Autowired
    private VoiceMailAssistant voiceMailAssistant;

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<ByteArrayResource> transcribeAudio(@RequestParam("audio") MultipartFile audioFile,
                                                             @RequestParam(name = "sessionId", required = false) String sessionId,
                                                             @RequestParam(name = "aParty") String aParty,
                                                             @RequestParam(name = "bParty") String bParty) {
        try {
            if (!audioFile.isEmpty() && audioFile.getContentType().startsWith("audio/")) {
                audioFile.getResource();
                VoiceMailAssistantRequestDto voiceMailAssistantRequestDto = new VoiceMailAssistantRequestDto(aParty, bParty, sessionId);
                CompletableFuture<AudioResponseDto> voiceMailFutureResponse = voiceMailAssistant.processVoiceMail(audioFile, voiceMailAssistantRequestDto);

                AudioResponseDto audioResponseDto = voiceMailFutureResponse.get();

                audioMetadataService.addAudioMetadataAsync(audioResponseDto.getAudioMetadata());

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.parseMediaType("audio/wav"));
                headers.setContentDispositionFormData("attachment", audioResponseDto.getAudioMetadata().getFilePath());
                return new ResponseEntity<>(new ByteArrayResource(audioResponseDto.getAudioBytes()), headers, HttpStatus.OK);
            } else {
                return ResponseEntity.badRequest().body(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
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