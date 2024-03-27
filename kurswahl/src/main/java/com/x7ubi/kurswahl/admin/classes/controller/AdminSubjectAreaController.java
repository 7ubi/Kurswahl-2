package com.x7ubi.kurswahl.admin.classes.controller;

import com.x7ubi.kurswahl.admin.authentication.AdminRequired;
import com.x7ubi.kurswahl.admin.classes.request.SubjectAreaCreationRequest;
import com.x7ubi.kurswahl.admin.classes.response.SubjectAreaResponse;
import com.x7ubi.kurswahl.admin.classes.service.SubjectAreaCreationService;
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
public class AdminSubjectAreaController {
    Logger logger = LoggerFactory.getLogger(AdminSubjectAreaController.class);

    private final SubjectAreaCreationService subjectAreaCreationService;

    public AdminSubjectAreaController(SubjectAreaCreationService subjectAreaCreationService) {
        this.subjectAreaCreationService = subjectAreaCreationService;
    }

    @PostMapping("/subjectArea")
    @AdminRequired
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(description = "Create new subject area")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created new subject area"),
            @ApiResponse(responseCode = "400", description = "Subject area already exists.", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = String.class))})
    })
    public ResponseEntity<?> createSubjectArea(
            @RequestBody SubjectAreaCreationRequest subjectAreaCreationRequest
    ) throws EntityCreationException {
        logger.info("Creating new Subject area");

        this.subjectAreaCreationService.createSubjectArea(subjectAreaCreationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/subjectArea")
    @AdminRequired
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Editing subject area")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Edited subject area"),
            @ApiResponse(responseCode = "400", description = "Subject area already exists.", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "404", description = "Subject area could not be found.", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = String.class))})
    })
    public ResponseEntity<?> editSubjectArea(
            @RequestParam Long subjectAreaId,
            @RequestBody SubjectAreaCreationRequest subjectAreaCreationRequest
    ) throws EntityCreationException, EntityNotFoundException {
        logger.info("Editing Subject area");

        this.subjectAreaCreationService.editSubjectArea(subjectAreaId, subjectAreaCreationRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/subjectArea")
    @AdminRequired
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Getting subject area")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found subject area", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = SubjectAreaResponse.class))
            }),
            @ApiResponse(responseCode = "404", description = "Subject area could not be found.", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = String.class))})
    })
    public ResponseEntity<SubjectAreaResponse> getSubjectArea(
            @RequestParam Long subjectAreaId
    ) throws EntityNotFoundException {
        logger.info("Getting Subject area");

        SubjectAreaResponse response = this.subjectAreaCreationService.getSubjectArea(subjectAreaId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/subjectArea")
    @AdminRequired
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Deleting subject area")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deleted subject area", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema =
                    @Schema(implementation = SubjectAreaResponse.class)))
            }),
            @ApiResponse(responseCode = "404", description = "Subject area could not be found.", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = String.class))})
    })
    public ResponseEntity<List<SubjectAreaResponse>> deleteSubjectArea(@RequestParam Long subjectAreaId) throws EntityNotFoundException {
        logger.info("Deleting Subject area");

        List<SubjectAreaResponse> response = this.subjectAreaCreationService.deleteSubjectArea(subjectAreaId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/subjectAreas")
    @AdminRequired
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Getting all subject areas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all subject areas", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema =
                    @Schema(implementation = SubjectAreaResponse.class)))
            })
    })
    public ResponseEntity<List<SubjectAreaResponse>> getSubjectAreas() {
        logger.info("Getting all Subject areas");

        List<SubjectAreaResponse> response = this.subjectAreaCreationService.getAllSubjectAreas();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/subjectAreas")
    @AdminRequired
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Deleting list of subject areas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deleted subject areas", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema =
                    @Schema(implementation = SubjectAreaResponse.class)))
            }),
            @ApiResponse(responseCode = "404", description = "Subject area could not be found.", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = String.class))})
    })
    public ResponseEntity<List<SubjectAreaResponse>> deleteSubjectArea(@RequestBody List<Long> subjectAreaIds) throws EntityNotFoundException {
        logger.info("Deleting Subject areas");

        List<SubjectAreaResponse> response = this.subjectAreaCreationService.deleteSubjectAreas(subjectAreaIds);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
