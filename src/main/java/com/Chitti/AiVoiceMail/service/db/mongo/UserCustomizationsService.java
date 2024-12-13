package com.Chitti.AiVoiceMail.service.db.mongo;

import com.Chitti.AiVoiceMail.models.UserCustomizations;
import com.Chitti.AiVoiceMail.repos.mongo.UserCustomizationsRepository;
import org.springframework.stereotype.Service;

@Service
public class UserCustomizationsService {

    private final UserCustomizationsRepository userCustomizationsRepository;

    public UserCustomizationsService(UserCustomizationsRepository userCustomizationsRepository) {
        this.userCustomizationsRepository = userCustomizationsRepository;
    }

    public UserCustomizations getUserCustomization(String id) {
        return userCustomizationsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User customization not found for ID: " + id));
    }

    public UserCustomizations saveUserCustomization(UserCustomizations userCustomizations) {
        try {
            return userCustomizationsRepository.save(userCustomizations);
        } catch (Exception e) {
            throw new RuntimeException("Error saving user customization: " + e.getMessage(), e);
        }
    }

    public void deleteUserCustomization(String id) {
        if (!userCustomizationsRepository.existsById(id)) {
            throw new IllegalArgumentException("User customization not found for ID: " + id);
        }
        userCustomizationsRepository.deleteById(id);
    }
}