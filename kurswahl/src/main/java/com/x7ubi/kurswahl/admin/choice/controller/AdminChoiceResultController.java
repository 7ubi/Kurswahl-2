package com.x7ubi.kurswahl.admin.choice.controller;

import com.x7ubi.kurswahl.admin.authentication.AdminRequired;
import com.x7ubi.kurswahl.admin.choice.response.ChoiceResultResponse;
import com.x7ubi.kurswahl.admin.choice.service.ChoiceResultService;
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
public class AdminChoiceResultController {
    private final Logger logger = LoggerFactory.getLogger(AdminChoiceResultController.class);

    private final ChoiceResultService choiceResultService;

    public AdminChoiceResultController(ChoiceResultService choiceResultService) {
        this.choiceResultService = choiceResultService;
    }

    @GetMapping("/result")
    @AdminRequired
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Get results")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Got results", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = ChoiceResultResponse.class))})
    })
    public ResponseEntity<ChoiceResultResponse> getResultsForYear(@RequestParam Integer year) {
        logger.info(String.format("Get Results for choice of year %s", year));

        ChoiceResultResponse responses = this.choiceResultService.getResults(year);
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }
}
