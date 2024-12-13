package com.Chitti.AiVoiceMail.service.impl;

import com.Chitti.AiVoiceMail.service.TtsService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TtsServiceImpl implements TtsService {

    public String convertTextToSpeechViaApi(String text, String fileName,String requestId) throws Exception {
        String outputFilePath = Utilities.getOutputFilePath(localFilePath,fileName);
        log.info("Output file path: {}", outputFilePath);

        Utilities.ensureDirectoryExists(localFilePath);

        // Prepare the SSML request body
        String ssml = String.format("<speak version='1.0' xmlns='http://www.w3.org/2001/10/synthesis' xml:lang='en-US'>"
                + "<voice name='%s'>%s</voice></speak>", voiceName, text);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/ssml+xml")); // Correct content type
        headers.set("Ocp-Apim-Subscription-Key", apiKey);
        headers.set("X-Microsoft-OutputFormat", "riff-8khz-16bit-mono-pcm");

        HttpEntity<String> requestEntity = new HttpEntity<>(ssml, headers);

        byte[] response = restTemplate.exchange(azureEndpoint, HttpMethod.POST, requestEntity, byte[].class).getBody();

        Utilities.writeResponseToFile(response, outputFilePath);
        log.info("Audio content written to file: {}", outputFilePath);

        return outputFilePath;
    }



}
