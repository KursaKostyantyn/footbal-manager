package com.example.footbalmanager.services;

import com.example.footbalmanager.dao.PlayerDAO;
import com.example.footbalmanager.models.Player;
import com.example.footbalmanager.models.dto.PlayerDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class PlayerService {
    private PlayerDAO playerDAO;

    private PlayerDTO convertToPlayerDTO(Player player) {
        return new PlayerDTO(
                player.getFirstName(),
                player.getLastName(),
                player.getAge(),
                player.getStartDate()
        );

    }

    public ResponseEntity<?> savePlayer(Player player) {
        if (player != null) {
            playerDAO.save(player);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<List<PlayerDTO>> getAllPlayers() {
        return new ResponseEntity<>(playerDAO.findAll()
                .stream()
                .map(this::convertToPlayerDTO)
                .collect(Collectors.toList()),
                HttpStatus.OK);
    }

    public ResponseEntity<PlayerDTO> getPlayerDTOById(int id) {
        PlayerDTO playerDTO = convertToPlayerDTO(playerDAO.findById(id).orElse(new Player()));
        if (playerDTO.getFirstName() != null) {
            return new ResponseEntity<>(playerDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }


    }

    public Player getPlayerById(int id) {
        return playerDAO.findById(id).orElse(new Player());
    }


    public ResponseEntity<PlayerDTO> updatePlayerById(int id, Player player) {
        Player oldPlayer = playerDAO.findById(id).orElse(new Player());
        if (oldPlayer.getFirstName() != null) {
            player.setId(id);
            playerDAO.save(player);
            return new ResponseEntity<>(convertToPlayerDTO(playerDAO.findById(id).orElse(new Player())), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<HttpStatus> deletePlayerById(int id) {
        Player player = playerDAO.findById(id).orElse(new Player());
        if (player.getFirstName() != null) {
            playerDAO.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


}
