package com.x7ubi.kurswahl.admin.rule.controller;

import com.x7ubi.kurswahl.admin.authentication.AdminRequired;
import com.x7ubi.kurswahl.admin.rule.request.RuleCreationRequest;
import com.x7ubi.kurswahl.admin.rule.response.RuleResponse;
import com.x7ubi.kurswahl.admin.rule.service.RuleCreationService;
import com.x7ubi.kurswahl.common.error.ErrorMessage;
import com.x7ubi.kurswahl.common.exception.EntityCreationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminRuleController {

    private final Logger logger = LoggerFactory.getLogger(AdminRuleController.class);

    private final RuleCreationService ruleCreationService;

    public AdminRuleController(RuleCreationService ruleCreationService) {
        this.ruleCreationService = ruleCreationService;
    }

    @PostMapping("/rule")
    @AdminRequired
    public ResponseEntity<?> createRule(@RequestBody RuleCreationRequest ruleCreationRequest) {
        logger.info("Creating rule");
        try {
            this.ruleCreationService.createRule(ruleCreationRequest);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (EntityCreationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/rules")
    @AdminRequired
    public ResponseEntity<?> getRules(@RequestParam Integer year) {
        logger.info(String.format("Getting Rules for year: %s", year));
        try {
            List<RuleResponse> responses = this.ruleCreationService.getRules(year);

            return ResponseEntity.status(HttpStatus.OK).body(responses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.INTERNAL_SERVER_ERROR);
        }
    }
}
