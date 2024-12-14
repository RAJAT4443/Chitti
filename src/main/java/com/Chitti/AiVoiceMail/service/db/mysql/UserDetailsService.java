package com.Chitti.AiVoiceMail.service.db.mysql;

import com.Chitti.AiVoiceMail.entities.UserDetails;
import com.Chitti.AiVoiceMail.repos.mysql.UserDetailsRepo;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsService {

    private final UserDetailsRepo userDetailsRepository;

    public UserDetailsService(UserDetailsRepo userDetailsRepository) {
        this.userDetailsRepository = userDetailsRepository;
    }

    @Cacheable(value = "userDetails", key = "'userDetails:' + #id")
    public UserDetails getUserDetailsById(Long id) {
        return userDetailsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User details not found for ID: " + id));
    }

    @CacheEvict(value = "userDetails", key = "'userDetails:' + #userDetails.id")
    public UserDetails saveUserDetails(UserDetails userDetails) {
        try {
            return userDetailsRepository.save(userDetails);
        } catch (Exception e) {
            throw new RuntimeException("Error saving user details: " + e.getMessage(), e);
        }
    }

    @CacheEvict(value = "userDetails", key = "'userDetails:' + #id")
    public void deleteUserDetails(Long id) {
        if (!userDetailsRepository.existsById(id)) {
            throw new IllegalArgumentException("User details not found for ID: " + id);
        }
        userDetailsRepository.deleteById(id);
    }

    public UserDetails getUserDetailsByPhone(String bParty) {
        return Optional.of(userDetailsRepository.findUserDetailsByPhone(bParty))
                .orElseThrow(() -> new IllegalArgumentException("User details not found for phone: " + bParty));
    }
}
