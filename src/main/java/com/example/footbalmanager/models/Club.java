package com.example.footbalmanager.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class Club {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private int account;
    private String city;
    private String country;
    private int commission;

    @OneToMany(cascade = {CascadeType.MERGE,CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JsonManagedReference
    @JoinTable(
            name = "club_player",
            joinColumns = @JoinColumn(name = "club_id"),
            inverseJoinColumns = @JoinColumn(name = "player_id")
    )

    @ToString.Exclude
    private List<Player> players;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JsonBackReference(value = "club-customUser")
    @JoinTable(
            name = "customUser_club",
            joinColumns = @JoinColumn(name = "club_id"),
            inverseJoinColumns = @JoinColumn(name = "customUser_id")
    )
    @ToString.Exclude
    private CustomUser customUser;

    private String photo="noLogo.png";
    private LocalDate creationDate = LocalDate.now();

    public Club(String name, int account, String city, String country, int commission) {
        this.name = name;
        this.account = account;
        this.city = city;
        this.country = country;
        this.commission = commission;
    }

    public Club(int id, String name, int account, String city, String country, int commission) {
        this.id = id;
        this.name = name;
        this.account = account;
        this.city = city;
        this.country = country;
        this.commission = commission;
    }

    public Club(String name, int account, String city, String country, int commission, List<Player> players, String photo) {
        this.name = name;
        this.account = account;
        this.city = city;
        this.country = country;
        this.commission = commission;
        this.players = players;
        this.photo = photo;
    }
}
