package com.x7ubi.kurswahl.common.service;

import com.x7ubi.kurswahl.admin.request.PasswordResetRequest;
import com.x7ubi.kurswahl.common.error.ErrorMessage;
import com.x7ubi.kurswahl.common.models.User;
import com.x7ubi.kurswahl.common.repository.UserRepo;
import com.x7ubi.kurswahl.common.request.ChangePasswordRequest;
import com.x7ubi.kurswahl.common.response.MessageResponse;
import com.x7ubi.kurswahl.common.response.ResultResponse;
import com.x7ubi.kurswahl.common.utils.PasswordGenerator;
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

    @Transactional
    public ResultResponse resetPassword(PasswordResetRequest passwordResetRequest) {
        ResultResponse response = new ResultResponse();

        response.setErrorMessages(this.userNotFound(passwordResetRequest.getUserId()));

        if (!response.getErrorMessages().isEmpty()) {
            return response;
        }

        User user = this.userRepo.findUserByUserId(passwordResetRequest.getUserId()).get();
        if (null == user.getGeneratedPassword()) {
            user.setGeneratedPassword(PasswordGenerator.generatePassword());
        }
        user.setPassword(this.passwordEncoder.encode(user.getGeneratedPassword()));
        this.userRepo.save(user);

        return response;
    }

    private List<MessageResponse> userNotFound(Long userId) {
        List<MessageResponse> error = new ArrayList<>();

        if (!this.userRepo.existsUserByUserId(userId)) {
            logger.error(ErrorMessage.General.USER_NOT_FOUND);
            error.add(new MessageResponse(ErrorMessage.General.USER_NOT_FOUND));
        }

        return error;
    }
}
