package com.example.BetaModel.services.iservices;

import com.example.BetaModel.dtos.UserDTO;
import com.example.BetaModel.model.Users;
import com.example.BetaModel.responses.UserResponse;
import jakarta.validation.constraints.Email;

import javax.mail.MessagingException;

public interface IUserService {
    Users createUser(UserDTO userDTO) throws Exception;
    String login(String email, String password) throws Exception;

//    UserDTO forgotPassword(UserDTO userDTO,  String email, String code, String newPassword);
//    UserDTO changePassword(UserDTO userDTO,String password, String newPassword);


    UserResponse getAllUser(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
    UserDTO getUserId(Long userId);

    void deleteUserId(Long userId);

//    void sendConfirmationEmail(String recipientEmail, String confirmationCode) throws MessagingException;

    }
