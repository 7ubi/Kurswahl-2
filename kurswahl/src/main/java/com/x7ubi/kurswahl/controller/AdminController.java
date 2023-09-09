package com.x7ubi.kurswahl.controller;

import com.x7ubi.kurswahl.request.admin.AdminSignupRequest;
import com.x7ubi.kurswahl.response.common.ResultResponse;
import com.x7ubi.kurswahl.service.admin.AdminCreationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    Logger logger = LoggerFactory.getLogger(AdminController.class);

    private final AdminCreationService adminCreationService;

    public AdminController(AdminCreationService adminCreationService) {
        this.adminCreationService = adminCreationService;
    }

    @PostMapping("/admin")
    public ResponseEntity<?> createAdmin(
            @RequestBody AdminSignupRequest signupRequest
    ) {
        logger.info("Signing up new Admin");

        ResultResponse response = adminCreationService.registerAdmin(signupRequest);

        if(response.getErrorMessages().isEmpty()) {
            return ResponseEntity.ok().body(response);
        }

        return ResponseEntity.badRequest().body(response);
    }
}
