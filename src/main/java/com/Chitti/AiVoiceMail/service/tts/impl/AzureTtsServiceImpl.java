package com.Chitti.AiVoiceMail.service.tts.impl;

import com.Chitti.AiVoiceMail.dtos.AudioResponseDto;
import com.Chitti.AiVoiceMail.models.AudioMetadata;
import com.Chitti.AiVoiceMail.service.db.mongo.AudioMetadataService;
import com.Chitti.AiVoiceMail.service.db.mysql.ApplicationConfigsService;
import com.Chitti.AiVoiceMail.service.tts.TtsService;
import com.Chitti.AiVoiceMail.utilities.Utilities;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class AzureTtsServiceImpl implements TtsService {

    private static final String TYPE = "azure";

    @Value("${speech.key}")
    private String apiKey;

    @Value("${speech.region}")
    private String region;

    @Value("${local.file.path}")
    private String localFilePath;

    @Value("${voice.name}")
    private String voiceName;

    @Value("${azure.endpoint}")
    private String azureEndpoint;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ApplicationConfigsService applicationConfigService;

    @Autowired
    private AudioMetadataService audioMetadataService;

    @Override
    public CompletableFuture<AudioResponseDto> convertTextToSpeechViaApi(String text, String fileName, String userId, String sessionId) throws Exception {
        // 1. Fetch Azure configurations from the database
        String azureEndpoint = applicationConfigService.getConfigValue("azure.tts.endpoint");
        String apiKey = applicationConfigService.getConfigValue("azure.speech.subscription.key");
        String voiceName = applicationConfigService.getConfigValue("azure.tts.voiceName");
        String outputFormat = applicationConfigService.getConfigValue("azure.tts.outputFormat");
        String baseFilePath = applicationConfigService.getConfigValue("azure.tts.baseFilePath");

        // Step 2: Generate output file path
        String outputFilePath = prepareOutputFilePath(baseFilePath, fileName);

        // Step 3: Create SSML content
        String ssml = generateSsmlContent(voiceName, text);

        // Step 4: Perform API call to Azure TTS
        byte[] audioBytes = fetchAudioFromAzureTTS(ssml, azureEndpoint, apiKey);

        // Step 5: Save the audio file asynchronously
        saveAudioFileAsync(audioBytes, outputFilePath);

        // Step 6: Build AudioMetadata object
        AudioMetadata audioMetadata = createAudioMetadata(audioBytes, sessionId, userId, outputFilePath);

        // Step 7: Build and return the response DTO
        AudioResponseDto responseDto = buildAudioResponseDto(audioBytes, audioMetadata);
        return CompletableFuture.completedFuture(responseDto);

    }

    private AudioResponseDto buildAudioResponseDto(byte[] audioBytes, AudioMetadata audioMetadata) {
        AudioResponseDto responseDto = new AudioResponseDto();
        responseDto.setAudioBytes(audioBytes);
        responseDto.setAudioMetadata(audioMetadata);
        return responseDto;
    }

    private AudioMetadata createAudioMetadata(byte[] audioBytes, String sessionId, String userId, String outputFilePath) {
        AudioMetadata audioMetadata = new AudioMetadata();
        audioMetadata.setId(UUID.randomUUID().toString());
        audioMetadata.setSessionId(sessionId);
        audioMetadata.setUserId(userId);
        audioMetadata.setFilePath(outputFilePath);
//        audioMetadata.setDurationSeconds(Utilities.estimateDuration(audioBytes)); // Assume this method calculates duration
        audioMetadata.setFileFormat("wav");
        audioMetadata.setTranscriptionStatus("completed");
        audioMetadata.setCreatedAt(System.currentTimeMillis());
        return audioMetadata;
    }


    private void saveAudioFileAsync(byte[] audioBytes, String outputFilePath) {
        CompletableFuture.runAsync(() -> {
            try {
                Utilities.writeResponseToFile(audioBytes, outputFilePath);
            } catch (Exception e) {
                throw new RuntimeException("Failed to write audio file: " + e.getMessage(), e);
            }
        });
    }

    private byte[] fetchAudioFromAzureTTS(String ssml, String azureEndpoint, String apiKey) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/ssml+xml"));
        headers.set("Ocp-Apim-Subscription-Key", apiKey);
        headers.set("X-Microsoft-OutputFormat", "riff-16khz-16bit-mono-pcm");

        HttpEntity<String> requestEntity = new HttpEntity<>(ssml, headers);

        try {
            ResponseEntity<byte[]> responseEntity = restTemplate.exchange(
                    azureEndpoint, HttpMethod.POST, requestEntity, byte[].class
            );
            byte[] audioBytes = responseEntity.getBody();
            if (audioBytes == null || audioBytes.length == 0) {
                throw new RuntimeException("Azure TTS returned an empty response.");
            }
            return audioBytes;
        } catch (Exception e) {
            throw new RuntimeException("Error during Azure TTS API call: " + e.getMessage(), e);
        }
    }

    private String generateSsmlContent(String voiceName, String text) {
        return String.format(
                "<speak version='1.0' xmlns='http://www.w3.org/2001/10/synthesis' xml:lang='en-US'>" +
                        "<voice name='%s'>%s</voice></speak>",
                voiceName, text
        );
    }
    /**
     * Maps the Azure TTS output format to a file extension.
     *
     * @param outputFormat The Azure output format (e.g., "riff-16khz-16bit-mono-pcm").
     * @return The corresponding file extension (e.g., ".wav").
     */
    private String getFileExtensionFromFormat(String outputFormat) {
        if (outputFormat.toLowerCase().contains("pcm") || outputFormat.toLowerCase().contains("riff")) {
            return ".wav";
        }
        return ".mp3"; // Default to .mp3 for other formats
    }



    private String prepareOutputFilePath(String baseFilePath, String fileName) throws Exception {
        String outputFilePath = Utilities.getOutputFilePath(baseFilePath, fileName);
        Utilities.ensureDirectoryExists(baseFilePath);
        return outputFilePath;
    }

    @Override
    public String getType() {
        return TYPE.toLowerCase();
    }


}
