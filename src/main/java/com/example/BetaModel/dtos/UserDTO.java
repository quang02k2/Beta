package com.example.BetaModel.dtos;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@Getter
@Setter
public class UserDTO {
    private Long Id;

    private int point;

    private String userName;

    private String email;

    private String name;

    private int phoneNumber;

    private String password;

    private boolean isActive;



}
