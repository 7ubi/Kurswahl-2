package com.x7ubi.kurswahl.admin.user.controller;

import com.x7ubi.kurswahl.admin.authentication.AdminRequired;
import com.x7ubi.kurswahl.admin.user.request.StudentCsvRequest;
import com.x7ubi.kurswahl.admin.user.request.StudentSignupRequest;
import com.x7ubi.kurswahl.admin.user.response.StudentResponse;
import com.x7ubi.kurswahl.admin.user.service.StudentCreationService;
import com.x7ubi.kurswahl.admin.user.service.StudentCsvService;
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
public class AdminStudentController {

    Logger logger = LoggerFactory.getLogger(AdminStudentController.class);

    private final StudentCreationService studentCreationService;

    private final StudentCsvService studentCsvService;

    public AdminStudentController(StudentCreationService studentCreationService, StudentCsvService studentCsvService) {
        this.studentCreationService = studentCreationService;
        this.studentCsvService = studentCsvService;
    }

    @PostMapping("/student")
    @AdminRequired
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(description = "Signing up new Student")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Signed up new Student"),
            @ApiResponse(responseCode = "404", description = "StudentClass could not be found.", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = String.class))})
    })
    public ResponseEntity<?> createStudent(
            @RequestBody StudentSignupRequest studentSignupRequest
    ) throws EntityNotFoundException {
        logger.info("Signing up new Student");

        this.studentCreationService.registerStudent(studentSignupRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/student")
    @AdminRequired
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Editing Student")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Edited Student"),
            @ApiResponse(responseCode = "404", description = "StudentClass could not be found.", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "404", description = "Student could not be found.", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = String.class))})
    })
    public ResponseEntity<?> editStudent(
            @RequestParam Long studentId,
            @RequestBody StudentSignupRequest studentSignupRequest
    ) throws EntityNotFoundException {
        logger.info("Editing Student");

        this.studentCreationService.editStudent(studentId, studentSignupRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/student")
    @AdminRequired
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Getting Student")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found Student", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = StudentResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Student could not be found.", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = String.class))})
    })
    public ResponseEntity<StudentResponse> getStudent(@RequestParam Long studentId) throws EntityNotFoundException {
        logger.info("Getting Student");

        StudentResponse response = this.studentCreationService.getStudent(studentId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/student")
    @AdminRequired
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Deleting Student")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deleted Student", content =
                    {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = StudentResponse.class)))}),
            @ApiResponse(responseCode = "404", description = "Student could not be found.", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = String.class))})
    })
    public ResponseEntity<List<StudentResponse>> deleteStudent(@RequestParam Long studentId) throws EntityNotFoundException {
        logger.info("Deleting Student");

        List<StudentResponse> response = this.studentCreationService.deleteStudent(studentId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/students")
    @AdminRequired
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Getting all Students")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all Students", content =
                    {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = StudentResponse.class)))})
    })
    public ResponseEntity<List<StudentResponse>> getStudents() {
        logger.info("Getting all Students");

        List<StudentResponse> responses = this.studentCreationService.getAllStudents();
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }


    @DeleteMapping("/students")
    @AdminRequired
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Deleting list of Students")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deleted selected Students", content =
                    {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = StudentResponse.class)))}),
            @ApiResponse(responseCode = "404", description = "A student could not be found.", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = String.class))})
    })
    public ResponseEntity<List<StudentResponse>> deleteStudents(
            @RequestBody List<Long> studentIds
    ) throws EntityNotFoundException {
        logger.info("Deleting Students");

        List<StudentResponse> response = this.studentCreationService.deleteStudents(studentIds);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/csvStudents")
    @AdminRequired
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(description = "Importing Students from csv file")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created Students from csv", content =
                    {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = StudentResponse.class)))})
    })
    public ResponseEntity<List<StudentResponse>> importStudentsFromCsv(@RequestBody StudentCsvRequest studentCsvRequest) {
        logger.info("Importing Students from csv");

        List<StudentResponse> response = this.studentCsvService.importCsv(studentCsvRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
