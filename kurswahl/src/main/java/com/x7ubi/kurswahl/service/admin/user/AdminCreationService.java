package com.x7ubi.kurswahl.service.admin.user;

import com.x7ubi.kurswahl.models.Admin;
import com.x7ubi.kurswahl.models.User;
import com.x7ubi.kurswahl.repository.AdminRepo;
import com.x7ubi.kurswahl.repository.StudentRepo;
import com.x7ubi.kurswahl.repository.TeacherRepo;
import com.x7ubi.kurswahl.repository.UserRepo;
import com.x7ubi.kurswahl.request.admin.AdminSignupRequest;
import com.x7ubi.kurswahl.response.admin.AdminResponse;
import com.x7ubi.kurswahl.response.admin.AdminResponses;
import com.x7ubi.kurswahl.response.common.ResultResponse;
import com.x7ubi.kurswahl.utils.PasswordGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminCreationService extends AbstractUserCreationService {

    private final Logger logger = LoggerFactory.getLogger(AdminCreationService.class);

    protected AdminCreationService(UserRepo userRepo, AdminRepo adminRepo, StudentRepo studentRepo,
                                   TeacherRepo teacherRepo, PasswordEncoder passwordEncoder) {
        super(userRepo, adminRepo, studentRepo, teacherRepo, passwordEncoder);
    }

    public ResultResponse registerAdmin(AdminSignupRequest signupRequest) {
        ResultResponse resultResponse = new ResultResponse();

        resultResponse.setErrorMessages(this.findRegisterErrors(signupRequest));

        if(!resultResponse.getErrorMessages().isEmpty()) {
            return resultResponse;
        }

        Admin admin = new Admin();
        admin.setUser(this.mapper.map(signupRequest, User.class));
        admin.getUser().setGeneratedPassword(PasswordGenerator.generatePassword());
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

    public ResultResponse deleteAdmin(Long adminId) {
        ResultResponse resultResponse = new ResultResponse();

        resultResponse.setErrorMessages(this.getAdminNotFound(adminId));

        if(!resultResponse.getErrorMessages().isEmpty()) {
            return resultResponse;
        }

        Admin admin = this.adminRepo.findAdminByAdminId(adminId).get();
        User adminUser = admin.getUser();

        logger.info(String.format("Deleted Admin %s", adminUser.getUsername()));

        this.adminRepo.delete(admin);
        this.userRepo.delete(adminUser);

        return resultResponse;
    }
}
