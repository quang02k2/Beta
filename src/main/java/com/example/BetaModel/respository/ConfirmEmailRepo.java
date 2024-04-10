package com.example.BetaModel.respository;

import com.example.BetaModel.dtos.UserDTO;
import com.example.BetaModel.model.ConfirmEmail;
import com.example.BetaModel.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ConfirmEmailRepo extends JpaRepository<ConfirmEmail, Long> {
    ConfirmEmail findByConfirmCode(String confirmCode);

    ConfirmEmail findByUsersAndConfirmCode(Users users, String code);

    ConfirmEmail findByUsers(Users users);



}
