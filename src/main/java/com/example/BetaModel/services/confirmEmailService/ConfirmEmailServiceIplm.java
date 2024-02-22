package com.example.BetaModel.services.confirmEmailService;

import com.example.BetaModel.dtos.ConfirmEmailDto;
import com.example.BetaModel.model.ConfirmEmail;
import com.example.BetaModel.respository.ConfirmEmailRepo;
import com.example.BetaModel.services.implement.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.time.LocalDateTime;

@Service
public class ConfirmEmailServiceIplm implements ConfirmEmailService{

    @Autowired
    private UserService userService;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private ConfirmEmailRepo confirmEmailRepo;

//    @Override
//    public void sendConfirmationEmail(String recipientEmail, String confirmationCode) throws MessagingException {
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setFrom("ongbaanhyeu4@gmail.com");
//        message.setTo(recipientEmail);
//        message.setSubject("Confirmation Code");
//        message.setText("Your confirmation code is: " + confirmationCode);
//        // Send the email
//        javaMailSender.send(message);
//
//        // Save the confirmation email in the database
//        ConfirmEmail confirmEmail = new ConfirmEmail();
//        confirmEmail.setRequiredTime(LocalDateTime.now());
//        confirmEmail.setExpiredTime(LocalDateTime.now().plusMinutes(5)); // Set expiration time as desired
//        confirmEmail.setConfirmCode(confirmationCode);
//        confirmEmail.setConfirm(false);
//        confirmEmail.setUsers().;
//        confirmEmailRepo.save(confirmEmail);
//    }

    @Override
    public ConfirmEmailDto createConfirmEmailDto(ConfirmEmailDto confirmEmailDto) {


        return null;
    }

    @Override
    public void deleteConfirmEmail(Long confirmEmailId) {

    }
}
