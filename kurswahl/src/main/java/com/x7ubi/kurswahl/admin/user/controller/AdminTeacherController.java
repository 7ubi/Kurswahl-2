package com.x7ubi.kurswahl.admin.user.controller;

import com.x7ubi.kurswahl.admin.authentication.AdminRequired;
import com.x7ubi.kurswahl.admin.user.request.TeacherSignupRequest;
import com.x7ubi.kurswahl.admin.user.response.TeacherResponse;
import com.x7ubi.kurswahl.admin.user.service.TeacherCreationService;
import com.x7ubi.kurswahl.admin.user.service.TeacherCsvService;
import com.x7ubi.kurswahl.common.exception.EntityDependencyException;
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
public class AdminTeacherController {

    Logger logger = LoggerFactory.getLogger(AdminTeacherController.class);

    private final TeacherCreationService teacherCreationService;

    private final TeacherCsvService teacherCsvService;

    public AdminTeacherController(TeacherCreationService teacherCreationService, TeacherCsvService teacherCsvService) {
        this.teacherCreationService = teacherCreationService;
        this.teacherCsvService = teacherCsvService;
    }

    @PostMapping("/teacher")
    @AdminRequired
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(description = "Signing up new Teacher")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Signed up new Teacher")
    })
    public ResponseEntity<?> createTeacher(
            @RequestBody TeacherSignupRequest teacherSignupRequest
    ) {
        logger.info("Signing up new Teacher");

        this.teacherCreationService.registerTeacher(teacherSignupRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/teacher")
    @AdminRequired
    @Operation(description = "Editing Teacher")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Edited Teacher"),
            @ApiResponse(responseCode = "404", description = "Teacher could not be found.", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = String.class))})
    })
    public ResponseEntity<?> editTeacher(
            @RequestParam Long teacherId,
            @RequestBody TeacherSignupRequest teacherSignupRequest
    ) throws EntityNotFoundException {
        logger.info("Editing Teacher");

        this.teacherCreationService.editTeacher(teacherId, teacherSignupRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


    @GetMapping("/teacher")
    @AdminRequired
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Getting Teacher")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found Teacher", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = TeacherResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Teacher could not be found.", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = String.class))})
    })
    public ResponseEntity<?> getTeacher(@RequestParam Long teacherId) throws EntityNotFoundException {
        logger.info("Getting Teacher");

        TeacherResponse response = this.teacherCreationService.getTeacher(teacherId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/teacher")
    @AdminRequired
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Getting Teacher")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found Teacher", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = TeacherResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Teacher could not be found.", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "409", description = "Teacher could not be deleted, because it is still used in a StudentClass.", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = String.class))})
    })
    public ResponseEntity<?> deleteTeacher(@RequestParam Long teacherId) throws EntityDependencyException, EntityNotFoundException {
        logger.info("Deleting Teacher");

        List<TeacherResponse> response = this.teacherCreationService.deleteTeacher(teacherId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/teachers")
    @AdminRequired
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Getting all teachers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all Teachers", content =
                    {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = TeacherResponse.class)))})
    })
    public ResponseEntity<?> getTeacher() {
        logger.info("Getting all Teachers");

        List<TeacherResponse> responses = this.teacherCreationService.getAllTeachers();
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @DeleteMapping("/teachers")
    @AdminRequired
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Deleting list of Teachers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deleted selected Teachers", content =
                    {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = TeacherResponse.class)))}),
            @ApiResponse(responseCode = "404", description = "A Teacher could not be found.", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "409", description = "A Teacher could not be deleted, because it is still used in a StudentClass.", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = String.class))})
    })
    public ResponseEntity<?> deleteTeachers(@RequestBody List<Long> teacherIds) throws EntityDependencyException, EntityNotFoundException {
        logger.info("Deleting Teachers");

        List<TeacherResponse> response = this.teacherCreationService.deleteTeachers(teacherIds);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/csvTeachers")
    @AdminRequired
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(description = "Importing Teachers from csv file")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created Teachers from csv", content =
                    {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = TeacherResponse.class)))})
    })
    public ResponseEntity<?> importCsv(@RequestBody String csv) {
        logger.info("Importing Teachers from csv");

        List<TeacherResponse> responses = this.teacherCsvService.importCsv(csv);
        return ResponseEntity.status(HttpStatus.CREATED).body(responses);
    }
}
