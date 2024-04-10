package com.example.BetaModel.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CinemaDto {
    private String address;

    private String description;

    private String code;

    private String nameOfCinema;

    private boolean isActive;
}
