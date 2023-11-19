package com.x7ubi.kurswahl.authentication;

import com.x7ubi.kurswahl.KurswahlServiceTest;
import com.x7ubi.kurswahl.error.ErrorMessage;
import com.x7ubi.kurswahl.models.User;
import com.x7ubi.kurswahl.repository.UserRepo;
import com.x7ubi.kurswahl.request.auth.ChangePasswordRequest;
import com.x7ubi.kurswahl.response.common.ResultResponse;
import com.x7ubi.kurswahl.service.authentication.ChangePasswordService;
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
}
