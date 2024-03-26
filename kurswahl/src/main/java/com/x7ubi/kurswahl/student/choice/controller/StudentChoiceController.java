package com.x7ubi.kurswahl.student.choice.controller;

import com.x7ubi.kurswahl.common.exception.EntityNotFoundException;
import com.x7ubi.kurswahl.common.exception.UnauthorizedException;
import com.x7ubi.kurswahl.common.jwt.JwtUtils;
import com.x7ubi.kurswahl.student.authentication.StudentRequired;
import com.x7ubi.kurswahl.student.choice.request.AlterStudentChoiceRequest;
import com.x7ubi.kurswahl.student.choice.request.DeleteClassFromChoiceRequest;
import com.x7ubi.kurswahl.student.choice.response.ChoiceResponse;
import com.x7ubi.kurswahl.student.choice.response.SubjectTapeResponse;
import com.x7ubi.kurswahl.student.choice.response.TapeClassResponse;
import com.x7ubi.kurswahl.student.choice.service.StudentChoiceResultService;
import com.x7ubi.kurswahl.student.choice.service.StudentChoiceService;
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
@RequestMapping("/api/student")
public class StudentChoiceController {

    private final Logger logger = LoggerFactory.getLogger(StudentChoiceController.class);

    private final StudentChoiceService studentChoiceService;

    private final StudentChoiceResultService studentChoiceResultService;

    private final JwtUtils jwtUtils;

    public StudentChoiceController(StudentChoiceService studentChoiceService, StudentChoiceResultService studentChoiceResultService, JwtUtils jwtUtils) {
        this.studentChoiceService = studentChoiceService;
        this.studentChoiceResultService = studentChoiceResultService;
        this.jwtUtils = jwtUtils;
    }

    @PutMapping("/choice")
    @StudentRequired
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Altering Choice of Student")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Altered Choice of Student", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = ChoiceResponse.class))}),
            @ApiResponse(responseCode = "401", description = "Student can only alter a Choice with Choice number 1 or 2", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "404", description = "Student could not be found", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = String.class))})
    })
    public ResponseEntity<ChoiceResponse> alterChoice(@RequestHeader("Authorization") String authorization,
                                                      @RequestBody AlterStudentChoiceRequest alterStudentChoiceRequest)
            throws UnauthorizedException, EntityNotFoundException {
        logger.info("Altering student choice");

        String username = jwtUtils.getUsernameFromAuthorizationHeader(authorization);

        ChoiceResponse response = this.studentChoiceService.alterChoice(username, alterStudentChoiceRequest);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/choice")
    @StudentRequired
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Getting choice of Students")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found Choice of Student", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = ChoiceResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Student could not be found", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = String.class))})
    })
    public ResponseEntity<ChoiceResponse> getChoice(@RequestHeader("Authorization") String authorization,
                                                    @RequestParam Integer choiceNumber) throws EntityNotFoundException {
        String username = jwtUtils.getUsernameFromAuthorizationHeader(authorization);
        logger.info(String.format("Getting choice from %s", username));

        ChoiceResponse response = this.studentChoiceService.getChoice(username, choiceNumber);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/choice")
    @StudentRequired
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Deleting class from choice of Students")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deleted class Choice of Student", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = ChoiceResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Student could not be found", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "404", description = "Class was not in Student's choice.", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = String.class))})
    })
    public ResponseEntity<?> deleteClassFromChoice(@RequestBody DeleteClassFromChoiceRequest
                                                           deleteClassFromChoiceRequest) throws EntityNotFoundException {
        logger.info("Deleting class from choice");

        ChoiceResponse response = this.studentChoiceService.deleteClassFromChoice(deleteClassFromChoiceRequest);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/choices")
    @StudentRequired
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Getting choices of Student")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found Choices of Student", content =
                    {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ChoiceResponse.class)))}),
            @ApiResponse(responseCode = "404", description = "Student could not be found", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "404", description = "Student does not have a first and second choice", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = String.class))})
    })
    public ResponseEntity<?> getChoice(@RequestHeader("Authorization") String authorization) throws EntityNotFoundException {

        String username = jwtUtils.getUsernameFromAuthorizationHeader(authorization);

        List<ChoiceResponse> response = this.studentChoiceService.getChoices(username);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/tapeClasses")
    @StudentRequired
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Getting all tapes for Student's year")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all tapes", content =
                    {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = TapeClassResponse.class)))}),
            @ApiResponse(responseCode = "404", description = "Student could not be found", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = String.class))})
    })
    public ResponseEntity<List<TapeClassResponse>> getTapeClasses(@RequestHeader("Authorization") String authorization) throws EntityNotFoundException {
        logger.info("Getting tapes");

        String username = jwtUtils.getUsernameFromAuthorizationHeader(authorization);

        List<TapeClassResponse> responses = this.studentChoiceService.getTapesForChoice(username);
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @GetMapping("/subjectTapes")
    @StudentRequired
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Getting all subjects with the tapes they are on for Student's year")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Got all subjects with the tapes they are on for Student's year", content =
                    {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = SubjectTapeResponse.class)))}),
            @ApiResponse(responseCode = "404", description = "Student could not be found", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = String.class))})
    })
    public ResponseEntity<List<SubjectTapeResponse>> getSubjectTapes(@RequestHeader("Authorization") String authorization) throws EntityNotFoundException {
        logger.info("Getting tapes");

        String username = jwtUtils.getUsernameFromAuthorizationHeader(authorization);

        List<SubjectTapeResponse> responses = this.studentChoiceService.getTapesOfSubjects(username);
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @GetMapping("/choiceResult")
    @StudentRequired
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Getting results of Student")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found results of Student", content =
                    {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ChoiceResponse.class)))}),
            @ApiResponse(responseCode = "404", description = "Student could not be found", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = String.class))})
    })
    public ResponseEntity<?> getChoiceResult(@RequestHeader("Authorization") String authorization) throws EntityNotFoundException {
        String username = jwtUtils.getUsernameFromAuthorizationHeader(authorization);
        logger.info(String.format("Getting result from %s", username));

        ChoiceResponse response = this.studentChoiceResultService.getStudentChoiceResult(username);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
