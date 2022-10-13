package com.example.footbalmanager.models.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ClubDTO {
    private String name;
    private int account;
    private String city;
    private String country;

}
