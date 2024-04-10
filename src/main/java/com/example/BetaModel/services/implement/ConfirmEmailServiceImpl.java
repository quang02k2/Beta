package com.example.BetaModel.services.implement;

import com.example.BetaModel.model.ConfirmEmail;
import com.example.BetaModel.model.Users;
import com.example.BetaModel.respository.ConfirmEmailRepo;
import com.example.BetaModel.respository.UsersRepo;
import com.example.BetaModel.services.iservices.ConfirmEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ConfirmEmailServiceImpl implements ConfirmEmailService {
    @Autowired
    private ConfirmEmailRepo confirmEmailRepo;

    @Autowired
    private UsersRepo usersRepo;

    public boolean checkEmail(String codeEmail) {
        ConfirmEmail confirmEmail = confirmEmailRepo.findByConfirmCode(codeEmail);
        if (confirmEmail != null && confirmEmail.getExpiredTime().isAfter(LocalDateTime.now())) {
            confirmEmail.setConfirm(true);
            confirmEmailRepo.save(confirmEmail);
            return true;
        }
        return false;
    }

//    public boolean checkEmail(String codeEmail) {
//        ConfirmEmail confirmEmail = confirmEmailRepo.findByConfirmCode(codeEmail);
//        if (confirmEmail.getConfirmCode() == codeEmail && confirmEmail != null && confirmEmail.getExpiredTime().isAfter(LocalDateTime.now())) {
//            confirmEmail.setConfirm(true);
//            confirmEmailRepo.save(confirmEmail);
//            return true;
//        }
//        return false;
//    }


}
