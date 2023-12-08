package com.x7ubi.kurswahl.common.service;

import com.x7ubi.kurswahl.common.exception.EntityNotFoundException;
import com.x7ubi.kurswahl.common.exception.PasswordNotMatchingException;
import com.x7ubi.kurswahl.common.request.PasswordResetRequest;
import com.x7ubi.kurswahl.common.error.ErrorMessage;
import com.x7ubi.kurswahl.common.models.User;
import com.x7ubi.kurswahl.common.repository.UserRepo;
import com.x7ubi.kurswahl.common.request.ChangePasswordRequest;
import com.x7ubi.kurswahl.common.utils.PasswordGenerator;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
    public void changePassword(String username, ChangePasswordRequest changePasswordRequest)
            throws EntityNotFoundException, PasswordNotMatchingException {
        Optional<User> userOptional = this.userRepo.findByUsername(username);
        if(userOptional.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.General.USER_NOT_FOUND);
        }
        User user = userOptional.get();
        oldPasswordCorrect(user, changePasswordRequest.getOldPassword());

        user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        this.userRepo.save(user);

        logger.info(String.format("Changed %s's password", username));
    }


    private void oldPasswordCorrect(User user, String oldPassword) throws PasswordNotMatchingException {
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new PasswordNotMatchingException(ErrorMessage.General.WRONG_OLD_PASSWORD);
        }
    }

    @Transactional
    public void resetPassword(PasswordResetRequest passwordResetRequest) throws EntityNotFoundException {
        Optional<User> userOptional = this.userRepo.findUserByUserId(passwordResetRequest.getUserId());
        if(userOptional.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.General.USER_NOT_FOUND);
        }
        User user = userOptional.get();
        if (null == user.getGeneratedPassword()) {
            user.setGeneratedPassword(PasswordGenerator.generatePassword());
        }
        user.setPassword(this.passwordEncoder.encode(user.getGeneratedPassword()));
        this.userRepo.save(user);
    }
}
