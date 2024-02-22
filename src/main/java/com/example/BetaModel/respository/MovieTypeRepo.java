package com.example.BetaModel.respository;

import com.example.BetaModel.model.MovieType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MovieTypeRepo extends JpaRepository<MovieType, Long> {
}
