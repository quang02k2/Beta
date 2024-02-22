package com.example.BetaModel.services.confirmEmailService;

import com.example.BetaModel.dtos.ConfirmEmailDto;

import javax.mail.MessagingException;

public interface ConfirmEmailService {
    ConfirmEmailDto createConfirmEmailDto(ConfirmEmailDto confirmEmailDto);
    void deleteConfirmEmail( Long confirmEmailId);

//    public void sendConfirmationEmail(String recipientEmail, String confirmationCode) throws MessagingException;

    }
