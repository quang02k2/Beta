package com.example.BetaModel.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "seat")
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private int number;

    private String line;

    private boolean isActive;

    @OneToMany(mappedBy = "seat", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonBackReference
    private Set<Ticket> ticket;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "seatStatusId", foreignKey = @ForeignKey(name = "fk_seat_seatStatus"), nullable = false)
    @JsonManagedReference
    private SeatStatus seatStatus;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "seatTypeId", foreignKey = @ForeignKey(name = "fk_seat_seatType"), nullable = false)
    @JsonManagedReference
    private SeatType seatType;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "roomId", foreignKey = @ForeignKey(name = "fk_seat_room"), nullable = false)
    @JsonManagedReference
    private Room room;

}
