package com.example.BetaModel.respository;

import com.example.BetaModel.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepo extends JpaRepository<Users, Long> {
    boolean existsByEmail(String email);
    Optional<Users> findByEmail(String email);
//
    Boolean existsByName(String username);

    Optional<Users> findByName(String username);


    Users findOneByEmailAndPassword(String email, String password);
}
