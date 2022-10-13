package com.example.footbalmanager.services;

import com.example.footbalmanager.dao.ClubDAO;
import com.example.footbalmanager.models.Club;
import com.example.footbalmanager.models.dto.ClubDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class ClubService {
    private ClubDAO clubDAO;

    public void saveClub(Club club) {
        if (club != null) {
            clubDAO.save(club);
        } else {
            System.out.println("club is null");
        }
    }

    public List<ClubDTO> getAllClubs() {
        return clubDAO.findAll()
                .stream()
                .map(club -> convertToDTO(club))
                .collect(Collectors.toList());
    }

    private ClubDTO convertToDTO(Club club) {
        ClubDTO clubDTO = new ClubDTO(
                club.getName(),
                club.getAccount(),
                club.getCity(),
                club.getCountry()
        );
        return clubDTO;
    }
}
