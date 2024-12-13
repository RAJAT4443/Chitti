package com.Chitti.AiVoiceMail.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping({"/call-receiver"})
public class CallReceiverController {
    public CallReceiverController() {
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<String> transcribeAudio(@RequestParam("audio") MultipartFile audioFile) {
        try {
            if (!audioFile.isEmpty() && audioFile.getContentType().startsWith("audio/")) {
                audioFile.getResource();
                return ResponseEntity.ok("ok");
            } else {
                return ResponseEntity.badRequest().body("Invalid audio file.");
            }
        } catch (Exception var3) {
            return ResponseEntity.status(500).body("An error occurred: " + var3.getMessage());
        }
    }
}