package com.x7ubi.kurswahl.service.admin;

import com.x7ubi.kurswahl.error.ErrorMessage;
import com.x7ubi.kurswahl.repository.AdminRepo;
import com.x7ubi.kurswahl.repository.StudentRepo;
import com.x7ubi.kurswahl.repository.UserRepo;
import com.x7ubi.kurswahl.request.auth.SignupRequest;
import com.x7ubi.kurswahl.response.common.MessageResponse;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractUserCreationService {
    Logger logger = LoggerFactory.getLogger(AbstractUserCreationService.class);

    protected final UserRepo userRepo;

    protected final AdminRepo adminRepo;

    protected final StudentRepo studentRepo;

    protected final PasswordEncoder passwordEncoder;

    protected final ModelMapper mapper = new ModelMapper();

    protected AbstractUserCreationService(UserRepo userRepo, AdminRepo adminRepo, StudentRepo studentRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.adminRepo = adminRepo;
        this.studentRepo = studentRepo;
        this.passwordEncoder = passwordEncoder;
    }

    protected List<MessageResponse> findRegisterErrors(SignupRequest signupRequest) {
        List<MessageResponse> errors = new ArrayList<>();

        if(userRepo.existsByUsername(signupRequest.getUsername())){
            logger.error(ErrorMessage.Authentication.USERNAME_EXITS);
            errors.add(new MessageResponse(ErrorMessage.Authentication.USERNAME_EXITS));
        }

        return errors;
    }

    protected List<MessageResponse> getAdminNotFound(Long adminId) {
        List<MessageResponse> errors = new ArrayList<>();

        if(!adminRepo.existsAdminByAdminId(adminId)){
            logger.error(ErrorMessage.Administration.ADMIN_NOT_FOUND);
            errors.add(new MessageResponse(ErrorMessage.Administration.ADMIN_NOT_FOUND));
        }

        return errors;
    }

    protected List<MessageResponse> getStudentNotFound(Long studentId) {

        List<MessageResponse> errors = new ArrayList<>();

        if(!studentRepo.existsStudentByStudentId(studentId)){
            logger.error(ErrorMessage.Administration.STUDENT_NOT_FOUND);
            errors.add(new MessageResponse(ErrorMessage.Administration.STUDENT_NOT_FOUND));
        }

        return errors;
    }
}
