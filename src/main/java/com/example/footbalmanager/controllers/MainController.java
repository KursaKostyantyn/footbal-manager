package com.example.footbalmanager.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.footbalmanager.models.dto.CustomUserDTO;
import com.example.footbalmanager.services.CustomUserService;

@RestController
@AllArgsConstructor
public class MainController {

    private CustomUserService customUserService;


    @GetMapping("/")
    public String hello() {
        return "hello";
    }

    @PostMapping("/register")
    public ResponseEntity<CustomUserDTO> register(@RequestBody CustomUserDTO customUserDTO) {
      return   customUserService.save(customUserDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody CustomUserDTO customUserDTO) {
        return customUserService.login(customUserDTO);

    }
}
