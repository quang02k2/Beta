package com.example.BetaModel.respository;

import com.example.BetaModel.model.BillTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillTicketRepo extends JpaRepository<BillTicket, Long> {
}
