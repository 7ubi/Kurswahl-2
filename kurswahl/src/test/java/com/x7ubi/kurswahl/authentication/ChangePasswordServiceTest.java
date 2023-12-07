package com.x7ubi.kurswahl.authentication;

import com.x7ubi.kurswahl.KurswahlServiceTest;
import com.x7ubi.kurswahl.common.request.PasswordResetRequest;
import com.x7ubi.kurswahl.common.error.ErrorMessage;
import com.x7ubi.kurswahl.common.models.User;
import com.x7ubi.kurswahl.common.repository.UserRepo;
import com.x7ubi.kurswahl.common.request.ChangePasswordRequest;
import com.x7ubi.kurswahl.common.response.ResultResponse;
import com.x7ubi.kurswahl.common.service.ChangePasswordService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@KurswahlServiceTest
public class ChangePasswordServiceTest {

    @Autowired
    private ChangePasswordService changePasswordService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User user;

    @BeforeEach
    public void setupUser() {
        user = new User();
        user.setUsername("test.user");
        user.setFirstname("Test");
        user.setSurname("User");
        user.setPassword(passwordEncoder.encode("Password"));
        this.userRepo.save(user);
    }

    @Test
    public void testChangePassword() {
        // Given
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
        changePasswordRequest.setOldPassword("Password");
        changePasswordRequest.setNewPassword("NewPassword");

        // When
        ResultResponse response = this.changePasswordService.changePassword(user.getUsername(), changePasswordRequest);

        // Then
        Assertions.assertTrue(response.getErrorMessages().isEmpty());

        User updatedUser = this.userRepo.findByUsername(user.getUsername()).get();
        Assertions.assertTrue(passwordEncoder.matches("NewPassword", updatedUser.getPassword()));
    }

    @Test
    public void testChangePasswordWrongPassword() {
        // Given
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
        changePasswordRequest.setOldPassword("WrongPassword");
        changePasswordRequest.setNewPassword("NewPassword");

        // When
        ResultResponse response = this.changePasswordService.changePassword(user.getUsername(), changePasswordRequest);

        // Then
        Assertions.assertEquals(response.getErrorMessages().size(), 1);
        Assertions.assertEquals(response.getErrorMessages().get(0).getMessage(),
                ErrorMessage.General.WRONG_OLD_PASSWORD);

        User updatedUser = this.userRepo.findByUsername(user.getUsername()).get();
        Assertions.assertTrue(passwordEncoder.matches("Password", updatedUser.getPassword()));
    }

    @Test
    public void testResetPassword() {
        // Given
        PasswordResetRequest passwordResetRequest = new PasswordResetRequest();
        passwordResetRequest.setUserId(this.userRepo.findByUsername(user.getUsername()).get().getUserId());

        // When
        ResultResponse response = this.changePasswordService.resetPassword(passwordResetRequest);

        // Then
        Assertions.assertTrue(response.getErrorMessages().isEmpty());

        User updatedUser = this.userRepo.findByUsername(user.getUsername()).get();
        Assertions.assertTrue(passwordEncoder.matches(updatedUser.getGeneratedPassword(), updatedUser.getPassword()));
    }

    @Test
    public void testResetPasswordWrongId() {
        // Given
        PasswordResetRequest passwordResetRequest = new PasswordResetRequest();
        passwordResetRequest.setUserId(this.userRepo.findByUsername(user.getUsername()).get().getUserId() + 1);

        // When
        ResultResponse response = this.changePasswordService.resetPassword(passwordResetRequest);

        // Then
        Assertions.assertEquals(response.getErrorMessages().size(), 1);
        Assertions.assertEquals(response.getErrorMessages().get(0).getMessage(), ErrorMessage.General.USER_NOT_FOUND);

        User updatedUser = this.userRepo.findByUsername(user.getUsername()).get();
        Assertions.assertTrue(passwordEncoder.matches("Password", updatedUser.getPassword()));
    }
}
