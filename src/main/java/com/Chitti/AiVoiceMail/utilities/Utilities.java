package com.Chitti.AiVoiceMail.utilities;

import com.Chitti.AiVoiceMail.entities.UserDetails;
import com.Chitti.AiVoiceMail.requests.UserRegistrationData;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.lang.reflect.Field;

@Slf4j
public class Utilities {

    public static String getOutputFilePath(String localFilePath, String fileName) {
        System.out.println("User home: " + System.getProperty("user.home"));
        return System.getProperty("user.home") + localFilePath + fileName + ".wav";
    }


    /**
     * Estimates the duration of the audio file based on its size and format.
     *
     * @param sizeInBytes The size of the audio file in bytes.
     * @param format      The audio format.
     * @return The estimated duration in seconds.
     */
    public static int estimateDuration(long sizeInBytes, String format) {
        if (format.toLowerCase().contains("16khz")) {
            return (int) (sizeInBytes / 32000); // Approximation
        }
        return 0; // Default or unsupported format
    }

    public static void ensureDirectoryExists(String path) {
        File directory = new File(path);
        if (!directory.exists() && directory.mkdirs()) {
//            log.info("Created directory: {}", path);
        }
    }

    // Write the response byte array to the output file
    public static void writeResponseToFile(byte[] response, String outputFilePath) throws IOException {
        File outputFile = new File(outputFilePath);
        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            fos.write(Optional.ofNullable(response).orElseThrow(() -> new IOException("Response is null")));
        } catch (IOException e) {
//            log.error("Error writing audio content to file: {}", e.getMessage(), e);
            e.printStackTrace(); // Rethrow the exception after logging
        }
    }


    public static UserDetails mapUserRegistrationDataToUserDetails(UserRegistrationData userRegistrationData) {

        String randomString= UUID.randomUUID().toString();

        UserDetails userDetails = new UserDetails();
        userDetails.setEmail(userRegistrationData.getEmail());
        userDetails.setUserId(randomString);
        userDetails.setName(userRegistrationData.getUsername());
        userDetails.setPhone(userRegistrationData.getPhoneNumber());
        return userDetails;

//        return UserDetails.builder()
//                .userId(userRegistrationData.getUserId())
//                .phone(userRegistrationData.getPhoneNumber())
//                .email(userRegistrationData.getEmail())
//                .name(userRegistrationData.getUsername()).build();


    }


    public static Map<String, String> extractFields(Object object) {
        Map<String, String> fieldMap = new HashMap<>();
        try {
            if(object==null){
                return fieldMap;
            }
            Class<?> clazz = object.getClass();
            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                Object value = field.get(object);
                if (value != null) {
                    fieldMap.put(field.getName(), value.toString());
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Error accessing fields of object", e);
        }
        return fieldMap;
    }

    public static String replacePlaceholders(String template, Map<String, String> placeholderValues) {
        String processedTemplate = template;
        for (Map.Entry<String, String> entry : placeholderValues.entrySet()) {
            String placeholder = "{{" + entry.getKey() + "}}";
            processedTemplate = processedTemplate.replace(placeholder, entry.getValue());
        }
        return processedTemplate;
    }

    /**
     * Generates a unique session ID using the current timestamp and a random UUID.
     *
     * @return A unique session ID string.
     */
    public static String generateSessionId() {
        // Get the current timestamp in milliseconds
        long timestamp = System.currentTimeMillis();

        // Generate a random UUID
        String randomUUID = UUID.randomUUID().toString();

        // Combine timestamp and UUID for uniqueness
        return "session_" + timestamp + "_" + randomUUID;
    }

}
