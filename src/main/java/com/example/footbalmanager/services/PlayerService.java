package com.example.footbalmanager.services;

import com.example.footbalmanager.dao.PlayerDAO;
import com.example.footbalmanager.models.Player;
import com.example.footbalmanager.models.dto.PlayerDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class PlayerService {
    private PlayerDAO playerDAO;

    public void savePlayer(Player player) {
        if (player != null) {
            playerDAO.save(player);
        } else {
            System.out.println("player is null");
        }
    }
}
