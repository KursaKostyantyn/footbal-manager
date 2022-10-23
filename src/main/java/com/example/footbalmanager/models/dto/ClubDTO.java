package com.example.footbalmanager.models.dto;

import com.example.footbalmanager.models.Player;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ClubDTO {
    private int id;
    private String name;
    private int account;
    private String city;
    private String country;
    private int commission;
    private List<Player> players;

}
