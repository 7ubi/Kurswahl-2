package com.x7ubi.kurswahl.service.authentication;

import com.x7ubi.kurswahl.models.Admin;
import com.x7ubi.kurswahl.models.User;
import com.x7ubi.kurswahl.repository.AdminRepo;
import com.x7ubi.kurswahl.repository.UserRepo;
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

    public void createStandardAdmin(String standardPassword) {
        String username = "admin";
        if (userRepo.existsByUsername(username)) {
            logger.warn("Standard Admin already exists, creating again");

            Admin formerAdmin = adminRepo.findAdminByUser_Username(username).get();
            adminRepo.delete(formerAdmin);
            userRepo.delete(formerAdmin.getUser());
        }


        Admin admin = new Admin();
        User user = new User();
        user.setUsername(username);
        user.setFirstname("Admin");
        user.setSurname("Admin");
        user.setPassword(passwordEncoder.encode(standardPassword));
        admin.setUser(user);

        userRepo.save(user);
        adminRepo.save(admin);

        logger.info(adminRepo.existsAdminByUser_Username(username).toString());
    }
}
