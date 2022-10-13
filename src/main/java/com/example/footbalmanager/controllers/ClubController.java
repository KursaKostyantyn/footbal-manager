package com.example.footbalmanager.controllers;

import com.example.footbalmanager.models.Club;
import com.example.footbalmanager.models.dto.ClubDTO;
import com.example.footbalmanager.services.ClubService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class ClubController {

    private ClubService clubService;
    @PostMapping("/clubs")
    public void saveClub (@RequestBody Club club){
        clubService.saveClub(club);
    }

    @GetMapping("/clubs")
    public List<ClubDTO> getAllClubs(){
        return clubService.getAllClubs();
    }
}
