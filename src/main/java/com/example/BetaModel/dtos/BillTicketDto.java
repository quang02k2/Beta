package com.example.BetaModel.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BillTicketDto {
    private Long Id;

    private int quantity;

}
