package com.example.BetaModel.services.iservices;

import com.example.BetaModel.dtos.UserDTO;
import com.example.BetaModel.model.Users;
import com.example.BetaModel.responses.UserResponse;

import javax.mail.MessagingException;

public interface IUserService {
    Users createUser(UserDTO userDTO) throws Exception;
    String login(String email, String password) throws Exception;

    UserResponse getAllUser(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
    UserDTO getUserId(Long userId);

    void deleteUserId(Long userId);

    void sendConfirmationEmail(String recipientEmail, String confirmationCode, UserDTO userDTO) throws MessagingException;

    }
