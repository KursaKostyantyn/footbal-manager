package com.example.footbalmanager.models.dto;

import com.example.footbalmanager.constants.Role;
import com.example.footbalmanager.models.Club;
import com.example.footbalmanager.models.Player;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CustomUserDTO {

    private int id;
    private String login;
    private String password;
    private String email;
    private Role role = Role.ROLE_ADMIN;
    private boolean isActivated = false;
    private boolean isBlocked = false;
    private List<Player> players;
    private List<Club> clubs;
    private String photo;

}
