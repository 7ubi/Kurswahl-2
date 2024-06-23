package com.x7ubi.kurswahl.admin.choice.controller;

import com.x7ubi.kurswahl.admin.authentication.AdminRequired;
import com.x7ubi.kurswahl.admin.choice.request.AlternateChoiceRequest;
import com.x7ubi.kurswahl.admin.choice.request.AlternateChoicesRequest;
import com.x7ubi.kurswahl.admin.choice.request.AssignChoicesRequest;
import com.x7ubi.kurswahl.admin.choice.response.ChoiceTapeResponse;
import com.x7ubi.kurswahl.admin.choice.response.ClassStudentsResponse;
import com.x7ubi.kurswahl.admin.choice.response.StudentChoicesResponse;
import com.x7ubi.kurswahl.admin.choice.response.StudentsChoicesResponse;
import com.x7ubi.kurswahl.admin.choice.service.AssignChoiceService;
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
public class AdminAssignChoiceController {

    Logger logger = LoggerFactory.getLogger(AdminAssignChoiceController.class);

    private final AssignChoiceService assignChoiceService;

    public AdminAssignChoiceController(AssignChoiceService assignChoiceService) {
        this.assignChoiceService = assignChoiceService;
    }

    @GetMapping("/classesStudents")
    @AdminRequired
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Get all classes with students")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all classes with Students", content =
                    {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ClassStudentsResponse.class)))})
    })
    public ResponseEntity<List<ClassStudentsResponse>> getClassesWithStudents(@RequestParam Integer year) {
        logger.info("Classes with students");

        List<ClassStudentsResponse> responses = this.assignChoiceService.getClassesWithStudents(year);
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @GetMapping("/studentChoices")
    @AdminRequired
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Getting choices of student")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found Choices of Student", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = StudentChoicesResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Student could not be found.", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = String.class))})
    })
    public ResponseEntity<StudentChoicesResponse> getStudentChoices(@RequestParam Long studentId) throws EntityNotFoundException {
        logger.info("Load Choices of Student");

        StudentChoicesResponse responses = this.assignChoiceService.getStudentChoices(studentId);
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @GetMapping("/studentsChoices")
    @AdminRequired
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Getting choices of student")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found Choices of Student", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = StudentsChoicesResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Student could not be found.", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = String.class))})
    })
    public ResponseEntity<StudentsChoicesResponse> getStudentsChoices(@RequestParam List<Long> studentIds) {
        logger.info("Load Choices of Students");

        StudentsChoicesResponse responses = this.assignChoiceService.getStudentsChoices(studentIds);
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @PostMapping("/assignChoice")
    @AdminRequired
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Assigning alternate Choice to student")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Assigned alternate Choice to student", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = StudentChoicesResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Student could not be found.", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "404", description = "Class could not be found.", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = String.class))})
    })
    public ResponseEntity<StudentChoicesResponse> assignAlternateChoice(@RequestBody AlternateChoiceRequest alternateChoiceRequest) throws EntityNotFoundException {
        logger.info("Assigning alternate Choice to Student");

        StudentChoicesResponse responses = this.assignChoiceService.assignAlternateChoice(alternateChoiceRequest);
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @PutMapping("/assignChoice")
    @AdminRequired
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Assigning ChoiceClass to student")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Assigned ChoiceClass to student", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = StudentChoicesResponse.class))}),
            @ApiResponse(responseCode = "404", description = "ChoiceClass could not be found.", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = String.class))})
    })
    public ResponseEntity<StudentChoicesResponse> assignChoice(@RequestParam Long choiceClassId) throws EntityNotFoundException {
        logger.info("Assigning ChoiceClass to Student");

        StudentChoicesResponse responses = this.assignChoiceService.assignChoice(choiceClassId);
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @PostMapping("/assignChoices")
    @AdminRequired
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Assigning alternate Choice to students")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Assigned alternate Choice to student", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = StudentChoicesResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Student could not be found.", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "404", description = "Class could not be found.", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = String.class))})
    })
    public ResponseEntity<StudentsChoicesResponse> assignAlternateChoices(@RequestBody AlternateChoicesRequest alternateChoicesRequest) throws EntityNotFoundException {
        logger.info("Assigning alternate Choice to Students");

        StudentsChoicesResponse responses = this.assignChoiceService.assignAlternateChoices(alternateChoicesRequest);
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @PutMapping("/assignChoices")
    @AdminRequired
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Assigning classes to students")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Assigned classes to students", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = StudentsChoicesResponse.class))})
    })
    public ResponseEntity<StudentsChoicesResponse> assignChoices(@RequestBody AssignChoicesRequest assignChoicesRequest) {
        logger.info("Assigning ChoiceClasses to Students");

        StudentsChoicesResponse responses = this.assignChoiceService.assignChoices(assignChoicesRequest);
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @DeleteMapping("/assignChoice")
    @AdminRequired
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Deleting ChoiceClass selection from student")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deleting ChoiceClass selection from student", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = StudentChoicesResponse.class))}),
            @ApiResponse(responseCode = "404", description = "ChoiceClass could not be found.", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = String.class))})
    })
    public ResponseEntity<StudentChoicesResponse> deleteChoiceSelection(@RequestParam Long choiceClassId) throws EntityNotFoundException {
        logger.info("Deleting Choice Selection from Student");

        StudentChoicesResponse responses = this.assignChoiceService.deleteChoiceSelection(choiceClassId);
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @DeleteMapping("/alternativeChoice")
    @AdminRequired
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Deleting alternate Choice from student")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deleted alternate Choice from student", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = StudentChoicesResponse.class))}),
            @ApiResponse(responseCode = "404", description = "ChoiceClass could not be found.", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = String.class))}),
    })
    public ResponseEntity<StudentChoicesResponse> deleteAlternativeChoiceClass(@RequestParam Long choiceClassId) throws EntityNotFoundException {
        logger.info("Deleting Alternative Choice Class");

        StudentChoicesResponse responses = this.assignChoiceService.deleteAlternativeChoiceClass(choiceClassId);
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @GetMapping("/choiceTapes")
    @AdminRequired
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Get all tapes by year")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all tapes", content =
                    {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ChoiceTapeResponse.class)))})
    })
    public ResponseEntity<List<ChoiceTapeResponse>> getTapes(@RequestParam Integer year) {
        logger.info("Getting Tapes");

        List<ChoiceTapeResponse> responses = this.assignChoiceService.getTapes(year);
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }
}
