package com.example.BetaModel.respository;

import com.example.BetaModel.model.BillStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillStatusRepo extends JpaRepository<BillStatus, Long> {
}
