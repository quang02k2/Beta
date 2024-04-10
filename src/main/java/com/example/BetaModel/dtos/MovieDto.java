package com.example.BetaModel.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieDto {
    private int movieDuration;

    private Timestamp endTime;

    private Timestamp premiereDate;

    private String description;

    private String director;

    private String image;

    private String heroImage;

    private String language;

    private String name;

    private String trailer;

    private boolean isActive;
}
