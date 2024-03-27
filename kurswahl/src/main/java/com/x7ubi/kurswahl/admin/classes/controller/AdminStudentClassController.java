package com.x7ubi.kurswahl.admin.classes.controller;


import com.x7ubi.kurswahl.admin.authentication.AdminRequired;
import com.x7ubi.kurswahl.admin.classes.request.StudentClassCreationRequest;
import com.x7ubi.kurswahl.admin.classes.response.StudentClassResponse;
import com.x7ubi.kurswahl.admin.classes.service.StudentClassCreationService;
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
public class AdminStudentClassController {

    private final Logger logger = LoggerFactory.getLogger(AdminStudentClassController.class);

    private final StudentClassCreationService studentClassCreationService;

    public AdminStudentClassController(StudentClassCreationService studentClassCreationService) {
        this.studentClassCreationService = studentClassCreationService;
    }

    @PostMapping("/studentClass")
    @AdminRequired
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(description = "Create a new student class")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created new student class"),
            @ApiResponse(responseCode = "404", description = "Teacher could not be found.", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "400", description = "A student class with this name already exists.", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = String.class))})
    })
    public ResponseEntity<?> createStudentClass(@RequestBody StudentClassCreationRequest studentClassCreationRequest) throws EntityCreationException, EntityNotFoundException {

        logger.info("Creating new Student Class");

        this.studentClassCreationService.createStudentClass(studentClassCreationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/studentClass")
    @AdminRequired
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Editing student class")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Edited student class"),
            @ApiResponse(responseCode = "404", description = "Student Class could not be found.", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "404", description = "Teacher could not be found.", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "400", description = "A student class with this name already exists.", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = String.class))})
    })
    public ResponseEntity<?> editStudentClass(
            @RequestParam Long studentClassId,
            @RequestBody StudentClassCreationRequest studentClassCreationRequest) throws EntityCreationException, EntityNotFoundException {

        logger.info("Editing Student Class");

        this.studentClassCreationService.editStudentClass(studentClassId, studentClassCreationRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/studentClass")
    @AdminRequired
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Getting student class")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found student class", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = StudentClassResponse.class))
            }),
            @ApiResponse(responseCode = "404", description = "Student class could not be found.", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = String.class))})
    })
    public ResponseEntity<StudentClassResponse> getStudentClass(@RequestParam Long studentClassId) throws EntityNotFoundException {
        logger.info("Getting Student Class");

        StudentClassResponse response = this.studentClassCreationService.getStudentClass(studentClassId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/studentClass")
    @AdminRequired
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Deleting student class")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deleted student class", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema =
                    @Schema(implementation = StudentClassResponse.class)))
            }),
            @ApiResponse(responseCode = "404", description = "Student class could not be found.", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = String.class))})
    })
    public ResponseEntity<List<StudentClassResponse>> deleteStudentClass(@RequestParam Long studentClassId) throws EntityNotFoundException {
        logger.info("Deleting Student Class");

        List<StudentClassResponse> responses = this.studentClassCreationService.deleteStudentClass(studentClassId);
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @GetMapping("/studentClasses")
    @AdminRequired
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Getting all student classes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all student classes", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema =
                    @Schema(implementation = StudentClassResponse.class)))
            })
    })
    public ResponseEntity<List<StudentClassResponse>> getAllStudentClasses() {
        logger.info("Getting all Student Classes");

        List<StudentClassResponse> responses = this.studentClassCreationService.getAllStudentClasses();
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @DeleteMapping("/studentClasses")
    @AdminRequired
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Deleting list of student classes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deleted student classes", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema =
                    @Schema(implementation = StudentClassResponse.class)))
            }),
            @ApiResponse(responseCode = "404", description = "Student class could not be found.", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = String.class))})
    })
    public ResponseEntity<List<StudentClassResponse>> deleteStudentClasses(@RequestBody List<Long> studentClassIds) throws EntityNotFoundException {
        logger.info("Deleting Student Classes");

        List<StudentClassResponse> responses = this.studentClassCreationService.deleteStudentClasses(studentClassIds);
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }
}
