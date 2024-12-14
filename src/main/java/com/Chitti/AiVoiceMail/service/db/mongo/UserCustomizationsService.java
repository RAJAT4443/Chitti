package com.Chitti.AiVoiceMail.service.db.mongo;

import com.Chitti.AiVoiceMail.models.UserCustomizations;
import com.Chitti.AiVoiceMail.repos.mongo.UserCustomizationsRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserCustomizationsService {

    private final UserCustomizationsRepository userCustomizationsRepository;

    public UserCustomizationsService(UserCustomizationsRepository userCustomizationsRepository) {
        this.userCustomizationsRepository = userCustomizationsRepository;
    }

    @Cacheable(value = "userCustomizations", key = "'user:' + #id")
    public UserCustomizations getUserCustomization(String id) {
        return userCustomizationsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User customization not found for ID: " + id));
    }

    @CacheEvict(value = "userCustomizations", key = "'user:' + #userCustomizations.id")
    public UserCustomizations saveUserCustomization(UserCustomizations userCustomizations) {
        try {
            return userCustomizationsRepository.save(userCustomizations);
        } catch (Exception e) {
            throw new RuntimeException("Error saving user customization: " + e.getMessage(), e);
        }
    }

    @CacheEvict(value = "userCustomizations", key = "'user:' + #id")
    public void deleteUserCustomization(String id) {
        if (!userCustomizationsRepository.existsById(id)) {
            throw new IllegalArgumentException("User customization not found for ID: " + id);
        }
        userCustomizationsRepository.deleteById(id);
    }

    public UserCustomizations getUserCustomizationsByUserId(String userId) {
        return Optional.of(userCustomizationsRepository.findUserCustomizationsByUserId(userId)).orElseThrow(() -> new IllegalArgumentException("User customization not found for user ID: " + userId));
    }
}