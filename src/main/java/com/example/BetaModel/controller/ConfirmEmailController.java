package com.example.BetaModel.controller;

import com.example.BetaModel.model.ConfirmEmail;
import com.example.BetaModel.responses.ApiResponse;
import com.example.BetaModel.respository.ConfirmEmailRepo;
import com.example.BetaModel.services.iservices.ConfirmEmailService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("${api.prefix}/confirmEmail")
public class ConfirmEmailController {
    @Autowired
    private ConfirmEmailService confirmEmailService;

    @Autowired
    private ConfirmEmailRepo confirmEmailRepo;

    @PutMapping("/checkEmail")
    public ResponseEntity<ApiResponse> checkEmail(@RequestParam String codeEmail) {
        boolean isEmailValid = this.confirmEmailService.checkEmail(codeEmail);
        if (isEmailValid) {
            return ResponseEntity.ok(new ApiResponse("Email verification successful", true));
        } else {
            return ResponseEntity.badRequest().body(new ApiResponse("Email verification failed", false));
        }
    }

}
