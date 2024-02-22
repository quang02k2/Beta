package com.example.BetaModel.respository;

import com.example.BetaModel.model.RankCustomers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RankCustomersRepo extends JpaRepository<RankCustomers, Long> {
}
