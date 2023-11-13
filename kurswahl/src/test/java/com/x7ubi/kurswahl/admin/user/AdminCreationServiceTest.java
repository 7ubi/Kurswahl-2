package com.x7ubi.kurswahl.admin.user;

import com.x7ubi.kurswahl.KurswahlServiceTest;
import com.x7ubi.kurswahl.error.ErrorMessage;
import com.x7ubi.kurswahl.models.Admin;
import com.x7ubi.kurswahl.models.User;
import com.x7ubi.kurswahl.repository.AdminRepo;
import com.x7ubi.kurswahl.request.admin.AdminSignupRequest;
import com.x7ubi.kurswahl.response.admin.user.AdminResponse;
import com.x7ubi.kurswahl.response.admin.user.AdminResponses;
import com.x7ubi.kurswahl.response.admin.user.AdminResultResponse;
import com.x7ubi.kurswahl.response.common.ResultResponse;
import com.x7ubi.kurswahl.service.admin.user.AdminCreationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@KurswahlServiceTest
public class AdminCreationServiceTest {

    @Autowired
    private AdminCreationService adminCreationService;

    @Autowired
    private AdminRepo adminRepo;

    private Admin admin;

    @BeforeEach
    public void setupTests() {
        User user = new User();
        user.setUsername("test.user");
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

        // When
        this.adminCreationService.registerAdmin(adminSignupRequest);

        // Then
        Admin createdAdmin = this.adminRepo.findAdminByUser_Username("firstname.surname").get();
        Assertions.assertEquals(createdAdmin.getUser().getFirstname(), adminSignupRequest.getFirstname());
        Assertions.assertEquals(createdAdmin.getUser().getSurname(), adminSignupRequest.getSurname());
        Assertions.assertEquals(createdAdmin.getUser().getUsername(),
                String.format("%s.%s", adminSignupRequest.getFirstname().toLowerCase(),
                        adminSignupRequest.getSurname().toLowerCase()));
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

    @Test
    public void testEditAdmin() {
        // Given
        this.admin = this.adminRepo.findAdminByUser_Username(this.admin.getUser().getUsername()).get();
        Long id = this.admin.getAdminId();
        AdminSignupRequest adminSignupRequest = new AdminSignupRequest();
        adminSignupRequest.setFirstname("Firstname");
        adminSignupRequest.setSurname("Surname");

        // When
        ResultResponse response = this.adminCreationService.editAdmin(id, adminSignupRequest);

        // Then
        Assertions.assertTrue(response.getErrorMessages().isEmpty());
        Admin editedAdmin = this.adminRepo.findAdminByUser_Username("test.user").get();
        Assertions.assertEquals(editedAdmin.getUser().getFirstname(), adminSignupRequest.getFirstname());
        Assertions.assertEquals(editedAdmin.getUser().getSurname(), adminSignupRequest.getSurname());
    }

    @Test
    public void testEditAdminWrongId() {
        // Given
        this.admin = this.adminRepo.findAdminByUser_Username(this.admin.getUser().getUsername()).get();
        Long id = this.admin.getAdminId() + 1;
        AdminSignupRequest adminSignupRequest = new AdminSignupRequest();
        adminSignupRequest.setFirstname("Firstname");
        adminSignupRequest.setSurname("Surname");

        // When
        ResultResponse response = this.adminCreationService.editAdmin(id, adminSignupRequest);

        // Then
        Assertions.assertEquals(response.getErrorMessages().size(), 1);
        Assertions.assertEquals(response.getErrorMessages().get(0).getMessage(),
                ErrorMessage.Administration.ADMIN_NOT_FOUND);
        Admin editedAdmin = this.adminRepo.findAdminByUser_Username("test.user").get();
        Assertions.assertEquals(editedAdmin.getUser().getFirstname(), this.admin.getUser().getFirstname());
        Assertions.assertEquals(editedAdmin.getUser().getSurname(), this.admin.getUser().getSurname());
    }

    @Test
    public void testGetAdmin() {
        // Given
        this.admin = this.adminRepo.findAdminByUser_Username(this.admin.getUser().getUsername()).get();
        Long id = this.admin.getAdminId();

        // When
        AdminResultResponse response = this.adminCreationService.getAdmin(id);

        // Then
        Assertions.assertTrue(response.getErrorMessages().isEmpty());
        Assertions.assertEquals(response.getAdminResponse().getAdminId(), this.admin.getAdminId());
        Assertions.assertEquals(response.getAdminResponse().getFirstname(), this.admin.getUser().getFirstname());
        Assertions.assertEquals(response.getAdminResponse().getSurname(), this.admin.getUser().getSurname());
        Assertions.assertEquals(response.getAdminResponse().getUsername(), this.admin.getUser().getUsername());
        Assertions.assertEquals(response.getAdminResponse().getGeneratedPassword(),
                this.admin.getUser().getGeneratedPassword());
    }

    @Test
    public void testGetAdminWrongId() {
        // Given
        this.admin = this.adminRepo.findAdminByUser_Username(this.admin.getUser().getUsername()).get();
        Long id = this.admin.getAdminId() + 1;

        // When
        AdminResultResponse response = this.adminCreationService.getAdmin(id);

        // Then
        Assertions.assertEquals(response.getErrorMessages().size(), 1);
        Assertions.assertEquals(response.getErrorMessages().get(0).getMessage(),
                ErrorMessage.Administration.ADMIN_NOT_FOUND);
    }

    @Test
    public void testGetAllAdmins() {
        // When
        AdminResponses adminResponses = this.adminCreationService.getAllAdmins();

        // Then
        Assertions.assertEquals(adminResponses.getAdminResponses().size(), 1);

        AdminResponse adminResponse = adminResponses.getAdminResponses().get(0);
        Assertions.assertEquals(adminResponse.getAdminId(), this.admin.getAdminId());
        Assertions.assertEquals(adminResponse.getFirstname(), this.admin.getUser().getFirstname());
        Assertions.assertEquals(adminResponse.getSurname(), this.admin.getUser().getSurname());
        Assertions.assertEquals(adminResponse.getUsername(), this.admin.getUser().getUsername());
        Assertions.assertEquals(adminResponse.getGeneratedPassword(),
                this.admin.getUser().getGeneratedPassword());
    }
}
