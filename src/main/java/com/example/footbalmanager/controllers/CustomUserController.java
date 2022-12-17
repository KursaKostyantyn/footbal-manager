package com.example.footbalmanager.controllers;

import com.example.footbalmanager.models.CustomUser;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.footbalmanager.models.dto.CustomUserDTO;
import com.example.footbalmanager.services.CustomUserService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
    public ResponseEntity<?> activate(@RequestParam int id) {
        return customUserService.activateCustomUser(id);
    }

    @GetMapping("/resetPassword")
    public ResponseEntity<?> resetPassword(@RequestParam String userLogin) {
        return customUserService.resetPassword(userLogin);
    }

    @PutMapping("/createNewPassword")
    public ResponseEntity<?> createNewPassword(@RequestParam String resetPassword, @RequestBody CustomUser customUser) {
        return customUserService.createNewPassword(resetPassword, customUser);
    }

    @PutMapping("/admin/users/blockUser/{login}")
    public ResponseEntity<?> blockUser(@PathVariable String login, @RequestParam boolean isBlocked) {
        return customUserService.blockUser(login, isBlocked);
    }

    @PostMapping("admin/users/save")
    public ResponseEntity<CustomUserDTO> saveCustomUser(@RequestBody CustomUser customUser) {
        return customUserService.saveWithoutSendingMail(customUser);
    }

    @DeleteMapping("admin/users/delete/{login}")
    public ResponseEntity<CustomUserDTO> deleteCustomUserByLogin (@PathVariable String login){
        return customUserService.deleteCustomUserByLogin(login);
    }

    @PutMapping("admin/users/update")
    public ResponseEntity<CustomUserDTO> updateCustomUser (@RequestBody CustomUser customUser, @RequestParam int id){
        return customUserService.updateCustomUser(customUser,id);
    }

    @GetMapping("admin/users")
    public ResponseEntity<List<CustomUserDTO>> getAllCustomUsers(){
        return customUserService.getAllCustomUsers();
    }

    @PostMapping("admin/users/photo")
    public ResponseEntity<CustomUserDTO> saveUserPhoto (@RequestParam MultipartFile photo, @RequestParam int id){
        return customUserService.saveUserPhoto(photo,id);
    }


}
