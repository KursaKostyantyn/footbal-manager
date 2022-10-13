package com.example.footbalmanager.controllers;

import com.example.footbalmanager.models.Player;
import com.example.footbalmanager.services.PlayerService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class PlayerController {
    private PlayerService playerService;

    @PostMapping("/players")
    public void savePlayer(@RequestBody Player player){
        System.out.println(player);
        playerService.savePlayer(player);
    }
}
