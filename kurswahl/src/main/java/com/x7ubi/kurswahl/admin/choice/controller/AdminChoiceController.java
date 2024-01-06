package com.x7ubi.kurswahl.admin.choice.controller;

import com.x7ubi.kurswahl.admin.authentication.AdminRequired;
import com.x7ubi.kurswahl.admin.choice.response.ChoiceSurveillanceResponse;
import com.x7ubi.kurswahl.admin.choice.service.ChoiceSurveillanceService;
import com.x7ubi.kurswahl.common.error.ErrorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminChoiceController {

    private final Logger logger = LoggerFactory.getLogger(AdminChoiceController.class);

    private final ChoiceSurveillanceService choiceSurveillanceService;

    public AdminChoiceController(ChoiceSurveillanceService choiceSurveillanceService) {
        this.choiceSurveillanceService = choiceSurveillanceService;
    }

    @GetMapping("/choiceSurveillance")
    @AdminRequired
    public ResponseEntity<?> getChoiceSurveillanceForStudents() {
        logger.info("Choice Surveillance For Students");

        try {
            List<ChoiceSurveillanceResponse> responses = this.choiceSurveillanceService.getChoiceSurveillanceForStudents();
            return ResponseEntity.status(HttpStatus.OK).body(responses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.INTERNAL_SERVER_ERROR);
        }
    }
}
