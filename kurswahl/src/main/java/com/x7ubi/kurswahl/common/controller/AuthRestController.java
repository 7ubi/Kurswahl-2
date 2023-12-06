package com.x7ubi.kurswahl.common.controller;

import com.x7ubi.kurswahl.admin.request.PasswordResetRequest;
import com.x7ubi.kurswahl.admin.service.authentication.AdminRequired;
import com.x7ubi.kurswahl.common.jwt.JwtUtils;
import com.x7ubi.kurswahl.common.models.SecurityUser;
import com.x7ubi.kurswahl.common.repository.AdminRepo;
import com.x7ubi.kurswahl.common.repository.StudentRepo;
import com.x7ubi.kurswahl.common.repository.TeacherRepo;
import com.x7ubi.kurswahl.common.request.ChangePasswordRequest;
import com.x7ubi.kurswahl.common.request.LoginRequest;
import com.x7ubi.kurswahl.common.response.JwtResponse;
import com.x7ubi.kurswahl.common.response.ResultResponse;
import com.x7ubi.kurswahl.common.response.Role;
import com.x7ubi.kurswahl.common.service.ChangePasswordService;
import com.x7ubi.kurswahl.common.service.StandardAdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


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

    private final ChangePasswordService changePasswordService;

    public AuthRestController(AuthenticationManager authenticationManager, JwtUtils jwtUtils, AdminRepo adminRepo,
                              TeacherRepo teacherRepo, StudentRepo studentRepo,
                              StandardAdminService standardAdminService, ChangePasswordService changePasswordService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.adminRepo = adminRepo;
        this.teacherRepo = teacherRepo;
        this.studentRepo = studentRepo;
        this.standardAdminService = standardAdminService;
        this.changePasswordService = changePasswordService;
    }

    @PostMapping("/standardAdmin")
    public ResponseEntity<?> createStandardAdmin() {
        logger.info("Creating standard Admin");

        standardAdminService.createStandardAdmin();

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

            String name = String.format("%s %s", userDetails.getUser().getFirstname(),
                    userDetails.getUser().getSurname());

            return ResponseEntity.ok(new JwtResponse(jwt,
                    userDetails.getUser().getUserId(),
                    userDetails.getUsername(), role, name));
        } catch (Exception e) {
            logger.error(String.valueOf(e));
        }
        return (ResponseEntity<?>) ResponseEntity.badRequest();
    }

    @PutMapping("/changePassword")
    public ResponseEntity<?> changePassword(
            @RequestHeader("Authorization") String authorization,
            @RequestBody ChangePasswordRequest changePasswordRequest
    ) {
        String username = jwtUtils.getUsernameFromAuthorizationHeader(authorization);

        logger.info("Changing Password");

        ResultResponse response = this.changePasswordService.changePassword(username, changePasswordRequest);

        if (!response.getErrorMessages().isEmpty()) {
            return ResponseEntity.badRequest().body(response);
        }

        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/resetPassword")
    @AdminRequired
    public ResponseEntity<?> resetPassword(@RequestBody PasswordResetRequest passwordResetRequest) {
        logger.info("Reseting Password");

        ResultResponse response = this.changePasswordService.resetPassword(passwordResetRequest);

        if (response.getErrorMessages().isEmpty()) {
            return ResponseEntity.ok().body(response);
        }

        return ResponseEntity.badRequest().body(response);
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
