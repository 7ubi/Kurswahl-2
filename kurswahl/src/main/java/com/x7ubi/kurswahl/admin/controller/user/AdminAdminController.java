package com.x7ubi.kurswahl.admin.controller.user;

import com.x7ubi.kurswahl.admin.request.AdminSignupRequest;
import com.x7ubi.kurswahl.admin.response.user.AdminResponse;
import com.x7ubi.kurswahl.admin.response.user.AdminResponses;
import com.x7ubi.kurswahl.admin.service.authentication.AdminRequired;
import com.x7ubi.kurswahl.admin.service.user.AdminCreationService;
import com.x7ubi.kurswahl.common.error.ErrorMessage;
import com.x7ubi.kurswahl.common.exception.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminAdminController {

    Logger logger = LoggerFactory.getLogger(AdminAdminController.class);

    private final AdminCreationService adminCreationService;

    public AdminAdminController(AdminCreationService adminCreationService) {
        this.adminCreationService = adminCreationService;
    }

    @PostMapping("/admin")
    @AdminRequired
    public ResponseEntity<?> createAdmin(
            @RequestBody AdminSignupRequest signupRequest
    ) {
        logger.info("Signing up new Admin");
        try {
            this.adminCreationService.registerAdmin(signupRequest);

            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.Common.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/admin")
    @AdminRequired
    public ResponseEntity<?> editAdmin(
            @RequestParam Long adminId,
            @RequestBody AdminSignupRequest signupRequest
    ) {
        logger.info("Editing Admin");
        try {
            this.adminCreationService.editAdmin(adminId, signupRequest);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (EntityNotFoundException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.Common.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/admin")
    @AdminRequired
    public ResponseEntity<?> getAdmin(
            @RequestParam Long adminId
    ) {
        logger.info("Getting Admin");
        try {
            AdminResponse response = this.adminCreationService.getAdmin(adminId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (EntityNotFoundException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.Common.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/admin")
    @AdminRequired
    public ResponseEntity<?> deleteAdmin(
            @RequestParam Long adminId
    ) {
        logger.info("Deleting Admin");

        try {
            this.adminCreationService.deleteAdmin(adminId);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (EntityNotFoundException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.Common.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/admins")
    @AdminRequired
    public ResponseEntity<?> getAdmins() {
        logger.info("Getting all Admins");
        try {
            AdminResponses adminResponses = this.adminCreationService.getAllAdmins();

            return ResponseEntity.status(HttpStatus.OK).body(adminResponses);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.Common.INTERNAL_SERVER_ERROR);
        }
    }

}
