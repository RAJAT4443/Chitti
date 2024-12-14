package com.Chitti.AiVoiceMail.utilities;

import com.Chitti.AiVoiceMail.entities.UserDetails;
import com.Chitti.AiVoiceMail.requests.UserRegistrationData;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Slf4j
public class Utilities {

    public static String getOutputFilePath(String localFilePath, String fileName) {
        return System.getProperty("user.home") + localFilePath + fileName + ".wav";
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
}
