package com.example.BetaModel.services.iservices;

import com.example.BetaModel.exceptions.TokenRefreshException;
import com.example.BetaModel.model.RefreshTokens;
import com.example.BetaModel.model.Users;
import com.example.BetaModel.respository.RefreshTokensRepo;
import com.example.BetaModel.respository.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {
  @Value("${bezkoder.app.jwtRefreshExpirationMs}")
  private Long refreshTokenDurationMs;

  @Autowired
  private RefreshTokensRepo refreshTokenRepository;

  @Autowired
  private UsersRepo userRepository;

  public Optional<RefreshTokens> findByToken(String token) {
    return refreshTokenRepository.findByToken(token);
  }

  public RefreshTokens createRefreshToken(Long userId) {
    RefreshTokens refreshToken = new RefreshTokens();

    refreshToken.setUsers(userRepository.findById(userId).get());
    refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
    refreshToken.setToken(UUID.randomUUID().toString());

    refreshToken = refreshTokenRepository.save(refreshToken);
    return refreshToken;
  }



  public RefreshTokens verifyExpiration(RefreshTokens token) {
    if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
      refreshTokenRepository.delete(token);
      throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new signin request");
    }

    return token;
  }


  public RefreshTokens generateRefreshToken(Users user) {
    String token = UUID.randomUUID().toString();
    Instant expirationTime = Instant.now().plusSeconds(30 * 24 * 60 * 60);

    RefreshTokens refreshToken = new RefreshTokens();
    refreshToken.setToken(token);
    refreshToken.setExpiryDate(expirationTime);
    refreshToken.setUsers(user);

    return refreshTokenRepository.save(refreshToken);
  }

//  @Transactional
//  public RefreshTokens deleteByUserId(Long userId) {
//    return refreshTokenRepository.deleteByUser(userRepository.findById(userId).get());
//  }
}
