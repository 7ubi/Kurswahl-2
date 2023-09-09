package com.x7ubi.kurswahl.service.admin;

import com.x7ubi.kurswahl.models.Admin;
import com.x7ubi.kurswahl.models.User;
import com.x7ubi.kurswahl.repository.AdminRepo;
import com.x7ubi.kurswahl.repository.UserRepo;
import com.x7ubi.kurswahl.request.admin.AdminSignupRequest;
import com.x7ubi.kurswahl.response.common.ResultResponse;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
        admin.getUser().setPassword(passwordEncoder.encode(signupRequest.getPassword()));

        this.adminRepo.save(admin);

        logger.info(String.format("Admin %s was created", admin.getUser().getUsername()));

        return resultResponse;
    }
}
