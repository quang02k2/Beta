package com.example.BetaModel.services.implement;


import com.example.BetaModel.components.JwtTokenUtils;
import com.example.BetaModel.components.LocalizationUtils;
import com.example.BetaModel.dtos.UserDTO;
import com.example.BetaModel.exceptions.DataNotFoundException;
import com.example.BetaModel.exceptions.ResourceNotFoundException;
import com.example.BetaModel.model.ConfirmEmail;
import com.example.BetaModel.model.Role;
import com.example.BetaModel.model.Users;
import com.example.BetaModel.responses.UserResponse;
import com.example.BetaModel.respository.ConfirmEmailRepo;
import com.example.BetaModel.respository.RoleRepo;
import com.example.BetaModel.respository.UsersRepo;
import com.example.BetaModel.services.iservices.ConfirmEmailService;
import com.example.BetaModel.services.iservices.IUserService;
import com.example.BetaModel.utils.MessageKeys;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService implements IUserService {
    private final UsersRepo userRepository;
    private final RoleRepo roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtils jwtTokenUtil;
    private final AuthenticationManager authenticationManager;
    private final LocalizationUtils localizationUtils;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ConfirmEmailRepo confirmEmailRepo;



    @Autowired
    private ConfirmEmailService confirmEmailService;
    @Override
    public Users createUser(UserDTO userDTO) throws Exception {
        // Register user
        String email = userDTO.getEmail();
        // Check if email already exists
        if (userRepository.existsByEmail(email)) {
            throw new DataIntegrityViolationException("Email already exists");
        }
        Role userRole = roleRepository.findById(1L)
                .orElseThrow(() -> new IllegalStateException("Role not found with ID 1"));

        // Convert from UserDTO to User
        Users newUser = Users.builder().point(userDTO.getPoint())
                .email(userDTO.getEmail()).userName(userDTO.getUserName())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .name(userDTO.getName()).phoneNumber(userDTO.getPhoneNumber())
                .isActive(userDTO.isActive()).role(userRole).build();

        checkEmail(newUser);
//        if (confirmEmailService.checkEmail(newUser.getEmail())) {
//            userRepository.save(newUser);
//            return newUser;
//        } else {
//            // Handle email verification failure
//            throw new IllegalStateException("Email verification failed");
//        }
        userRepository.save(newUser);

        return newUser;

    }


    public void checkEmail(Users users){
        String code = generateConfirmationCode();
        try {
            sendConfirmationEmail(users.getEmail(), code);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        ConfirmEmail confirmEmail = new ConfirmEmail();
        confirmEmail.setUsers(users);
        confirmEmail.setConfirmCode(code);
        confirmEmail.setRequiredTime(LocalDateTime.now());
        confirmEmail.setExpiredTime(LocalDateTime.now().plusMinutes(1));
        confirmEmail.setConfirm(false);
        confirmEmailRepo.save(confirmEmail);

    }


    @Autowired
    private JavaMailSender javaMailSender;

    private String generateConfirmationCode() {
        int code = (int) (Math.random() * 900000) + 100000;
        return String.valueOf(code);
    }

    public void sendConfirmationEmail(String recipientEmail, String confirmationCode) throws MessagingException {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("ongbaanhyeu4@gmail.com");
        message.setTo(recipientEmail);
        message.setSubject("Confirmation Code");
        message.setText("Your confirmation code is: " + confirmationCode);
        // Send the email
        javaMailSender.send(message);

    }




    @Override
    public String login(
            String email,
            String password
    ) throws Exception {
        Optional<Users> optionalUser = userRepository.findByEmail(email);
        if(optionalUser.isEmpty()) {
            throw new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.WRONG_PHONE_PASSWORD));
        }
        //lấy ra user
        Users existingUser = optionalUser.get();

        //Chuyền email,password, role vào authenticationToken để xac thực ngươi dùng
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                email, password,
                existingUser.getAuthorities()
        );
        //Xác thực người dùng
        authenticationManager.authenticate(authenticationToken);

        //sinh ngẫu nhiên 1 token từ existingUser
        String token = jwtTokenUtil.generateToken(existingUser);

        return token;
    }


    private void saveConfirmationCode(String email, String code) {
        Optional<Users> existingUser = userRepository.findByEmail(email);
        existingUser.ifPresent(user -> {
            ConfirmEmail confirmEmail = confirmEmailRepo.findByUsers(user);
            if (confirmEmail != null) {
                confirmEmail.setConfirmCode(code);
                confirmEmailRepo.save(confirmEmail);
            }
        });
    }

    public void sendConfirmationEmail1(String email) {
        String code = generateConfirmationCode();
        try {
            sendConfirmationEmail(email, code);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        saveConfirmationCode(email, code);
    }

    public void verifyConfirmationCode(String email, String code) {
        Optional<Users> existingUser = userRepository.findByEmail(email);
        if (existingUser.isPresent()) {
            ConfirmEmail confirmEmail = confirmEmailRepo.findByUsersAndConfirmCode(existingUser.get(), code);
            confirmEmail.setConfirm(true);

        }
    }

    public void forgotPass(String email, String newPassword) {
        Optional<Users> existingUser = userRepository.findByEmail(email);
        existingUser.ifPresent(user -> {
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
        });
    }


    public void changePassword(String email, String password, String newPassword) {
        Optional<Users> existingUser = userRepository.findByEmail(email);
        existingUser.ifPresent(user -> {
            // Verify if the provided password matches the user's current password
            if (passwordEncoder.matches(password, user.getPassword())) {
                // Update the user's password with the new password
                user.setPassword(passwordEncoder.encode(newPassword));
                userRepository.save(user);
            } else {
                throw new RuntimeException("Incorrect password.");
            }
        });
    }

    @Override
    public UserResponse getAllUser(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("asc")?Sort.by(sortBy).ascending(): Sort.by(sortBy).descending());
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sort);
        Page<Users> usersPage = this.userRepository.findAll(pageRequest);
        List<Users> allUsers = usersPage.getContent();
        List<UserDTO> userDTOS = allUsers.stream().map(users -> this.userToDto(users)).collect(Collectors.toList());

        UserResponse userResponse = new UserResponse();
        userResponse.setContent(userDTOS);
        userResponse.setPageNumber(usersPage.getNumber());
        userResponse.setPageSize(usersPage.getSize());
        userResponse.setTotalElement(userResponse.getTotalElement());
        userResponse.setLastPage(usersPage.isLast());
        userResponse.setTotalPage(usersPage.getTotalPages());

        return userResponse;
    }

    @Override
    public UserDTO getUserId(Long userId) {
        Users users = this.userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User", "UserId", userId));

        return this.userToDto(users);
    }

    @Override
    public void deleteUserId(Long userId) {
        Users users = this.userRepository.findById(userId).orElseThrow(null);
        this.userRepository.delete(users);
    }



    public Users dtoToUser(UserDTO userDTO){
        return modelMapper.map(userDTO, Users.class);
    }

    public UserDTO userToDto(Users users){
        return modelMapper.map(users, UserDTO.class);
    }
}
