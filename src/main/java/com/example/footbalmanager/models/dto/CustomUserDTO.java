package com.example.footbalmanager.models.dto;

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

    public CustomUserDTO(String login, String password) {
        this.login = login;
        this.password = password;
    }
}
