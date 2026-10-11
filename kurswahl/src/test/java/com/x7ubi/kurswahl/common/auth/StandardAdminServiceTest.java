package com.x7ubi.kurswahl.common.auth;

import com.x7ubi.kurswahl.KurswahlServiceTest;
import com.x7ubi.kurswahl.common.auth.service.StandardAdminService;
import com.x7ubi.kurswahl.common.error.ErrorMessage;
import com.x7ubi.kurswahl.common.exception.EntityCreationException;
import com.x7ubi.kurswahl.common.models.Admin;
import com.x7ubi.kurswahl.common.models.User;
import com.x7ubi.kurswahl.common.repository.AdminRepo;
import com.x7ubi.kurswahl.common.repository.UserRepo;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@KurswahlServiceTest
public class StandardAdminServiceTest {

    @Autowired
    private StandardAdminService standardAdminService;

    @Autowired
    private AdminRepo adminRepo;

    @Autowired
    private UserRepo userRepo;

    private void setupAdmin(String username) {
        User user = new User();
        user.setUsername(username);
        user.setFirstname("Test");
        user.setSurname("Admin");
        user.setPassword("Password");
        Admin admin = new Admin();
        admin.setUser(user);
        this.adminRepo.save(admin);
    }

    @Test
    public void testCreateStandardAdmin() throws EntityCreationException {
        // When
        this.standardAdminService.createStandardAdmin();

        // Then
        Assertions.assertTrue(this.adminRepo.existsAdminByUser_Username("admin"));
    }

    @Test
    public void testCreateStandardAdminAdminExists() {
        // Given
        setupAdmin("admin");
        String password = this.userRepo.findByUsername("admin").get().getPassword();

        // When
        EntityCreationException exception = Assert.assertThrows(EntityCreationException.class, () ->
                this.standardAdminService.createStandardAdmin());

        // Then
        Assertions.assertEquals(exception.getMessage(), ErrorMessage.STANDARD_ADMIN_NOT_ALLOWED);
        Assertions.assertEquals(this.adminRepo.count(), 1);
        Assertions.assertEquals(this.userRepo.findByUsername("admin").get().getPassword(), password);
    }

    @Test
    public void testCreateStandardAdminOtherAdminExists() {
        // Given
        setupAdmin("other.admin");

        // When
        EntityCreationException exception = Assert.assertThrows(EntityCreationException.class, () ->
                this.standardAdminService.createStandardAdmin());

        // Then
        Assertions.assertEquals(exception.getMessage(), ErrorMessage.STANDARD_ADMIN_NOT_ALLOWED);
        Assertions.assertFalse(this.userRepo.existsByUsername("admin"));
    }

    @Test
    public void testCreateStandardAdminUsernameTaken() {
        // Given
        User user = new User();
        user.setUsername("admin");
        user.setFirstname("Not");
        user.setSurname("Admin");
        user.setPassword("Password");
        this.userRepo.save(user);

        // When
        EntityCreationException exception = Assert.assertThrows(EntityCreationException.class, () ->
                this.standardAdminService.createStandardAdmin());

        // Then
        Assertions.assertEquals(exception.getMessage(), ErrorMessage.STANDARD_ADMIN_NOT_ALLOWED);
        Assertions.assertEquals(this.adminRepo.count(), 0);
        Assertions.assertTrue(this.userRepo.existsByUsername("admin"));
    }
}
