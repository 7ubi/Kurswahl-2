package com.x7ubi.kurswahl.common.auth.controller;

import com.x7ubi.kurswahl.admin.authentication.AdminRequired;
import com.x7ubi.kurswahl.common.auth.request.ChangePasswordRequest;
import com.x7ubi.kurswahl.common.auth.request.LoginRequest;
import com.x7ubi.kurswahl.common.auth.request.PasswordResetRequest;
import com.x7ubi.kurswahl.common.auth.response.JwtResponse;
import com.x7ubi.kurswahl.common.auth.response.Role;
import com.x7ubi.kurswahl.common.auth.service.ChangePasswordService;
import com.x7ubi.kurswahl.common.auth.service.LoginHelperService;
import com.x7ubi.kurswahl.common.auth.service.StandardAdminService;
import com.x7ubi.kurswahl.common.error.ErrorMessage;
import com.x7ubi.kurswahl.common.exception.EntityNotFoundException;
import com.x7ubi.kurswahl.common.exception.PasswordNotMatchingException;
import com.x7ubi.kurswahl.common.jwt.JwtUtils;
import com.x7ubi.kurswahl.common.models.SecurityUser;
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

import java.util.List;


@RestController
@RequestMapping("/api/auth")
public class AuthRestController {

    Logger logger = LoggerFactory.getLogger(AuthRestController.class);

    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;

    private final StandardAdminService standardAdminService;

    private final ChangePasswordService changePasswordService;

    private final LoginHelperService loginHelperService;

    public AuthRestController(AuthenticationManager authenticationManager, JwtUtils jwtUtils,
                              StandardAdminService standardAdminService, ChangePasswordService changePasswordService,
                              LoginHelperService loginHelperService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.standardAdminService = standardAdminService;
        this.changePasswordService = changePasswordService;
        this.loginHelperService = loginHelperService;
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

            Role role = this.loginHelperService.getRoleUser(userDetails.getUsername());

            boolean changedPassword = this.loginHelperService.getChangedPassword(userDetails.getUser());

            String name = String.format("%s %s", userDetails.getUser().getFirstname(),
                    userDetails.getUser().getSurname());
            JwtResponse jwtResponse = new JwtResponse(jwt, userDetails.getUser().getUserId(), userDetails.getUsername(),
                    role, name, changedPassword);
            return ResponseEntity.status(HttpStatus.OK).body(jwtResponse);
        } catch (BadCredentialsException e) {
            logger.error(String.valueOf(e));
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorMessage.UNAUTHORIZED);
        } catch (Exception e) {
            logger.error(String.valueOf(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.INTERNAL_SERVER_ERROR);
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
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/resetPassword")
    @AdminRequired
    public ResponseEntity<?> resetPassword(@RequestBody PasswordResetRequest passwordResetRequest) {
        logger.info("Resetting Password");

        try {
            this.changePasswordService.resetPassword(passwordResetRequest);

            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (EntityNotFoundException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/resetPasswords")
    @AdminRequired
    public ResponseEntity<?> resetPasswords(@RequestBody List<PasswordResetRequest> passwordResetRequests) {
        logger.info("Resetting Password");

        try {
            this.changePasswordService.resetPasswords(passwordResetRequests);

            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (EntityNotFoundException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.INTERNAL_SERVER_ERROR);
        }
    }
}
