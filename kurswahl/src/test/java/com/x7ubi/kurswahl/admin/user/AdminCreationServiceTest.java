package com.x7ubi.kurswahl.admin.user;

import com.x7ubi.kurswahl.error.ErrorMessage;
import com.x7ubi.kurswahl.models.Admin;
import com.x7ubi.kurswahl.models.User;
import com.x7ubi.kurswahl.repository.AdminRepo;
import com.x7ubi.kurswahl.request.admin.AdminSignupRequest;
import com.x7ubi.kurswahl.response.common.ResultResponse;
import com.x7ubi.kurswahl.service.admin.user.AdminCreationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest()
@TestPropertySource(properties = {
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.url=jdbc:h2:mem:kurswahlTestdb;NON_KEYWORDS=user",
        "spring.jpa.properties.hibernate.globally_quoted_identifiers=true"
})
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AdminCreationServiceTest {

    @Autowired
    private AdminCreationService adminCreationService;

    @Autowired
    private AdminRepo adminRepo;

    private Admin admin;

    @BeforeEach
    public void setupTests() {
        User user = new User();
        user.setUsername("test");
        user.setFirstname("Test");
        user.setSurname("User");
        user.setPassword("Password");
        admin = new Admin();
        admin.setUser(user);

        this.adminRepo.save(admin);
    }

    @Test
    public void testCreateAdmin() {
        // Given
        AdminSignupRequest adminSignupRequest = new AdminSignupRequest();
        adminSignupRequest.setFirstname("Firstname");
        adminSignupRequest.setSurname("Surname");
        adminSignupRequest.setUsername("Username");

        // When
        ResultResponse response = this.adminCreationService.registerAdmin(adminSignupRequest);

        // Then
        Admin createdAdmin = this.adminRepo.findAdminByUser_Username("Username").get();
        Assertions.assertTrue(response.getErrorMessages().isEmpty());
        Assertions.assertEquals(createdAdmin.getUser().getFirstname(), adminSignupRequest.getFirstname());
        Assertions.assertEquals(createdAdmin.getUser().getSurname(), adminSignupRequest.getSurname());
        Assertions.assertEquals(createdAdmin.getUser().getUsername(), adminSignupRequest.getUsername());
    }

    @Test
    public void testCreateAdminUsernameExists() {
        // Given
        AdminSignupRequest adminSignupRequest = new AdminSignupRequest();
        adminSignupRequest.setFirstname("Firstname");
        adminSignupRequest.setSurname("Surname");
        adminSignupRequest.setUsername("test");

        // When
        ResultResponse response = this.adminCreationService.registerAdmin(adminSignupRequest);

        // Then
        Admin createdAdmin = this.adminRepo.findAdminByUser_Username("test").get();
        Assertions.assertEquals(response.getErrorMessages().size(), 1);
        Assertions.assertEquals(response.getErrorMessages().get(0).getMessage(), ErrorMessage.Authentication.USERNAME_EXITS);
        Assertions.assertEquals(createdAdmin.getUser().getFirstname(), admin.getUser().getFirstname());
        Assertions.assertEquals(createdAdmin.getUser().getSurname(), admin.getUser().getSurname());
        Assertions.assertEquals(createdAdmin.getUser().getUsername(), admin.getUser().getUsername());
    }

    @Test
    public void testDeleteAdmin() {
        // Given
        this.admin = this.adminRepo.findAdminByUser_Username(this.admin.getUser().getUsername()).get();
        Long id = this.admin.getAdminId();

        // When
        ResultResponse response = this.adminCreationService.deleteAdmin(id);

        // Then
        Assertions.assertTrue(response.getErrorMessages().isEmpty());
        Assertions.assertFalse(this.adminRepo.existsAdminByUser_Username(this.admin.getUser().getUsername()));
    }

    @Test
    public void testDeleteAdminWrongId() {
        // Given
        this.admin = this.adminRepo.findAdminByUser_Username(this.admin.getUser().getUsername()).get();
        Long id = this.admin.getAdminId() + 1;

        // When
        ResultResponse response = this.adminCreationService.deleteAdmin(id);

        // Then
        Assertions.assertEquals(response.getErrorMessages().size(), 1);
        Assertions.assertEquals(response.getErrorMessages().get(0).getMessage(), ErrorMessage.Administration.ADMIN_NOT_FOUND);
        Assertions.assertTrue(this.adminRepo.existsAdminByUser_Username(this.admin.getUser().getUsername()));
    }
}