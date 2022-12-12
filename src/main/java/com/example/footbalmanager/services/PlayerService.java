package com.example.footbalmanager.services;


import com.example.footbalmanager.models.Club;
import com.example.footbalmanager.models.CustomUser;
import com.example.footbalmanager.models.dto.ClubDTO;
import com.example.footbalmanager.models.dto.CustomUserDTO;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import com.example.footbalmanager.dao.PlayerDAO;
import com.example.footbalmanager.models.Player;
import com.example.footbalmanager.models.dto.PlayerDTO;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@Service
public class PlayerService {
    private PlayerDAO playerDAO;

    private CustomUserService customUserService;

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
                player.getStartDate().toString(),
                clubWithoutPlayers(player.getClub()),
                player.getPhoto(),
                player.getCreationDate().toString(),
                customUserWithoutPlayers(player.getCustomUser())
        );

    }

    private ClubDTO clubWithoutPlayers(Club club) {
        if (club != null) {
            return new ClubDTO(
                    club.getId(),
                    club.getName(),
                    club.getAccount(),
                    club.getCity(),
                    club.getCountry(),
                    club.getCommission(),
                    club.getPhoto()
            );
        }
        return new ClubDTO();
    }

    private CustomUserDTO customUserWithoutPlayers(CustomUser customUser) {
        if (customUser != null) {
            return new CustomUserDTO(
                    customUser.getLogin(),
                    customUser.getEmail()
            );
        }
        return new CustomUserDTO();
    }


    public ResponseEntity<PlayerDTO> savePlayer(String customUserLogin, Player player) {
        if (player != null) {
            playerDAO.save(player);
            customUserService.addPlayerToCustomUser(customUserLogin, player);
            return new ResponseEntity<>(convertToPlayerDTO(player), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<List<PlayerDTO>> getAllPlayersDTO() {
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
        Player player = playerDAO.findById(id).orElse(new Player());
        if (player.getFirstName() != null) {
            return player;
        } else {
            return null;
        }
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

    public ResponseEntity<PlayerDTO> savePlayerPhoto(MultipartFile photo, int id) {
        String originalFilename = photo.getOriginalFilename();
        File playersPhoto = new File("playersPhoto");

        if (!playersPhoto.exists()) {
            playersPhoto.mkdir();
        }

        String pathToSavePhoto = playersPhoto.getAbsolutePath() + File.separator + originalFilename;

        try {
            photo.transferTo(new File(pathToSavePhoto));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Player player = getPlayerById(id);

        if (player != null) {
            player.setPhoto(originalFilename);
            playerDAO.save(player);
            return new ResponseEntity<>(convertToPlayerDTO(player), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


}
