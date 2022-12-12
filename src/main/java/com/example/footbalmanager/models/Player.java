package com.example.footbalmanager.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String firstName;
    private String lastName;
    private int age;
    private LocalDate startDate;
    private LocalDate creationDate= LocalDate.now();

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinTable(
            name = "club_player",
            joinColumns = @JoinColumn(name = "player_id"),
            inverseJoinColumns = @JoinColumn(name = "club_id")
    )
    @ToString.Exclude
    private Club club;
    private String photo = "noPhoto.png";

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JsonBackReference(value = "player-customUser")
    @JoinTable(
            name = "customUser_player",
            joinColumns = @JoinColumn(name = "player_id"),
            inverseJoinColumns = @JoinColumn(name = "customUser_id")
    )
    @ToString.Exclude
    private CustomUser customUser;


    public Player(String firstName, String lastName, int age, String date) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.startDate = LocalDate.parse(date);
    }

    public Player(String firstName, String lastName, int age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }

}
