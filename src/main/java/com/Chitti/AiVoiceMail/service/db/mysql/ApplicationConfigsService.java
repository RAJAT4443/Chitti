package com.Chitti.AiVoiceMail.service.db.mysql;

import com.Chitti.AiVoiceMail.entities.ApplicationConfig;
import com.Chitti.AiVoiceMail.repos.mysql.ApplicationConfigRepo;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicationConfigsService {
    private final ApplicationConfigRepo applicationConfigsRepository;

    public ApplicationConfigsService(ApplicationConfigRepo applicationConfigsRepository) {
        this.applicationConfigsRepository = applicationConfigsRepository;
    }

    @Cacheable(value = "applicationConfigs", key = "'all'")
    public List<ApplicationConfig> getAllConfigs() {
        try {
            return applicationConfigsRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error fetching application configs: " + e.getMessage(), e);
        }
    }

    @CacheEvict(value = "applicationConfigs", key = "'all'")
    public ApplicationConfig saveConfig(ApplicationConfig config) {
        try {
            return applicationConfigsRepository.save(config);
        } catch (Exception e) {
            throw new RuntimeException("Error saving config: " + e.getMessage(), e);
        }
    }

    @CacheEvict(value = "applicationConfigs", key = "'all'")
    public void deleteConfig(Long id) {
        if (!applicationConfigsRepository.existsById(id)) {
            throw new IllegalArgumentException("Application config not found for ID: " + id);
        }
        applicationConfigsRepository.deleteById(id);
    }
}
