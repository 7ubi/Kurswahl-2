package com.x7ubi.kurswahl.service.authentication;

import com.x7ubi.kurswahl.error.ErrorMessage;
import com.x7ubi.kurswahl.models.User;
import com.x7ubi.kurswahl.repository.UserRepo;
import com.x7ubi.kurswahl.request.auth.SignupRequest;
import com.x7ubi.kurswahl.response.common.MessageResponse;
import com.x7ubi.kurswahl.response.common.ResultResponse;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthService {

    Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final PasswordEncoder passwordEncoder;

    private final UserRepo userRepo;

    public AuthService(PasswordEncoder passwordEncoder, UserRepo userRepo) {
        this.passwordEncoder = passwordEncoder;
        this.userRepo = userRepo;
    }

    @Transactional
    public ResultResponse registerNewUserAccount(SignupRequest signupRequest) {
        User user = new User();

        ResultResponse response = new ResultResponse();
        response.setErrorMessages(findRegisterErrors(signupRequest));

        if(response.getErrorMessages().size() > 0) {
            return response;
        }

        user.setUsername(signupRequest.getUsername());
        user.setFirstname(signupRequest.getFirstname());
        user.setSurname(signupRequest.getSurname());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));

        userRepo.save(user);

        response.setSuccess(true);

        logger.info("User was created");

        return response;
    }

    private List<MessageResponse> findRegisterErrors(SignupRequest signupRequest) {
        List<MessageResponse> errors = new ArrayList<>();

        if(userRepo.existsByUsername(signupRequest.getUsername())){
            logger.error(ErrorMessage.Authentication.USERNAME_EXITS);
            errors.add(new MessageResponse(ErrorMessage.Authentication.USERNAME_EXITS));
        }

        return errors;
    }
}
