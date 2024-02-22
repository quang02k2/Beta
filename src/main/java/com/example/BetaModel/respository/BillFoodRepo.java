package com.example.BetaModel.respository;

import com.example.BetaModel.model.BillFood;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillFoodRepo extends JpaRepository<BillFood, Long> {
}
