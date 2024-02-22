package com.example.BetaModel.respository;

import com.example.BetaModel.model.RefreshTokens;
import com.example.BetaModel.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface RefreshTokensRepo extends JpaRepository<RefreshTokens, Long> {
//    List<RefreshTokens> findByUsers(Users user);
Optional<RefreshTokens> findByToken(String token);
//    RefreshTokens findByRefreshToken(String token);

//    @Modifying
//    RefreshTokens deleteByUser(Users user);

}
