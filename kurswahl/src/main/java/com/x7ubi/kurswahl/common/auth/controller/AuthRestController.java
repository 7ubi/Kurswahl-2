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
import com.x7ubi.kurswahl.common.exception.EntityNotFoundException;
import com.x7ubi.kurswahl.common.exception.PasswordNotMatchingException;
import com.x7ubi.kurswahl.common.jwt.JwtUtils;
import com.x7ubi.kurswahl.common.models.SecurityUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
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
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(description = "Creating Standard Admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created Standard Admin. The username ist admin. The Password can be found in the logs.")
    })
    public ResponseEntity<?> createStandardAdmin() {
        logger.info("Creating standard Admin");

        standardAdminService.createStandardAdmin();

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Logging in User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User was logged in.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = JwtResponse.class))
            }),
            @ApiResponse(responseCode = "401", description = "Bad credentials. User was not logged in", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
            }),
    })
    public ResponseEntity<?> login(
            @RequestBody LoginRequest loginRequest
    ) {
        logger.info(String.format("Logging in %s", loginRequest.getUsername()));

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
    }

    @PutMapping("/changePassword")
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Changing Password of User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Changed Password of User."),
            @ApiResponse(responseCode = "400", description = "Old Password was not correct", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
            }),
            @ApiResponse(responseCode = "404", description = "User could not be found.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
            })
    })
    public ResponseEntity<?> changePassword(
            @RequestHeader("Authorization") String authorization,
            @RequestBody ChangePasswordRequest changePasswordRequest
    ) throws EntityNotFoundException, PasswordNotMatchingException {

        logger.info("Changing Password");

        String username = jwtUtils.getUsernameFromAuthorizationHeader(authorization);
        this.changePasswordService.changePassword(username, changePasswordRequest);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/resetPassword")
    @AdminRequired
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Resetting Password of User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reset Password of User."),
            @ApiResponse(responseCode = "404", description = "User could not be found.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
            })
    })
    public ResponseEntity<?> resetPassword(@RequestBody PasswordResetRequest passwordResetRequest) throws EntityNotFoundException {
        logger.info("Resetting Password");

        this.changePasswordService.resetPassword(passwordResetRequest);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/resetPasswords")
    @AdminRequired
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Resetting Password of User List")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reset Password of Users."),
            @ApiResponse(responseCode = "404", description = "User could not be found.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
            })
    })
    public ResponseEntity<?> resetPasswords(@RequestBody List<PasswordResetRequest> passwordResetRequests) throws EntityNotFoundException {
        logger.info("Resetting Passwords");

        this.changePasswordService.resetPasswords(passwordResetRequests);

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
