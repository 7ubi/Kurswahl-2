package com.x7ubi.kurswahl.admin.choice.controller;

import com.x7ubi.kurswahl.admin.authentication.AdminRequired;
import com.x7ubi.kurswahl.admin.choice.response.ChoiceSurveillanceResponse;
import com.x7ubi.kurswahl.admin.choice.service.ChoiceSurveillanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminChoiceSurveillanceController {

    private final Logger logger = LoggerFactory.getLogger(AdminChoiceSurveillanceController.class);

    private final ChoiceSurveillanceService choiceSurveillanceService;

    public AdminChoiceSurveillanceController(ChoiceSurveillanceService choiceSurveillanceService) {
        this.choiceSurveillanceService = choiceSurveillanceService;
    }

    @GetMapping("/choiceSurveillance")
    @AdminRequired
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Get choice surveillance")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Got if students have chosen and fulfilled the rules.", content =
                    {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ChoiceSurveillanceResponse.class)))})
    })
    public ResponseEntity<List<ChoiceSurveillanceResponse>> getChoiceSurveillanceForStudents() {
        logger.info("Choice Surveillance For Students");

        List<ChoiceSurveillanceResponse> responses = this.choiceSurveillanceService.getChoiceSurveillanceForStudents();
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }
}
