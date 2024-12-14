package com.Chitti.AiVoiceMail.service.stt.impl;

import com.Chitti.AiVoiceMail.dtos.AzureSpeechToTextRequestDto;
import com.Chitti.AiVoiceMail.dtos.AzureSpeechToTextResponseDto;
import com.Chitti.AiVoiceMail.models.UserCustomizations;
import com.Chitti.AiVoiceMail.service.db.mysql.ApplicationConfigsService;
import com.Chitti.AiVoiceMail.service.stt.SpeechToTextService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Primary;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

@Service
@Primary
public class AzureSpeechToTextService implements SpeechToTextService {
    private static final String TYPE = "Azure";

    private final ApplicationConfigsService applicationConfigsService;

    private final ObjectMapper objectMapper;

    private final RestTemplate restTemplate;

    public AzureSpeechToTextService(ApplicationConfigsService applicationConfigsService, ObjectMapper objectMapper, RestTemplate restTemplate) {
        this.applicationConfigsService = applicationConfigsService;
        this.objectMapper = objectMapper;
        this.restTemplate = restTemplate;
    }

    @Override
    public String convertSpeechToText(MultipartFile audioFile, UserCustomizations userCustomizations) throws Exception {
        // Fetch configurations from ApplicationConfigsService
        String speechApiUrl = applicationConfigsService.getConfigValue("azure.speech.api.url");
        String apiVersion = applicationConfigsService.getConfigValue("azure.speech.api.version");
        String apiKey = applicationConfigsService.getConfigValue("azure.speech.subscription.key");

        // Validate configurations
        if (speechApiUrl == null || apiVersion == null || apiKey == null) {
            throw new Exception("Missing required API configurations.");
        }

        // Build URI
        String uri = UriComponentsBuilder.fromHttpUrl(speechApiUrl)
                .path("/transcriptions:transcribe")
                .queryParam("api-version", apiVersion)
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Ocp-Apim-Subscription-Key", apiKey);
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        // Create the `definition` object dynamically
        AzureSpeechToTextRequestDto definition = new AzureSpeechToTextRequestDto(
                Arrays.asList("en-US", "ja-JP", "es-ES", "fr-FR")
        );

        // Serialize the `definition` object to JSON
        String definitionJson = objectMapper.writeValueAsString(definition);

        // Create form data
        MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();
        formData.add("audio", audioFile.getResource());
        formData.add("definition", definitionJson);

        // Create request entity
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(formData, headers);

        try {
            // Make the API call
            ResponseEntity<AzureSpeechToTextResponseDto> response = restTemplate.exchange(
                    uri, HttpMethod.POST, requestEntity, AzureSpeechToTextResponseDto.class);


            AzureSpeechToTextResponseDto azureSpeechToTextResponseDto = Optional.of(response.getBody()).orElseThrow(() -> new Exception("Error during Speech-to-Text API call: empty response"));
//             Return the response body
            return azureSpeechToTextResponseDto.getCombinedPhrases().get(0).getText();
        } catch (Exception ex) {
            throw new Exception("Error during Speech-to-Text API call: " + ex.getMessage(), ex);
        }
    }

    @Override
    public String getType() {
        return TYPE.toLowerCase();
    }
}
