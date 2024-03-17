package com.x7ubi.kurswahl.admin.settings.controller;

import com.x7ubi.kurswahl.admin.authentication.AdminRequired;
import com.x7ubi.kurswahl.admin.settings.response.ClassSizeSettingResponse;
import com.x7ubi.kurswahl.admin.settings.service.AdminSettingsService;
import com.x7ubi.kurswahl.common.error.ErrorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminSettingsController {

    Logger logger = LoggerFactory.getLogger(AdminSettingsController.class);

    private final AdminSettingsService adminSettingsService;

    public AdminSettingsController(AdminSettingsService adminSettingsService) {
        this.adminSettingsService = adminSettingsService;
    }

    @GetMapping("/classSize")
    @AdminRequired
    public ResponseEntity<?> getClassSizeSetting() {
        logger.info("Getting class size warning and critical size from settings");

        try {
            ClassSizeSettingResponse responses = this.adminSettingsService.getClassSizeSettings();
            return ResponseEntity.status(HttpStatus.OK).body(responses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.INTERNAL_SERVER_ERROR);
        }
    }
}
