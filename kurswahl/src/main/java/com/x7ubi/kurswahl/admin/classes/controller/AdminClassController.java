package com.x7ubi.kurswahl.admin.classes.controller;

import com.x7ubi.kurswahl.admin.authentication.AdminRequired;
import com.x7ubi.kurswahl.admin.classes.request.ClassCreationRequest;
import com.x7ubi.kurswahl.admin.classes.response.ClassResponse;
import com.x7ubi.kurswahl.admin.classes.service.ClassCreationService;
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
public class AdminClassController {

    private final Logger logger = LoggerFactory.getLogger(AdminClassController.class);

    private final ClassCreationService classCreationService;

    public AdminClassController(ClassCreationService classCreationService) {
        this.classCreationService = classCreationService;
    }


    @PostMapping("/class")
    @AdminRequired
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(description = "Create a new class")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created new class"),
            @ApiResponse(responseCode = "404", description = "Teacher could not be found.", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "404", description = "Tape could not be found.", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "404", description = "Subject could not be found.", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = String.class))})
    })
    public ResponseEntity<?> createClass(@RequestBody ClassCreationRequest classCreationRequest) throws EntityNotFoundException {
        logger.info("Creating class");

        this.classCreationService.createClass(classCreationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/class")
    @AdminRequired
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Editing class")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Edited class"),
            @ApiResponse(responseCode = "404", description = "Class could not be found.", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "404", description = "Teacher could not be found.", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "404", description = "Tape could not be found.", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "404", description = "Subject could not be found.", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = String.class))})
    })
    public ResponseEntity<?> editClass(@RequestParam Long classId,
                                       @RequestBody ClassCreationRequest classCreationRequest) throws EntityNotFoundException {
        logger.info("Editing class");

        this.classCreationService.editClass(classId, classCreationRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/class")
    @AdminRequired
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Getting class")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found class", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ClassResponse.class))
            }),
            @ApiResponse(responseCode = "404", description = "Class could not be found.", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = String.class))})
    })
    public ResponseEntity<ClassResponse> getClass(@RequestParam Long classId) throws EntityNotFoundException {

        logger.info("Getting class");

        ClassResponse classResponse = this.classCreationService.getClassByClassId(classId);
        return ResponseEntity.status(HttpStatus.OK).body(classResponse);
    }

    @DeleteMapping("/class")
    @AdminRequired
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Deleting class")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deleted class", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ClassResponse.class)))
            }),
            @ApiResponse(responseCode = "404", description = "Class could not be found.", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = String.class))})
    })
    public ResponseEntity<List<ClassResponse>> deleteClass(@RequestParam Long classId) throws EntityNotFoundException {
        logger.info("Deleting class");

        List<ClassResponse> responses = this.classCreationService.deleteClass(classId);
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @GetMapping("classes")
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Getting all classes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all classes", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ClassResponse.class)))
            })
    })
    public ResponseEntity<List<ClassResponse>> getAllClasses(@RequestParam Integer year) {
        logger.info("Getting all Classes");

        List<ClassResponse> classResponses = this.classCreationService.getAllClasses(year);
        return ResponseEntity.status(HttpStatus.OK).body(classResponses);
    }

    @DeleteMapping("/classes")
    @AdminRequired
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Deleting list of classes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deleted list of classes", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ClassResponse.class)))
            }),
            @ApiResponse(responseCode = "404", description = "A class could not be found.", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = String.class))})
    })
    public ResponseEntity<List<ClassResponse>> deleteClasses(@RequestBody List<Long> classIds) throws EntityNotFoundException {
        logger.info("Deleting classes");

        List<ClassResponse> responses = this.classCreationService.deleteClasses(classIds);
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }
}
