package com.example.BetaModel.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieTypeDto {
    private Long Id;

    private String movieTypeName;

    private boolean isActive;
}
