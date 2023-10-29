package com.x7ubi.kurswahl.controller;

import com.x7ubi.kurswahl.jwt.JwtUtils;
import com.x7ubi.kurswahl.models.SecurityUser;
import com.x7ubi.kurswahl.repository.AdminRepo;
import com.x7ubi.kurswahl.repository.StudentRepo;
import com.x7ubi.kurswahl.repository.TeacherRepo;
import com.x7ubi.kurswahl.request.auth.LoginRequest;
import com.x7ubi.kurswahl.response.common.JwtResponse;
import com.x7ubi.kurswahl.response.common.Role;
import com.x7ubi.kurswahl.service.authentication.StandardAdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/auth")
public class AuthRestController {

    Logger logger = LoggerFactory.getLogger(AuthRestController.class);

    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;

    private final AdminRepo adminRepo;

    private final TeacherRepo teacherRepo;

    private final StudentRepo studentRepo;

    private final StandardAdminService standardAdminService;

    @Value("${password}")
    private String standardPassword;

    public AuthRestController(AuthenticationManager authenticationManager, JwtUtils jwtUtils, AdminRepo adminRepo,
                              TeacherRepo teacherRepo, StudentRepo studentRepo,
                              StandardAdminService standardAdminService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.adminRepo = adminRepo;
        this.teacherRepo = teacherRepo;
        this.studentRepo = studentRepo;
        this.standardAdminService = standardAdminService;
    }

    @PostMapping("/standardAdmin")
    public ResponseEntity<?> createStandardAdmin() {
        logger.info("Creating standard Admin");

        standardAdminService.createStandardAdmin(standardPassword);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
        @RequestBody LoginRequest loginRequest
    ) {
        logger.info(String.format("Logging in %s", loginRequest.getUsername()));

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);

            SecurityUser userDetails = (SecurityUser) authentication.getPrincipal();

            Role role =  getRoleUser(userDetails.getUsername());

            return ResponseEntity.ok(new JwtResponse(jwt,
                    userDetails.getUser().getId(),
                    userDetails.getUsername(), role));
        } catch (Exception e) {
            logger.error(String.valueOf(e));
        }
        return (ResponseEntity<?>) ResponseEntity.badRequest();
    }

    private Role getRoleUser(String username) {
        if(adminRepo.existsAdminByUser_Username(username)) {
            return Role.ADMIN;
        }

        if(studentRepo.existsStudentByUser_Username(username)) {
            return Role.STUDENT;
        }

        if(teacherRepo.existsTeacherByUser_Username(username)) {
            return Role.TEACHER;
        }

        return Role.NOROLE;
    }
}
