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
    private String password;
    private String email;
    private Role role;

    public CustomUserDTO(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public CustomUserDTO(String login, String password, String email) {
        this.login = login;
        this.password = password;
        this.email = email;
    }

    public CustomUserDTO(String login, String password, Role role) {
        this.login = login;
        this.password = password;
        this.role = role;
    }
}
