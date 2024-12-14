//package com.Chitti.AiVoiceMail.controller;
//
//import org.json.JSONObject;
//import okhttp3.*;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.File;
//import java.io.IOException;
//import java.nio.file.Files;
//
//@RestController
//@RequestMapping("/chitti")
//public class SummarizationController {
//
//    private static final String OPENAI_API_KEY = "YOUR_OPENAI_API_KEY";
//
//    @PostMapping("/summarize")
//    public ResponseEntity<String> summarizeAudio(@RequestParam("audio") MultipartFile audioFile) {
//        try {
//            // Step 1: Save audio file to a temporary location
//            File tempFile = File.createTempFile("audio", ".wav");
//            Files.copy(audioFile.getInputStream(), tempFile.toPath());
//
//            // Step 2: Transcribe audio using Whisper API
//            String transcription = transcribeAudio(tempFile);
//
//            // Step 3: Summarize transcription using GPT API
//            String summary = generateSummary(transcription);
//
//            // Step 4: Return the summary
//            return ResponseEntity.ok(summary);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(500).body("Error: " + e.getMessage());
//        }
//    }
//
//    private String transcribeAudio(File audioFile) throws IOException {
//        OkHttpClient client = new OkHttpClient();
//
//        // Build the request body using RequestBody.Companion.create
//        RequestBody body = new MultipartBody.Builder()
//                .setType(MultipartBody.FORM)
//                .addFormDataPart("file", audioFile.getName(),
//                        RequestBody.Companion.create(audioFile, MediaType.parse("audio/wav")))
//                .addFormDataPart("model", "whisper-1")
//                .build();
//
//        // Build the HTTP request
//        Request request = new Request.Builder()
//                .url("https://api.openai.com/v1/audio/transcriptions")
//                .addHeader("Authorization", "Bearer " + OPENAI_API_KEY)
//                .post((okhttp3.RequestBody) body)
//                .build();
//
//        // Execute the request
//        try (Response response = client.newCall(request).execute()) {
//            if (!response.isSuccessful()) {
//                throw new IOException("Failed to transcribe audio: " + response);
//            }
//
//            JSONObject responseBody = new JSONObject(response.body().string());
//            return responseBody.getString("text"); // Return the transcribed text
//        }
//    }
//
//    private String generateSummary(String transcription) throws IOException {
//        OkHttpClient client = new OkHttpClient();
//
//        // Create JSON payload for GPT request
//        JSONObject payload = new JSONObject();
//        payload.put("model", "text-davinci-003");
//        payload.put("prompt", "Summarize the following voicemail transcription:\n" + transcription);
//        payload.put("max_tokens", 100);
//        payload.put("temperature", 0.7);
//
//        // Build the HTTP request using RequestBody.Companion.create
//        Request request = new Request.Builder()
//                .url("https://api.openai.com/v1/completions")
//                .addHeader("Authorization", "Bearer " + OPENAI_API_KEY)
//                .post(RequestBody.Companion.create(payload.toString(), MediaType.parse("application/json")))
//                .build();
//
//        // Execute the request
//        try (Response response = client.newCall(request).execute()) {
//            if (!response.isSuccessful()) {
//                throw new IOException("Failed to summarize transcription: " + response);
//            }
//
//            JSONObject responseBody = new JSONObject(response.body().string());
//            return responseBody.getJSONArray("choices").getJSONObject(0).getString("text").trim();
//        }
//    }
//
//}
