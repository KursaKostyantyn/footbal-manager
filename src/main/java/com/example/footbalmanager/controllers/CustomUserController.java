package com.example.footbalmanager.controllers;

import com.example.footbalmanager.models.CustomUser;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.footbalmanager.models.dto.CustomUserDTO;
import com.example.footbalmanager.services.CustomUserService;

@RestController
@AllArgsConstructor
public class CustomUserController {

    private CustomUserService customUserService;

    @PostMapping("/register")
    public ResponseEntity<CustomUserDTO> register(@RequestBody CustomUser customUser) {
        return customUserService.save(customUser);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody CustomUser customUser) {
        return customUserService.login(customUser);

    }

    @GetMapping("/activate")
    public ResponseEntity<?> activate (@RequestParam int id){
        return customUserService.activateCustomUser(id);
    }

    @GetMapping("/resetPassword")
    public ResponseEntity<?> resetPassword (@RequestParam String userLogin){
        return customUserService.resetPassword(userLogin);
    }

    @PutMapping("/createNewPassword")
    public ResponseEntity<?> createNewPassword (@RequestParam String resetPassword,@RequestBody CustomUser customUser){
return customUserService.createNewPassword(resetPassword,customUser);
    }

}
