package com.example.BetaModel.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmEmailDto {
    private LocalDateTime requiredTime;

    private LocalDateTime expiredTime;

    private String confirmCode;

    private boolean isConfirm;

}
