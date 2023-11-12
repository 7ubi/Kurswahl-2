package com.x7ubi.kurswahl.service.admin.user;

import com.x7ubi.kurswahl.mapper.AdminMapper;
import com.x7ubi.kurswahl.models.Admin;
import com.x7ubi.kurswahl.models.User;
import com.x7ubi.kurswahl.repository.AdminRepo;
import com.x7ubi.kurswahl.repository.UserRepo;
import com.x7ubi.kurswahl.request.admin.AdminSignupRequest;
import com.x7ubi.kurswahl.response.admin.user.AdminResponses;
import com.x7ubi.kurswahl.response.admin.user.AdminResultResponse;
import com.x7ubi.kurswahl.response.common.ResultResponse;
import com.x7ubi.kurswahl.service.admin.AdminErrorService;
import com.x7ubi.kurswahl.utils.PasswordGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminCreationService {

    private final Logger logger = LoggerFactory.getLogger(AdminCreationService.class);

    private final AdminErrorService adminErrorService;

    private final AdminRepo adminRepo;

    private final UserRepo userRepo;

    private final PasswordEncoder passwordEncoder;

    private final UsernameService usernameService;

    private final AdminMapper adminMapper;

    protected AdminCreationService(AdminErrorService adminErrorService, AdminRepo adminRepo, UserRepo userRepo,
                                   PasswordEncoder passwordEncoder, UsernameService usernameService,
                                   AdminMapper adminMapper) {
        this.adminErrorService = adminErrorService;
        this.adminRepo = adminRepo;
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.usernameService = usernameService;
        this.adminMapper = adminMapper;
    }

    public void registerAdmin(AdminSignupRequest signupRequest) {

        Admin admin = this.adminMapper.adminRequestToAdmin(signupRequest);
        admin.getUser().setUsername(this.usernameService.generateUsernameFromName(signupRequest));
        admin.getUser().setGeneratedPassword(PasswordGenerator.generatePassword());
        admin.getUser().setPassword(passwordEncoder.encode(admin.getUser().getGeneratedPassword()));

        this.adminRepo.save(admin);

        logger.info(String.format("Admin %s was created", admin.getUser().getUsername()));
    }

    public AdminResponses getAllAdmins() {
        List<Admin> admins = this.adminRepo.findAll();

        return this.adminMapper.adminsToAdminResponses(admins);
    }

    public ResultResponse editAdmin(Long adminId, AdminSignupRequest signupRequest) {
        ResultResponse resultResponse = new ResultResponse();

        resultResponse.setErrorMessages(this.adminErrorService.getAdminNotFound(adminId));

        if (!resultResponse.getErrorMessages().isEmpty()) {
            return resultResponse;
        }

        Admin admin = this.adminRepo.findAdminByAdminId(adminId).get();

        this.adminMapper.adminRequestToAdmin(signupRequest, admin);

        this.adminRepo.save(admin);

        return resultResponse;
    }

    public ResultResponse deleteAdmin(Long adminId) {
        ResultResponse resultResponse = new ResultResponse();

        resultResponse.setErrorMessages(this.adminErrorService.getAdminNotFound(adminId));

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

    public AdminResultResponse getAdmin(Long adminId) {
        AdminResultResponse adminResultResponse = new AdminResultResponse();

        adminResultResponse.setErrorMessages(this.adminErrorService.getAdminNotFound(adminId));

        if (!adminResultResponse.getErrorMessages().isEmpty()) {
            return adminResultResponse;
        }

        Admin admin = this.adminRepo.findAdminByAdminId(adminId).get();
        adminResultResponse.setAdminResponse(this.adminMapper.adminToAdminResponse(admin));

        return adminResultResponse;
    }
}
