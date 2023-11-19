package com.x7ubi.kurswahl.service.authentication;

import com.x7ubi.kurswahl.error.ErrorMessage;
import com.x7ubi.kurswahl.models.User;
import com.x7ubi.kurswahl.repository.UserRepo;
import com.x7ubi.kurswahl.request.auth.ChangePasswordRequest;
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
public class ChangePasswordService {

    private final Logger logger = LoggerFactory.getLogger(ChangePasswordService.class);

    private final UserRepo userRepo;

    private final PasswordEncoder passwordEncoder;

    public ChangePasswordService(UserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public ResultResponse changePassword(String username, ChangePasswordRequest changePasswordRequest) {
        ResultResponse resultResponse = new ResultResponse();
        User user = this.userRepo.findByUsername(username).get();
        resultResponse.setErrorMessages(oldPasswordCorrect(user, changePasswordRequest.getOldPassword()));

        if (!resultResponse.getErrorMessages().isEmpty()) {
            return resultResponse;
        }


        user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        this.userRepo.save(user);

        logger.info(String.format("Changed %s's password", username));

        return resultResponse;
    }


    private List<MessageResponse> oldPasswordCorrect(User user, String oldPassword) {
        List<MessageResponse> error = new ArrayList<>();

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            logger.error(ErrorMessage.General.WRONG_OLD_PASSWORD);
            error.add(new MessageResponse(ErrorMessage.General.WRONG_OLD_PASSWORD));
        }

        return error;
    }
}
