package com.Chitti.AiVoiceMail.service.db.mysql;

import com.Chitti.AiVoiceMail.entities.UserDetails;
import com.Chitti.AiVoiceMail.repos.mysql.UserDetailsRepo;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsService {

    private final UserDetailsRepo userDetailsRepository;

    public UserDetailsService(UserDetailsRepo userDetailsRepository) {
        this.userDetailsRepository = userDetailsRepository;
    }

    public UserDetails getUserDetailsById(Long id) {
        return userDetailsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User details not found for ID: " + id));
    }

    public UserDetails saveUserDetails(UserDetails userDetails) {
        try {
            return userDetailsRepository.save(userDetails);
        } catch (Exception e) {
            throw new RuntimeException("Error saving user details: " + e.getMessage(), e);
        }
    }

    public void deleteUserDetails(Long id) {
        if (!userDetailsRepository.existsById(id)) {
            throw new IllegalArgumentException("User details not found for ID: " + id);
        }
        userDetailsRepository.deleteById(id);
    }
}
