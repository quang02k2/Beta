package com.example.BetaModel.controller;


import com.example.BetaModel.model.*;
import com.example.BetaModel.respository.ConfirmEmailRepo;
import com.example.BetaModel.services.implement.UserService;
import com.example.BetaModel.services.iservices.ConfirmEmailService;
import org.springframework.mail.javamail.JavaMailSender;

import com.example.BetaModel.components.LocalizationUtils;
import com.example.BetaModel.configurations.AppConstants;
import com.example.BetaModel.dtos.UserDTO;
import com.example.BetaModel.dtos.UserDtoRefresh;
import com.example.BetaModel.dtos.UserLoginDTO;
import com.example.BetaModel.exceptions.TokenRefreshException;
import com.example.BetaModel.request.SignupRequest;
import com.example.BetaModel.request.TokenRefreshRequest;
import com.example.BetaModel.responses.*;
import com.example.BetaModel.respository.RoleRepo;
import com.example.BetaModel.respository.UsersRepo;
import com.example.BetaModel.services.iservices.IUserService;
import com.example.BetaModel.services.iservices.RefreshTokenService;
import com.example.BetaModel.services.iservices.UserDetailsImpl;
import com.example.BetaModel.services.security.JwtUtils;
import com.example.BetaModel.utils.MessageKeys;
import com.nimbusds.oauth2.sdk.token.RefreshToken;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.prefix}/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final LocalizationUtils localizationUtils;
    private final UserDetailsService userDetailsService;

    @Autowired
    private ConfirmEmailService confirmEmailService;


    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> createUser(@Valid @RequestBody UserDTO userDTO, BindingResult result) {
        RegisterResponse registerResponse = new RegisterResponse();

        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();

            registerResponse.setMessage(errorMessages.toString());
            return ResponseEntity.badRequest().body(registerResponse);
        }
        try {



            Users user = userService.createUser(userDTO);

//            if(confirmEmailService.checkEmail())

//            boolean isCheck = confirmEmailService.checkEmail(userService)

            registerResponse.setMessage("Đăng kí thành công");
            registerResponse.setUser(user);
            return ResponseEntity.ok(registerResponse);
        } catch (Exception e) {
            registerResponse.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(registerResponse);
        }
    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody UserLoginDTO userLoginDTO
    ) {
        try {
            String token = userService.login(
                    userLoginDTO.getEmail(),
                    userLoginDTO.getPassword()
            );

            Users userDetails = (Users) userDetailsService.loadUserByUsername(userLoginDTO.getEmail());
            List<String> role = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            RefreshTokens refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

            // Trả về token trong response
            return new ResponseEntity<>(new LoginResponse("Đăng nhập thành công", token, refreshToken.getToken(),  role), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    LoginResponse.builder()
                            .message(localizationUtils.getLocalizedMessage(MessageKeys.LOGIN_FAILED, e.getMessage()))
                            .build()
            );
        }
    }

//    @PutMapping("/forgotPass")
//    public ResponseEntity<UserDTO> forgotPassUser(@Valid @RequestBody UserDTO userDTO, @RequestParam String email, @RequestParam String code, @RequestParam String newPassword){
//        UserDTO update = this.userService.forgotPassword(userDTO, email,code, newPassword);
//        return new ResponseEntity<UserDTO>(update, HttpStatus.OK);
//    }
//
////    @PreAuthorize("hasRole('USER')")
//    @PutMapping("/changePass")
//    public ResponseEntity<UserDTO> changePassUser(@Valid @RequestBody UserDTO userDTO, @RequestParam String password, @RequestParam String newPassword){
//        UserDTO update = this.userService.changePassword(userDTO, password, newPassword);
//        return new ResponseEntity<UserDTO>(update, HttpStatus.OK);
//    }


    @PostMapping("/confirmation")
    public ResponseEntity<String> sendConfirmationEmail(@RequestParam String email) {
        try {
            userService.sendConfirmationEmail1(email);
            return ResponseEntity.ok("Confirmation email sent successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send confirmation email.");
        }
    }

    @GetMapping("/confirmation/verify")
    public ResponseEntity<ApiResponse> verifyConfirmationCode(@RequestParam String email, @RequestParam String code) {
        userService.verifyConfirmationCode(email, code);
        return new ResponseEntity<ApiResponse>(new ApiResponse("true", true), HttpStatus.OK);
    }

    @PutMapping("/forgotPass")
    public ResponseEntity<String> forgotPassCode(@Valid @RequestParam String email, @RequestParam String newPassword) {
        try {
            userService.forgotPass(email, newPassword);
            return ResponseEntity.ok("Successfully created new password");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create new password");
        }
    }

    @PostMapping("/password/change")
    public ResponseEntity<String> changePassword(@RequestParam("email") String email,@RequestParam String password, @RequestParam("newPassword") String newPassword) {
        try {
            userService.changePassword(email, password, newPassword);
            return ResponseEntity.ok("Password changed successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to change password.");
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse> deleteCourse(@RequestParam Long userId){
        this.userService.deleteUserId(userId);
        return new ResponseEntity<ApiResponse>(new ApiResponse("Course is deleted successfully", true), HttpStatus.OK);
    }

    @GetMapping("/getAll")
    private ResponseEntity<UserResponse> getAll(@RequestParam( value = "pageNumber",
                                                defaultValue = AppConstants.PAGE_NUMBER,
                                                required = false) Integer pageNumber,
                                                @RequestParam(value = "pageSize",
                                                defaultValue = AppConstants.PAGE_SIZE,
                                                required = false) Integer pageSize,
                                                @RequestParam(value = "sortBy",
                                                defaultValue = AppConstants.SORT_BY,
                                                required = false) String sortBy,
                                                @RequestParam(value = "sortDir",
                                                defaultValue = AppConstants.SORT_DIR,
                                                required = false) String sortDir){
        UserResponse userResponse = this.userService.getAllUser(pageNumber,pageSize,sortBy,sortDir);
        return new ResponseEntity<UserResponse>(userResponse, HttpStatus.OK);
    }

    @GetMapping("/getUserId")
    private ResponseEntity<UserDTO> getUserId(@RequestParam Long userId){
        UserDTO userDTO = this.userService.getUserId(userId);
        return new ResponseEntity<UserDTO>(userDTO, HttpStatus.OK);
    }



    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UsersRepo userRepository;

    @Autowired
    RoleRepo roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    private RefreshTokenService refreshTokenService;


    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();
        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshTokens::getUsers)
                .map(user -> {
                    String token = jwtUtils.generateTokenFromUsername(user.getUsername());
                    return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                        "Refresh token is not in database!"));
    }



}
