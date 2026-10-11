package com.x7ubi.kurswahl.common.auth.service;

import com.x7ubi.kurswahl.common.auth.utils.PasswordGenerator;
import com.x7ubi.kurswahl.common.error.ErrorMessage;
import com.x7ubi.kurswahl.common.exception.EntityCreationException;
import com.x7ubi.kurswahl.common.models.Admin;
import com.x7ubi.kurswahl.common.models.User;
import com.x7ubi.kurswahl.common.repository.AdminRepo;
import com.x7ubi.kurswahl.common.repository.UserRepo;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class StandardAdminService {

    Logger logger = LoggerFactory.getLogger(StandardAdminService.class);

    private final UserRepo userRepo;

    private final AdminRepo adminRepo;

    private final PasswordEncoder passwordEncoder;

    public StandardAdminService(UserRepo userRepo, AdminRepo adminRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.adminRepo = adminRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void createStandardAdmin() throws EntityCreationException {
        String username = "admin";
        if (adminRepo.count() > 0 || userRepo.existsByUsername(username)) {
            logger.warn("Refusing to create Standard Admin, because an admin or a user named admin already exists");
            throw new EntityCreationException(ErrorMessage.STANDARD_ADMIN_NOT_ALLOWED);
        }

        String password = PasswordGenerator.generatePassword();

        Admin admin = new Admin();
        User user = new User();
        user.setUsername(username);
        user.setFirstname("Admin");
        user.setSurname("Admin");
        user.setGeneratedPassword(password);
        user.setPassword(passwordEncoder.encode(password));
        admin.setUser(user);

        adminRepo.save(admin);

        logger.info(String.format("Standard Admin with password %s was created!", password));
    }
}
