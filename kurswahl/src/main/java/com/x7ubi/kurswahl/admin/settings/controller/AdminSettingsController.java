package com.x7ubi.kurswahl.admin.settings.controller;

import com.x7ubi.kurswahl.admin.authentication.AdminRequired;
import com.x7ubi.kurswahl.admin.settings.request.EditSettingsRequest;
import com.x7ubi.kurswahl.admin.settings.response.SettingsResponse;
import com.x7ubi.kurswahl.admin.settings.service.AdminSettingsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminSettingsController {

    Logger logger = LoggerFactory.getLogger(AdminSettingsController.class);

    private final AdminSettingsService adminSettingsService;

    public AdminSettingsController(AdminSettingsService adminSettingsService) {
        this.adminSettingsService = adminSettingsService;
    }

    @GetMapping("/settings")
    @AdminRequired
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Getting Settings")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found Settings", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = SettingsResponse.class))
            }),
    })
    public ResponseEntity<SettingsResponse> getClassSizeSetting() {
        logger.info("Getting settings");

        SettingsResponse responses = this.adminSettingsService.getSettings();
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @PutMapping("/settings")
    @AdminRequired
    @Operation(description = "Editing Settings")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Edited Settings", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = SettingsResponse.class))
            }),
    })
    public ResponseEntity<SettingsResponse> editClassSizeSetting(@RequestBody EditSettingsRequest editSettingsRequest) {
        logger.info("Editing settings");

        SettingsResponse responses = this.adminSettingsService.editSettings(editSettingsRequest);
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }
}
