package com.example.BetaModel.respository;

import com.example.BetaModel.model.SeatStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeatStatusRepo extends JpaRepository<SeatStatus, Long> {
}
