package com.example.footbalmanager.models;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

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

    @ManyToOne(cascade = CascadeType.ALL)
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
