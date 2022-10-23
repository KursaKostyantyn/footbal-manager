package com.example.footbalmanager.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(exclude = "club")
@Entity
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String firstName;
    private String lastName;
    private int age;
    private LocalDate startDate;

    @ManyToOne(cascade = {CascadeType.MERGE,CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinTable(
            name = "club_player",
            joinColumns = @JoinColumn(name = "player_id"),
            inverseJoinColumns = @JoinColumn(name = "club_id")
    )
    private Club club;

    public Player(String firstName, String lastName, int age, String date) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        System.out.println(date);
        this.startDate = LocalDate.parse(date);
    }

    public Player(String firstName, String lastName, int age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }
}
