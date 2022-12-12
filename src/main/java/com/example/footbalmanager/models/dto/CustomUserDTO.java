package com.example.footbalmanager.models.dto;

import com.example.footbalmanager.constants.Role;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CustomUserDTO {

    private String login;
    private String email;

    public CustomUserDTO(String login) {
        this.login = login;
    }

  }
