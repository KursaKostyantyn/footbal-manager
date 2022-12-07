package com.example.footbalmanager.models;

import lombok.*;

import javax.persistence.*;

import com.example.footbalmanager.constants.Role;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class CustomUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true)
    private String login;

    private String password;

    private String email;

    private Role role = Role.ROLE_ADMIN;

    private boolean isActivated = false;

    public CustomUser(String login, String password, String email) {
        this.login = login;
        this.password = password;
        this.email = email;
    }
}
