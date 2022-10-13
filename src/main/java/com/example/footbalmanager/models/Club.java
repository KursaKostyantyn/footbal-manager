package com.example.footbalmanager.models;

import lombok.*;

import javax.persistence.*;
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

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "club_player",
            joinColumns =@JoinColumn(name ="club_id" ) ,
            inverseJoinColumns = @JoinColumn(name ="player_id" )
    )
    private List<Player> players;

    public Club(String name, int account, String city, String country) {
        this.name = name;
        this.account = account;
        this.city = city;
        this.country = country;
    }

}
