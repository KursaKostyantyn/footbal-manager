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
    private String photo;
    private CustomUserDTO customUserDTO;

    public ClubDTO(int id, String name, int account, String city, String country, int commission, String photo) {
        this.id = id;
        this.name = name;
        this.account = account;
        this.city = city;
        this.country = country;
        this.commission = commission;
        this.photo = photo;
    }

    public ClubDTO(String name, int account, String city, String country, int commission, List<Player> players, String photo, CustomUserDTO customUserDTO) {
        this.name = name;
        this.account = account;
        this.city = city;
        this.country = country;
        this.commission = commission;
        this.players = players;
        this.photo = photo;
        this.customUserDTO = customUserDTO;
    }
}
