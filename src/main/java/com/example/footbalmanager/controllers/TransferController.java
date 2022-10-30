package com.example.footbalmanager.controllers;

import com.example.footbalmanager.services.ClubService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/transfer")
public class TransferController {
    private ClubService clubService;

    @PutMapping("")
    public ResponseEntity<String> playerTransfer(@RequestParam int playerId,
                                                 @RequestParam int donorClubId,
                                                 @RequestParam int recipientClubId) {
        return clubService.playerTransfer(playerId, donorClubId, recipientClubId);
    }
}
