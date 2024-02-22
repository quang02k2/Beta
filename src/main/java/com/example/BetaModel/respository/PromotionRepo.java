package com.example.BetaModel.respository;

import com.example.BetaModel.model.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PromotionRepo extends JpaRepository<Promotion, Long> {
}
