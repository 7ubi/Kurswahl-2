package com.x7ubi.kurswahl.authentication;

import com.x7ubi.kurswahl.KurswahlServiceTest;
import com.x7ubi.kurswahl.common.auth.request.ChangePasswordRequest;
import com.x7ubi.kurswahl.common.auth.request.PasswordResetRequest;
import com.x7ubi.kurswahl.common.auth.service.ChangePasswordService;
import com.x7ubi.kurswahl.common.error.ErrorMessage;
import com.x7ubi.kurswahl.common.exception.EntityNotFoundException;
import com.x7ubi.kurswahl.common.exception.PasswordNotMatchingException;
import com.x7ubi.kurswahl.common.models.User;
import com.x7ubi.kurswahl.common.repository.UserRepo;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

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
    public void testChangePassword() throws EntityNotFoundException, PasswordNotMatchingException {
        // Given
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
        changePasswordRequest.setOldPassword("Password");
        changePasswordRequest.setNewPassword("NewPassword");

        // When
        this.changePasswordService.changePassword(user.getUsername(), changePasswordRequest);

        // Then
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
        PasswordNotMatchingException exception = Assert.assertThrows(PasswordNotMatchingException.class, () ->
                this.changePasswordService.changePassword(user.getUsername(), changePasswordRequest));

        // Then
        Assertions.assertEquals(exception.getMessage(), ErrorMessage.WRONG_OLD_PASSWORD);

        User updatedUser = this.userRepo.findByUsername(user.getUsername()).get();
        Assertions.assertTrue(passwordEncoder.matches("Password", updatedUser.getPassword()));
    }

    @Test
    public void testChangePasswordWrongUsername() {
        // Given
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
        changePasswordRequest.setOldPassword("WrongPassword");
        changePasswordRequest.setNewPassword("NewPassword");

        // When
        EntityNotFoundException entityNotFoundException = Assert.assertThrows(EntityNotFoundException.class, () ->
                this.changePasswordService.changePassword("wrong", changePasswordRequest));

        // Then
        Assertions.assertEquals(entityNotFoundException.getMessage(), ErrorMessage.USER_NOT_FOUND);

        User updatedUser = this.userRepo.findByUsername(user.getUsername()).get();
        Assertions.assertTrue(passwordEncoder.matches("Password", updatedUser.getPassword()));
    }

    @Test
    public void testResetPassword() throws EntityNotFoundException {
        // Given
        PasswordResetRequest passwordResetRequest = new PasswordResetRequest();
        passwordResetRequest.setUserId(this.userRepo.findByUsername(user.getUsername()).get().getUserId());

        // When
        this.changePasswordService.resetPassword(passwordResetRequest);

        // Then
        User updatedUser = this.userRepo.findByUsername(user.getUsername()).get();
        Assertions.assertTrue(passwordEncoder.matches(updatedUser.getGeneratedPassword(), updatedUser.getPassword()));
    }

    @Test
    public void testResetPasswordWrongId() {
        // Given
        PasswordResetRequest passwordResetRequest = new PasswordResetRequest();
        passwordResetRequest.setUserId(this.userRepo.findByUsername(user.getUsername()).get().getUserId() + 1);

        // When
        EntityNotFoundException entityNotFoundException = Assert.assertThrows(EntityNotFoundException.class, () ->
                this.changePasswordService.resetPassword(passwordResetRequest));

        // Then
        Assertions.assertEquals(entityNotFoundException.getMessage(), ErrorMessage.USER_NOT_FOUND);

        User updatedUser = this.userRepo.findByUsername(user.getUsername()).get();
        Assertions.assertTrue(passwordEncoder.matches("Password", updatedUser.getPassword()));
    }

    @Test
    public void testResetPasswords() throws EntityNotFoundException {
        // Given
        PasswordResetRequest passwordResetRequest = new PasswordResetRequest();
        passwordResetRequest.setUserId(this.userRepo.findByUsername(user.getUsername()).get().getUserId());
        List<PasswordResetRequest> passwordResetRequests = List.of(passwordResetRequest);

        // When
        this.changePasswordService.resetPasswords(passwordResetRequests);

        // Then
        User updatedUser = this.userRepo.findByUsername(user.getUsername()).get();
        Assertions.assertTrue(passwordEncoder.matches(updatedUser.getGeneratedPassword(), updatedUser.getPassword()));
    }
}
