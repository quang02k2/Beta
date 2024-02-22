package com.example.BetaModel.respository;

import com.example.BetaModel.model.Cinema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CinemaRepo extends JpaRepository<Cinema, Long> {
}
