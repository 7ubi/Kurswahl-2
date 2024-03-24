package com.x7ubi.kurswahl.admin.user.controller;

import com.x7ubi.kurswahl.admin.authentication.AdminRequired;
import com.x7ubi.kurswahl.admin.user.request.AdminSignupRequest;
import com.x7ubi.kurswahl.admin.user.response.AdminResponse;
import com.x7ubi.kurswahl.admin.user.service.AdminCreationService;
import com.x7ubi.kurswahl.common.exception.EntityNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(description = "Signs up new Admin")
    public ResponseEntity<?> createAdmin(
            @RequestBody AdminSignupRequest signupRequest
    ) {
        logger.info("Signing up new Admin");
        this.adminCreationService.registerAdmin(signupRequest);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/admin")
    @AdminRequired
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Edit Admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Admin could not be found.", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = String.class))})
    })
    public ResponseEntity<?> editAdmin(
            @RequestParam Long adminId,
            @RequestBody AdminSignupRequest signupRequest
    ) throws EntityNotFoundException {
        logger.info("Editing Admin");
        this.adminCreationService.editAdmin(adminId, signupRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/admin")
    @AdminRequired
    @ResponseStatus(HttpStatus.FOUND)
    @Operation(description = "Gets Admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "FOUND", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = AdminResponse.class), examples = @ExampleObject)}),
            @ApiResponse(responseCode = "404", description = "Admin could not be found.", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = String.class))})
    })
    public ResponseEntity<?> getAdmin(
            @RequestParam Long adminId
    ) throws EntityNotFoundException {
        logger.info("Getting Admin");
        AdminResponse response = this.adminCreationService.getAdmin(adminId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/admin")
    @AdminRequired
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> deleteAdmin(
            @RequestParam Long adminId
    ) throws EntityNotFoundException {
        logger.info("Deleting Admin");

        List<AdminResponse> adminResponses = this.adminCreationService.deleteAdmin(adminId);
        return ResponseEntity.status(HttpStatus.OK).body(adminResponses);
    }

    @GetMapping("/admins")
    @AdminRequired
    @ResponseStatus(HttpStatus.FOUND)
    public ResponseEntity<?> getAdmins() {
        logger.info("Getting all Admins");
        List<AdminResponse> adminResponses = this.adminCreationService.getAllAdmins();

        return ResponseEntity.status(HttpStatus.OK).body(adminResponses);
    }

    @DeleteMapping("/admins")
    @AdminRequired
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> deleteAdmins(@RequestBody List<Long> adminIds) throws EntityNotFoundException {
        logger.info("Deleting Admins");
        List<AdminResponse> adminResponses = this.adminCreationService.deleteAdmins(adminIds);

        return ResponseEntity.status(HttpStatus.OK).body(adminResponses);
    }
}
