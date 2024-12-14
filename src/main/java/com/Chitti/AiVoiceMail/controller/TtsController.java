package com.Chitti.AiVoiceMail.controller;

import com.Chitti.AiVoiceMail.service.tts.TtsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/chitti/")
public class TtsController {

    @Autowired
    TtsService ttsService;

    @PostMapping("/generatePrompt")
    public ResponseEntity<String> generatePrompt() {
        try {
            ttsService.convertTextToSpeechViaApi("hello", "test");
        }catch(Exception e) {
            e.printStackTrace();
//            log.error("Error while generating prompts ",e);
        }

        return ResponseEntity.ok("Prompt file generated");
    }
}
