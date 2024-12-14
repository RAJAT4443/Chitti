package com.Chitti.AiVoiceMail.controller;

import com.Chitti.AiVoiceMail.requests.UserRegistrationData;
import com.Chitti.AiVoiceMail.service.userRegistration.UserRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chitti")
@CrossOrigin(origins = "*")
public class UserRegistrationController {

    @Autowired
    UserRegistrationService userRegistrationService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserRegistrationData user) {
        try {
            userRegistrationService.addUser(user);
            return ResponseEntity.ok("User registered successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Registration failed: " + e.getMessage());
        }
    }


}
