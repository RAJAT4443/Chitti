package com.Chitti.AiVoiceMail;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@SpringBootApplication
public class ChittiVoiceMailApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChittiVoiceMailApplication.class, args);
    }


    @Bean
    public RestTemplate getRestTemplate() {
        RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());

        // Add necessary message converters for multipart support
        restTemplate.setMessageConverters(Arrays.asList(
                new MappingJackson2HttpMessageConverter(), // Handles application/json
                new FormHttpMessageConverter(), // Handles multipart/form-data
                new StringHttpMessageConverter() // Handles plain text
        ));

        return restTemplate;
    }


}
