package com.example.BetaModel.respository;

import com.example.BetaModel.model.ConfirmEmail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ConfirmEmailRepo extends JpaRepository<ConfirmEmail, Long> {
}
