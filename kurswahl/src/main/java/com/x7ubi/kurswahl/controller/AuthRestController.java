package com.x7ubi.kurswahl.controller;

import com.x7ubi.kurswahl.jwt.JwtUtils;
import com.x7ubi.kurswahl.models.SecurityUser;
import com.x7ubi.kurswahl.request.auth.LoginRequest;
import com.x7ubi.kurswahl.request.auth.SignupRequest;
import com.x7ubi.kurswahl.response.common.JwtResponse;
import com.x7ubi.kurswahl.response.common.ResultResponse;
import com.x7ubi.kurswahl.service.authentication.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final AuthService authService;

    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;

    public AuthRestController(AuthService authService, AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(
        @RequestBody SignupRequest signupRequest
    ) {
        logger.info("Signing up new account");

        ResultResponse response = authService.registerNewUserAccount(signupRequest);

        if(response.isSuccess()) {
            return ResponseEntity.ok().body(response);
        }

        return ResponseEntity.badRequest().body(response);
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

            return ResponseEntity.ok(new JwtResponse(jwt,
                    userDetails.getUser().getId(),
                    userDetails.getUsername()));
        } catch (Exception e) {
            logger.error(String.valueOf(e));
        }
        return (ResponseEntity<?>) ResponseEntity.badRequest();
    }
}
