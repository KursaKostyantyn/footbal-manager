package com.example.footbalmanager.models.dto;

import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PlayerDTO {

    private String firstName;
    private String lastName;
    private int age;
    private LocalDate startDate;

}
