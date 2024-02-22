package com.example.BetaModel.model;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "promotion")
public class Promotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private int percent;

    private int quantity;

    private String type;

    private LocalDate startTime;

    private LocalDate endTime;

    private String description;

    private String name;

    private boolean isActive;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "userId", foreignKey = @ForeignKey(name = "fk_promotion_users"), nullable = false)
    @JsonManagedReference
    private Users users;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "billId", foreignKey = @ForeignKey(name = "fk_promotion_bill"), nullable = false)
    @JsonManagedReference
    private Bill bill;
}
