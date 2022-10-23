package com.example.footbalmanager.services;


import com.example.footbalmanager.models.Club;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import com.example.footbalmanager.dao.PlayerDAO;
import com.example.footbalmanager.models.Player;
import com.example.footbalmanager.models.dto.PlayerDTO;

@AllArgsConstructor
@Service
public class PlayerService {
    private PlayerDAO playerDAO;

    @Bean
    private void autoCompletePlayers() {
        if (playerDAO.findAll().size() == 0) {
            for (int i = 0; i < 10; i++) {
                playerDAO.save(new Player("Vasya" + i, "Koval" + i, 18 + i, "2017-09-1" + i));
            }
        }
    }


    private PlayerDTO convertToPlayerDTO(Player player) {
        return new PlayerDTO(
                player.getId(),
                player.getFirstName(),
                player.getLastName(),
                player.getAge(),
                player.getStartDate(),
                clubWithoutPlayers(player.getClub())
        );

    }

    private Club clubWithoutPlayers(Club club) {
        if (club != null) {
            return new Club(
                    club.getId(),
                    club.getName(),
                    club.getAccount(),
                    club.getCity(),
                    club.getCountry(),
                    club.getCommission()
            );
        }
        return new Club();
    }


    public ResponseEntity<PlayerDTO> savePlayer(Player player) {
        if (player != null) {
            playerDAO.save(player);
            PlayerDTO playerDTO = convertToPlayerDTO(player);
            return new ResponseEntity<>(playerDTO, HttpStatus.CREATED);
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

    public ResponseEntity<PlayerDTO> deletePlayerById(int id) {
        Player player = playerDAO.findById(id).orElse(new Player());
        if (player.getFirstName() != null) {
            playerDAO.deleteById(id);
            PlayerDTO playerDTO = convertToPlayerDTO(player);
            return new ResponseEntity<>(playerDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


}
