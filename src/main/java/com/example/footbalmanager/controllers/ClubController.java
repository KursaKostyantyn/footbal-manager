package com.example.footbalmanager.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.example.footbalmanager.models.Club;
import com.example.footbalmanager.models.dto.ClubDTO;
import com.example.footbalmanager.services.ClubService;

@RestController
@AllArgsConstructor
@RequestMapping("/clubs")
public class ClubController {

    private ClubService clubService;

    @PostMapping("")
    public ResponseEntity<ClubDTO> saveClub(@RequestBody Club club) {
        return clubService.saveClub(club);
    }

    @GetMapping("")
    public ResponseEntity<List<ClubDTO>> getAllClubs() {
        return clubService.getAllClubs();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClubDTO> getClubById(@PathVariable int id) {
        return clubService.getCLubDTOByID(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClubDTO> updateClubById(@PathVariable int id, @RequestBody Club club) {
        return clubService.updateClubById(id, club);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ClubDTO> deleteClubById(@PathVariable int id) {
        return clubService.deleteClubById(id);
    }

    @PutMapping("/{id}/add")
    public ResponseEntity<ClubDTO> addPlayerToClubById(@PathVariable(name = "id") int clubId, @RequestParam int playerId) {
        return clubService.addPlayerToClubById(playerId, clubId);

    }




}
