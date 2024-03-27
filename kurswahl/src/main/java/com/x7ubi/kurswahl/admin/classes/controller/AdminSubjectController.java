package com.x7ubi.kurswahl.admin.classes.controller;

import com.x7ubi.kurswahl.admin.authentication.AdminRequired;
import com.x7ubi.kurswahl.admin.classes.request.SubjectCreationRequest;
import com.x7ubi.kurswahl.admin.classes.response.SubjectResponse;
import com.x7ubi.kurswahl.admin.classes.service.SubjectCreationService;
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
public class AdminSubjectController {
    Logger logger = LoggerFactory.getLogger(AdminSubjectController.class);

    private final SubjectCreationService subjectCreationService;

    public AdminSubjectController(SubjectCreationService subjectCreationService) {
        this.subjectCreationService = subjectCreationService;
    }

    @PostMapping("/subject")
    @AdminRequired
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(description = "Create new subject")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created new subject"),
            @ApiResponse(responseCode = "400", description = "Subject area already exists.", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "404", description = "Subject area could not be found.", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = String.class))})
    })
    public ResponseEntity<?> createSubject(
            @RequestBody SubjectCreationRequest subjectCreationRequest
    ) throws EntityCreationException, EntityNotFoundException {
        logger.info("Creating new Subject");

        this.subjectCreationService.createSubject(subjectCreationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/subject")
    @AdminRequired
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Editing subject")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Edited subject"),
            @ApiResponse(responseCode = "400", description = "Subject area already exists.", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "404", description = "Subject area could not be found.", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "404", description = "Subject could not be found.", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = String.class))})
    })
    public ResponseEntity<?> editSubject(
            @RequestParam Long subjectId,
            @RequestBody SubjectCreationRequest subjectCreationRequest
    ) throws EntityNotFoundException, EntityCreationException {
        logger.info("Editing Subject");

        this.subjectCreationService.editSubject(subjectId, subjectCreationRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/subject")
    @AdminRequired
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Getting subject")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found subject", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = SubjectResponse.class))
            }),
            @ApiResponse(responseCode = "404", description = "Subject could not be found.", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = String.class))})
    })
    public ResponseEntity<SubjectResponse> getSubject(@RequestParam Long subjectId) throws EntityNotFoundException {
        logger.info("Getting Subject");

        SubjectResponse response = this.subjectCreationService.getSubject(subjectId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/subject")
    @AdminRequired
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Deleting subject")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deleted subject", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema =
                    @Schema(implementation = SubjectResponse.class)))
            }),
            @ApiResponse(responseCode = "404", description = "Subject could not be found.", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = String.class))})
    })
    public ResponseEntity<List<SubjectResponse>> deleteSubject(
            @RequestParam Long subjectId
    ) throws EntityNotFoundException {
        logger.info("Deleting Subject");

        List<SubjectResponse> response = this.subjectCreationService.deleteSubject(subjectId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/subjects")
    @AdminRequired
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Getting all subjects")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all subjects", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema =
                    @Schema(implementation = SubjectResponse.class)))
            })
    })
    public ResponseEntity<List<SubjectResponse>> getSubjects() {
        logger.info("Getting all subjects");

        List<SubjectResponse> response = this.subjectCreationService.getAllSubjects();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/subjects")
    @AdminRequired
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Deleting list of subjects")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deleted subjects", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema =
                    @Schema(implementation = SubjectResponse.class)))
            }),
            @ApiResponse(responseCode = "404", description = "Subject could not be found.", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = String.class))})
    })
    public ResponseEntity<?> deleteSubjects(
            @RequestBody List<Long> subjectIds
    ) throws EntityNotFoundException {
        logger.info("Deleting Subjects");

        List<SubjectResponse> response = this.subjectCreationService.deleteSubjects(subjectIds);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
