package com.example.BetaModel.respository;

import com.example.BetaModel.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MovieRepo extends JpaRepository<Movie, Long> {
}
