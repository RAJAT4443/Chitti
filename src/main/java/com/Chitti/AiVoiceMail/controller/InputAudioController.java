package com.Chitti.AiVoiceMail.controller;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/chitti")
@CrossOrigin(origins = "*")
public class InputAudioController {




    @PostMapping("/api/process-audio")
    public ResponseEntity<ByteArrayResource> processAudio(@RequestParam("audio") MultipartFile audioFile) {
        try {
            // Retrieve the content of the audio file
            byte[] audioBytes = audioFile.getBytes();

            // Wrap the audio bytes in a ByteArrayResource
            ByteArrayResource resource = new ByteArrayResource(audioBytes);

            // Create HTTP headers for the response
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(audioFile.getContentType()));
            headers.setContentDispositionFormData("attachment", audioFile.getOriginalFilename());

            // Return the same audio file in the response
            return new ResponseEntity<>(resource, headers, HttpStatus.OK);

        } catch (IOException e) {
            // Handle the exception if there is an issue reading the file
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }


}
