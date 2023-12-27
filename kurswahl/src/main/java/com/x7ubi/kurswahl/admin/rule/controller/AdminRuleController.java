package com.x7ubi.kurswahl.admin.rule.controller;

import com.x7ubi.kurswahl.admin.authentication.AdminRequired;
import com.x7ubi.kurswahl.admin.rule.request.RuleCreationRequest;
import com.x7ubi.kurswahl.admin.rule.service.RuleCreationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

        this.ruleCreationService.createRule(ruleCreationRequest);
        return ResponseEntity.status(HttpStatus.OK).build();

    }
}
