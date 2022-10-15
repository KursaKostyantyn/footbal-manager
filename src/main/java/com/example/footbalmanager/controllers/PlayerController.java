package com.example.footbalmanager.controllers;

import com.example.footbalmanager.models.Player;
import com.example.footbalmanager.models.dto.PlayerDTO;
import com.example.footbalmanager.services.PlayerService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class PlayerController {
    private PlayerService playerService;

    @PostMapping("/players")
    public ResponseEntity<?> savePlayer(@RequestBody Player player) {
        return playerService.savePlayer(player);
    }

    @GetMapping("/players")
    public ResponseEntity<List<PlayerDTO>> getAllPlayers() {
        return playerService.getAllPlayers();
    }

    @GetMapping("/players/{id}")
    public ResponseEntity<PlayerDTO> getPlayerById(@PathVariable int id) {
        return playerService.getPlayerDTOById(id);
    }

    @PutMapping("players/{id}")
    public ResponseEntity<PlayerDTO> updatePlayerById(@PathVariable int id, @RequestBody Player player) {
        return playerService.updatePlayerById(id, player);
    }

    @DeleteMapping("players/{id}")
    public ResponseEntity<HttpStatus> deletePlayerById(@PathVariable int id) {
         return    playerService.deletePlayerById(id);
    }

}
