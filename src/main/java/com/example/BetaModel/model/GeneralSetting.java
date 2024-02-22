package com.example.BetaModel.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "generalSetting")
public class GeneralSetting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private Timestamp breakTime;

    private int businessHours;

    private Timestamp closeTime;

    private double fixedTicketPrice;

    private int percentDay;

    private int percentWeekend;

    private Timestamp timeBeginToChange;
}
