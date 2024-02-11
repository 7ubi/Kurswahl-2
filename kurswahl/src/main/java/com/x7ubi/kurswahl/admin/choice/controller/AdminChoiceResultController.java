package com.x7ubi.kurswahl.admin.choice.controller;

import com.x7ubi.kurswahl.admin.authentication.AdminRequired;
import com.x7ubi.kurswahl.admin.choice.response.ChoiceResultResponse;
import com.x7ubi.kurswahl.admin.choice.service.ChoiceResultService;
import com.x7ubi.kurswahl.common.error.ErrorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<?> getResultsForYear(@RequestParam Integer year) {
        logger.info(String.format("Get Results for choice of year %s", year));

        try {
            ChoiceResultResponse responses = this.choiceResultService.getResults(year);
            return ResponseEntity.status(HttpStatus.OK).body(responses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.INTERNAL_SERVER_ERROR);
        }
    }
}
