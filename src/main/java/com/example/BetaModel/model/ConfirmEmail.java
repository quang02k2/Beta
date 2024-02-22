package com.example.BetaModel.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "confirmEmail")
public class ConfirmEmail {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long Id;

    private LocalDateTime requiredTime;

    private LocalDateTime expiredTime;

    private String confirmCode;

    private boolean isConfirm;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "userId", foreignKey = @ForeignKey(name = "fk_confirmEmail_users"), nullable = false)
    @JsonManagedReference
    private Users users;


}
