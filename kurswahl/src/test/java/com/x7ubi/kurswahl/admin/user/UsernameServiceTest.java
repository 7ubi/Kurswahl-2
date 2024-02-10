package com.x7ubi.kurswahl.admin.user;

import com.x7ubi.kurswahl.KurswahlServiceTest;
import com.x7ubi.kurswahl.admin.user.service.UsernameService;
import com.x7ubi.kurswahl.common.auth.request.SignupRequest;
import com.x7ubi.kurswahl.common.models.User;
import com.x7ubi.kurswahl.common.repository.UserRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@KurswahlServiceTest
public class UsernameServiceTest {

    @Autowired
    private UsernameService usernameService;

    @Autowired
    private UserRepo userRepo;

    private User user;

    @BeforeEach
    public void setupTests() {
        user = new User();
        user.setUsername("max.mustermann");
        user.setFirstname("Max");
        user.setSurname("Mustermann");
        user.setPassword("Password");

        this.userRepo.save(user);
    }

    @Test
    public void testGenerateUsername() {
        // Given
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setFirstname("Max Lukas");
        signupRequest.setSurname("Mustermann");

        // When
        String username = this.usernameService.generateUsernameFromName(signupRequest);

        // Then
        Assertions.assertEquals(username, "max.lukas.mustermann");
    }

    @Test
    public void testGenerateUsernameNameExists() {
        // Given
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setFirstname("Max");
        signupRequest.setSurname("Mustermann");

        // When
        String username = this.usernameService.generateUsernameFromName(signupRequest);

        // Then
        Assertions.assertEquals(username, "max1.mustermann");
    }
}
