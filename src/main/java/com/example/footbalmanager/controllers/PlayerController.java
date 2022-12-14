package com.example.footbalmanager.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.example.footbalmanager.models.Player;
import com.example.footbalmanager.models.dto.PlayerDTO;
import com.example.footbalmanager.services.PlayerService;
import org.springframework.web.multipart.MultipartFile;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/players")
public class PlayerController {
    private PlayerService playerService;

    @PostMapping("")
    public ResponseEntity<PlayerDTO> savePlayer(@RequestParam String customUserLogin, @RequestBody Player player) {
        return playerService.savePlayer(customUserLogin, player);
    }

    @GetMapping("")
    public ResponseEntity<List<PlayerDTO>> getAllPlayers() {
        return playerService.getAllPlayersDTO();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlayerDTO> getPlayerById(@PathVariable int id) {
        return playerService.getPlayerDTOById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlayerDTO> updatePlayerById(@PathVariable int id, @RequestBody Player player) {
        return playerService.updatePlayerById(id, player);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PlayerDTO> deletePlayerById(@PathVariable int id) {
        return playerService.deletePlayerById(id);
    }

    @PostMapping("/photo")
    public ResponseEntity<PlayerDTO> savePlayerPhoto(@RequestParam MultipartFile photo, @RequestParam int id) {
       return playerService.savePlayerPhoto(photo, id);
    }

}
