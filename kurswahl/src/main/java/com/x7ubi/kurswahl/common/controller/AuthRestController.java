package com.x7ubi.kurswahl.common.controller;

import com.x7ubi.kurswahl.common.error.ErrorMessage;
import com.x7ubi.kurswahl.common.exception.EntityNotFoundException;
import com.x7ubi.kurswahl.common.exception.PasswordNotMatchingException;
import com.x7ubi.kurswahl.common.request.PasswordResetRequest;
import com.x7ubi.kurswahl.admin.authentication.AdminRequired;
import com.x7ubi.kurswahl.common.jwt.JwtUtils;
import com.x7ubi.kurswahl.common.models.SecurityUser;
import com.x7ubi.kurswahl.common.repository.AdminRepo;
import com.x7ubi.kurswahl.common.repository.StudentRepo;
import com.x7ubi.kurswahl.common.repository.TeacherRepo;
import com.x7ubi.kurswahl.common.request.ChangePasswordRequest;
import com.x7ubi.kurswahl.common.request.LoginRequest;
import com.x7ubi.kurswahl.common.response.JwtResponse;
import com.x7ubi.kurswahl.common.response.Role;
import com.x7ubi.kurswahl.common.service.ChangePasswordService;
import com.x7ubi.kurswahl.common.service.StandardAdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
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
            JwtResponse jwtResponse = new JwtResponse(jwt, userDetails.getUser().getUserId(), userDetails.getUsername(),
                    role, name);
            return ResponseEntity.status(HttpStatus.OK).body(jwtResponse);
        } catch (BadCredentialsException e) {
            logger.error(String.valueOf(e));
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorMessage.Common.UNAUTHORIZED);
        } catch (Exception e) {
            logger.error(String.valueOf(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.Common.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/changePassword")
    public ResponseEntity<?> changePassword(
            @RequestHeader("Authorization") String authorization,
            @RequestBody ChangePasswordRequest changePasswordRequest
    ) {

        logger.info("Changing Password");

        try {
            String username = jwtUtils.getUsernameFromAuthorizationHeader(authorization);
            this.changePasswordService.changePassword(username, changePasswordRequest);

            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (EntityNotFoundException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (PasswordNotMatchingException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.Common.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/resetPassword")
    @AdminRequired
    public ResponseEntity<?> resetPassword(@RequestBody PasswordResetRequest passwordResetRequest) {
        logger.info("Reseting Password");

        try {
            this.changePasswordService.resetPassword(passwordResetRequest);

            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (EntityNotFoundException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.Common.INTERNAL_SERVER_ERROR);
        }
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
