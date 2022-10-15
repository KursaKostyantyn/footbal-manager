package com.example.footbalmanager.services;

import com.example.footbalmanager.dao.ClubDAO;
import com.example.footbalmanager.models.Club;
import com.example.footbalmanager.models.Player;
import com.example.footbalmanager.models.dto.ClubDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class ClubService {
    private ClubDAO clubDAO;
    private PlayerService playerService;

    private ClubDTO convertToDTO(Club club) {
        return new ClubDTO(
                club.getName(),
                club.getAccount(),
                club.getCity(),
                club.getCountry()
        );
    }

    public ResponseEntity<HttpStatus> saveClub(Club club) {
        if (club != null) {
            clubDAO.save(club);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<List<ClubDTO>> getAllClubs() {
        return new ResponseEntity<>(clubDAO.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList()),
                HttpStatus.OK);
    }

    public ResponseEntity<ClubDTO> getCLubDTOByID(int id) {
        ClubDTO clubDTO = convertToDTO(clubDAO.findById(id).orElse(new Club()));
        if (clubDTO.getName() != null) {
            return new ResponseEntity<>(clubDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    public Club getCLubByID(int id) {
        return clubDAO.findById(id).orElse(new Club());
    }

    public ResponseEntity<ClubDTO> updateClubById(int id, Club club) {
        Club oldClub = clubDAO.findById(id).orElse(new Club());
        if (oldClub.getName() != null) {
            club.setId(id);
            clubDAO.save(club);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<HttpStatus> deleteClubById(int id) {
        Club club = clubDAO.findById(id).orElse(new Club());
        if (club.getName() != null) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    public ResponseEntity<HttpStatus> addPlayerToClubById(int playerId, int clubId) {
        Club club = clubDAO.findById(clubId).orElse(new Club());
        Player player = playerService.getPlayerById(playerId);
        if (club.getName() != null && player.getFirstName() != null) {
            club.getPlayers().add(player);
            clubDAO.save(club);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<String> playerTransfer(int playerId, int donorClubId, int recipientClubId) {

        if (donorClubId == recipientClubId) {
            return new ResponseEntity<>("it is the same club", HttpStatus.BAD_REQUEST);
        }

        Club donorClub = getCLubByID(donorClubId);
        Club recipientClub = getCLubByID(recipientClubId);

        Player player = donorClub
                .getPlayers()
                .stream()
                .filter(p -> p.getId() == playerId)
                .findFirst()
                .orElse(new Player());
        if (player.getFirstName() != null) {
            LocalDate todayDate = LocalDate.now();
            int month = (int) ChronoUnit.MONTHS.between(player.getStartDate(), todayDate);
            int sumOfTransfer = month * 100000 / player.getAge()
                    + month * 100000 / player.getAge() * recipientClub.getCommission() / 100;
            donorClub.setAccount(donorClub.getAccount() + sumOfTransfer);
            donorClub.getPlayers().remove(player);
            recipientClub.setAccount(recipientClub.getAccount() - sumOfTransfer);
            recipientClub.getPlayers().add(player);
            clubDAO.save(donorClub);
            clubDAO.save(recipientClub);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No such player in this club", HttpStatus.BAD_REQUEST);
        }
    }

}
