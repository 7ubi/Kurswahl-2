package com.x7ubi.kurswahl.service.admin;

import com.x7ubi.kurswahl.error.ErrorMessage;
import com.x7ubi.kurswahl.repository.UserRepo;
import com.x7ubi.kurswahl.request.auth.SignupRequest;
import com.x7ubi.kurswahl.response.common.MessageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractUserCreationService {
    Logger logger = LoggerFactory.getLogger(AbstractUserCreationService.class);

    protected final UserRepo userRepo;

    protected AbstractUserCreationService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    protected List<MessageResponse> findRegisterErrors(SignupRequest signupRequest) {
        List<MessageResponse> errors = new ArrayList<>();

        if(userRepo.existsByUsername(signupRequest.getUsername())){
            logger.error(ErrorMessage.Authentication.USERNAME_EXITS);
            errors.add(new MessageResponse(ErrorMessage.Authentication.USERNAME_EXITS));
        }

        return errors;
    }
}
