package com.example.footbalmanager.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.footbalmanager.models.dto.CustomUserDTO;
import com.example.footbalmanager.services.CustomUserService;

@RestController
@AllArgsConstructor
public class MainController {

    private CustomUserService customUserService;

    @PostMapping("/register")
    public ResponseEntity<CustomUserDTO> register(@RequestBody CustomUserDTO customUserDTO) {
        return customUserService.save(customUserDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody CustomUserDTO customUserDTO) {
        return customUserService.login(customUserDTO);

    }

    @GetMapping("/activate")
    public ResponseEntity<?> activate (@RequestParam int id){
        return customUserService.activateCustomUser(id);
    }
}
