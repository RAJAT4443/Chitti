package com.Chitti.AiVoiceMail.controller;

import com.Chitti.AiVoiceMail.requests.UserRegistrationData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chitti")
@CrossOrigin(origins = "*")
public class UserRegistrationController {


    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserRegistrationData user) {
        try {
            userService.registerUser(user);
            return ResponseEntity.ok("User registered successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Registration failed: " + e.getMessage());
        }
    }


}
