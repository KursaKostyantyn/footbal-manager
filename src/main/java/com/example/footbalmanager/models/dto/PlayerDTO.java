package com.example.footbalmanager.models.dto;

import com.example.footbalmanager.models.Club;
import lombok.*;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = "club")
public class PlayerDTO {
    private int id;
    private String firstName;
    private String lastName;
    private int age;
    private LocalDate startDate;
    private Club club;
    private String photo;
}
