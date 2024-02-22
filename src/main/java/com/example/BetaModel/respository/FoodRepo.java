package com.example.BetaModel.respository;

import com.example.BetaModel.model.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface FoodRepo extends JpaRepository<Food, Long> {
}
