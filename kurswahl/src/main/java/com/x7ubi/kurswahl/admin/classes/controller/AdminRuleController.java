package com.x7ubi.kurswahl.admin.classes.controller;

import com.x7ubi.kurswahl.admin.authentication.AdminRequired;
import com.x7ubi.kurswahl.admin.classes.request.RuleCreationRequest;
import com.x7ubi.kurswahl.admin.classes.response.RuleResponse;
import com.x7ubi.kurswahl.admin.classes.service.RuleCreationService;
import com.x7ubi.kurswahl.common.exception.EntityCreationException;
import com.x7ubi.kurswahl.common.exception.EntityNotFoundException;
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

    @GetMapping("/rule")
    @AdminRequired
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Getting rule")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found rule", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = RuleResponse.class))
            }),
            @ApiResponse(responseCode = "404", description = "Rule could not be found.", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = String.class))})
    })
    public ResponseEntity<RuleResponse> getRule(@RequestParam Long ruleId) throws EntityNotFoundException {
        logger.info("Getting Rule");

        RuleResponse response = this.ruleCreationService.getRule(ruleId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/rule")
    @AdminRequired
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(description = "Getting rule")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found rule", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = RuleResponse.class))
            }),
            @ApiResponse(responseCode = "400", description = "Rule already exists.", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = String.class))})
    })
    public ResponseEntity<?> createRule(@RequestBody RuleCreationRequest ruleCreationRequest) throws EntityCreationException {
        logger.info("Creating rule");

        this.ruleCreationService.createRule(ruleCreationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/rule")
    @AdminRequired
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Editing rule")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Edited rule", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = RuleResponse.class))
            }),
            @ApiResponse(responseCode = "404", description = "Rule could not be found.", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "400", description = "Rule already exists.", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = String.class))})
    })
    public ResponseEntity<?> editRule(@RequestParam Long ruleId, @RequestBody RuleCreationRequest ruleCreationRequest) throws EntityNotFoundException, EntityCreationException {
        logger.info("Editing rule");

        this.ruleCreationService.editRule(ruleId, ruleCreationRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/rule")
    @AdminRequired
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Deleting rule")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deleted rule", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = RuleResponse.class)))
            }),
            @ApiResponse(responseCode = "404", description = "Rule could not be found.", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = String.class))})
    })
    public ResponseEntity<List<RuleResponse>> deleteRule(@RequestParam Long ruleId) throws EntityNotFoundException {
        logger.info("Deleting Rule");

        List<RuleResponse> responses = this.ruleCreationService.deleteRule(ruleId);

        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @GetMapping("/rules")
    @AdminRequired
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Getting all rules by year")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all rules", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = RuleResponse.class)))
            })
    })
    public ResponseEntity<List<RuleResponse>> getRules(@RequestParam Integer year) {
        logger.info(String.format("Getting Rules for year: %s", year));

        List<RuleResponse> responses = this.ruleCreationService.getRules(year);

        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @DeleteMapping("/rules")
    @AdminRequired
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Deleting list of rules")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deleted rules", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = RuleResponse.class)))
            }),
            @ApiResponse(responseCode = "404", description = "A Rule could not be found.", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = String.class))})
    })
    public ResponseEntity<List<RuleResponse>> deleteRule(@RequestBody List<Long> ruleIds) throws EntityNotFoundException {
        logger.info("Deleting Rules");

        List<RuleResponse> responses = this.ruleCreationService.deleteRules(ruleIds);

        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }
}
