package com.x7ubi.kurswahl.admin.user;

import com.x7ubi.kurswahl.KurswahlServiceTest;
import com.x7ubi.kurswahl.admin.user.request.AdminSignupRequest;
import com.x7ubi.kurswahl.admin.user.response.AdminResponse;
import com.x7ubi.kurswahl.admin.user.response.AdminResponses;
import com.x7ubi.kurswahl.admin.user.service.AdminCreationService;
import com.x7ubi.kurswahl.common.error.ErrorMessage;
import com.x7ubi.kurswahl.common.exception.EntityNotFoundException;
import com.x7ubi.kurswahl.common.models.Admin;
import com.x7ubi.kurswahl.common.models.User;
import com.x7ubi.kurswahl.common.repository.AdminRepo;
import org.junit.Assert;
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
    public void testDeleteAdmin() throws EntityNotFoundException {
        // Given
        this.admin = this.adminRepo.findAdminByUser_Username(this.admin.getUser().getUsername()).get();
        Long id = this.admin.getAdminId();

        // When
        this.adminCreationService.deleteAdmin(id);

        // Then
        Assertions.assertFalse(this.adminRepo.existsAdminByUser_Username(this.admin.getUser().getUsername()));
    }

    @Test
    public void testDeleteAdminWrongId() {
        // Given
        this.admin = this.adminRepo.findAdminByUser_Username(this.admin.getUser().getUsername()).get();
        Long id = this.admin.getAdminId() + 1;

        // When
        EntityNotFoundException entityNotFoundException = Assert.assertThrows(EntityNotFoundException.class, () ->
                this.adminCreationService.deleteAdmin(id));

        // Then
        Assertions.assertEquals(entityNotFoundException.getMessage(), ErrorMessage.ADMIN_NOT_FOUND);
        Assertions.assertTrue(this.adminRepo.existsAdminByUser_Username(this.admin.getUser().getUsername()));
    }

    @Test
    public void testEditAdmin() throws EntityNotFoundException {
        // Given
        this.admin = this.adminRepo.findAdminByUser_Username(this.admin.getUser().getUsername()).get();
        Long id = this.admin.getAdminId();
        AdminSignupRequest adminSignupRequest = new AdminSignupRequest();
        adminSignupRequest.setFirstname("Firstname");
        adminSignupRequest.setSurname("Surname");

        // When
        this.adminCreationService.editAdmin(id, adminSignupRequest);

        // Then
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
        EntityNotFoundException entityNotFoundException = Assert.assertThrows(EntityNotFoundException.class, () ->
                this.adminCreationService.editAdmin(id, adminSignupRequest));

        // Then
        Assertions.assertEquals(entityNotFoundException.getMessage(), ErrorMessage.ADMIN_NOT_FOUND);

        Admin editedAdmin = this.adminRepo.findAdminByUser_Username("test.user").get();
        Assertions.assertEquals(editedAdmin.getUser().getFirstname(), this.admin.getUser().getFirstname());
        Assertions.assertEquals(editedAdmin.getUser().getSurname(), this.admin.getUser().getSurname());
    }

    @Test
    public void testGetAdmin() throws EntityNotFoundException {
        // Given
        this.admin = this.adminRepo.findAdminByUser_Username(this.admin.getUser().getUsername()).get();
        Long id = this.admin.getAdminId();

        // When
        AdminResponse response = this.adminCreationService.getAdmin(id);

        // Then
        Assertions.assertEquals(response.getAdminId(), this.admin.getAdminId());
        Assertions.assertEquals(response.getFirstname(), this.admin.getUser().getFirstname());
        Assertions.assertEquals(response.getSurname(), this.admin.getUser().getSurname());
        Assertions.assertEquals(response.getUsername(), this.admin.getUser().getUsername());
        Assertions.assertEquals(response.getGeneratedPassword(), this.admin.getUser().getGeneratedPassword());
    }

    @Test
    public void testGetAdminWrongId() {
        // Given
        this.admin = this.adminRepo.findAdminByUser_Username(this.admin.getUser().getUsername()).get();
        Long id = this.admin.getAdminId() + 1;

        // When
        EntityNotFoundException entityNotFoundException = Assert.assertThrows(EntityNotFoundException.class, () ->
                this.adminCreationService.getAdmin(id));

        // Then
        Assertions.assertEquals(entityNotFoundException.getMessage(), ErrorMessage.ADMIN_NOT_FOUND);
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
