package com.example.footbalmanager.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;

import com.example.footbalmanager.constants.Role;

import java.util.List;

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

    @OneToMany(cascade = {CascadeType.MERGE,CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JsonManagedReference(value = "player-customUser")
    @JoinTable(
            name = "customUser_player",
            joinColumns = @JoinColumn(name = "customUser_id"),
            inverseJoinColumns = @JoinColumn(name = "player_id")
    )
    @ToString.Exclude
    private List<Player> players;

    @OneToMany(cascade = {CascadeType.MERGE,CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JsonManagedReference(value = "club-customUser")
    @JoinTable(
            name = "customUser_club",
            joinColumns = @JoinColumn(name = "customUser_id"),
            inverseJoinColumns = @JoinColumn(name = "club_id")
    )
    @ToString.Exclude
    private List<Club> clubs;

    private String resetPassword;
    private long resetPasswordExpiryDate;

    public CustomUser(String login, String password, String email, Role role, boolean isActivated) {
        this.login = login;
        this.password = password;
        this.email = email;
        this.role = role;
        this.isActivated = isActivated;
    }

    public CustomUser(String login, String password, String email) {
        this.login = login;
        this.password = password;
        this.email = email;
    }

    public CustomUser(String login, String email, Role role, boolean isActivated) {
        this.login = login;
        this.email = email;
        this.role = role;
        this.isActivated = isActivated;
    }

    public CustomUser(String login, String email) {
        this.login = login;
        this.email = email;
    }
}
