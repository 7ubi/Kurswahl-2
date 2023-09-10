package com.x7ubi.kurswahl.service.admin;

import com.x7ubi.kurswahl.models.Admin;
import com.x7ubi.kurswahl.models.User;
import com.x7ubi.kurswahl.repository.AdminRepo;
import com.x7ubi.kurswahl.repository.UserRepo;
import com.x7ubi.kurswahl.request.admin.AdminSignupRequest;
import com.x7ubi.kurswahl.response.admin.AdminResponse;
import com.x7ubi.kurswahl.response.admin.AdminResponses;
import com.x7ubi.kurswahl.response.common.ResultResponse;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class AdminCreationService extends AbstractUserCreationService {

    private final Logger logger = LoggerFactory.getLogger(AdminCreationService.class);
    private final AdminRepo adminRepo;
    private final ModelMapper mapper = new ModelMapper();
    private final PasswordEncoder passwordEncoder;

    protected AdminCreationService(UserRepo userRepo, AdminRepo adminRepo, PasswordEncoder passwordEncoder) {
        super(userRepo);
        this.adminRepo = adminRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public ResultResponse registerAdmin(AdminSignupRequest signupRequest) {
        ResultResponse resultResponse = new ResultResponse();

        resultResponse.setErrorMessages(this.findRegisterErrors(signupRequest));

        if(!resultResponse.getErrorMessages().isEmpty()) {
            return resultResponse;
        }

        Admin admin = new Admin();
        admin.setUser(this.mapper.map(signupRequest, User.class));
        admin.getUser().setGeneratedPassword(this.generatePassword());
        admin.getUser().setPassword(passwordEncoder.encode(admin.getUser().getGeneratedPassword()));

        this.adminRepo.save(admin);

        logger.info(String.format("Admin %s was created", admin.getUser().getUsername()));

        return resultResponse;
    }

    public AdminResponses getAllAdmins() {
        AdminResponses adminResultResponses = new AdminResponses();
        adminResultResponses.setAdminResponses(new ArrayList<>());

        List<Admin> admins = this.adminRepo.findAll();
        for(Admin admin: admins) {
            AdminResponse adminResponse = this.mapper.map(admin.getUser(), AdminResponse.class);
            adminResponse.setAdminId(admin.getAdminId());
            adminResultResponses.getAdminResponses().add(adminResponse);
        }

        return adminResultResponses;
    }

    private String generatePassword() {
        int length = 12;
        StringBuilder password = new StringBuilder();
        Random random = new Random(System.nanoTime());
        final String lowerLetters = "abcdefghikmnpqrstuvwxyz";
        final String upperLetters = "ABCDEFGHJKLMNOPQRSTUVWXYZ";
        final String digits = "0123456789";
        final String extraCharacters = "!#$%";

        // Collect the categories to use.
        List<String> charCategories = new ArrayList<>(List.of(lowerLetters, upperLetters, digits, extraCharacters));

        // Build the password.
        for (int i = 0; i < length; i++) {
            String charCategory = charCategories.get(random.nextInt(charCategories.size()));
            int position = random.nextInt(charCategory.length());
            password.append(charCategory.charAt(position));
        }
        return new String(password);
    }
}
