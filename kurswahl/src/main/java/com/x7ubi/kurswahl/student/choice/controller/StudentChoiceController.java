package com.x7ubi.kurswahl.student.choice.controller;

import com.x7ubi.kurswahl.common.error.ErrorMessage;
import com.x7ubi.kurswahl.common.exception.EntityNotFoundException;
import com.x7ubi.kurswahl.common.exception.UnauthorizedException;
import com.x7ubi.kurswahl.common.jwt.JwtUtils;
import com.x7ubi.kurswahl.student.authentication.StudentRequired;
import com.x7ubi.kurswahl.student.choice.request.AlterStudentChoiceRequest;
import com.x7ubi.kurswahl.student.choice.request.DeleteClassFromChoiceRequest;
import com.x7ubi.kurswahl.student.choice.response.ChoiceResponse;
import com.x7ubi.kurswahl.student.choice.response.SubjectTapeResponse;
import com.x7ubi.kurswahl.student.choice.response.TapeClassResponse;
import com.x7ubi.kurswahl.student.choice.service.StudentChoiceService;
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

    private final JwtUtils jwtUtils;

    public StudentChoiceController(StudentChoiceService studentChoiceService, JwtUtils jwtUtils) {
        this.studentChoiceService = studentChoiceService;
        this.jwtUtils = jwtUtils;
    }

    @PutMapping("/choice")
    @StudentRequired
    public ResponseEntity<?> alterChoice(@RequestHeader("Authorization") String authorization,
                                         @RequestBody AlterStudentChoiceRequest alterStudentChoiceRequest) {
        logger.info("Altering student choice");

        try {
            String username = jwtUtils.getUsernameFromAuthorizationHeader(authorization);

            ChoiceResponse response = this.studentChoiceService.alterChoice(username, alterStudentChoiceRequest);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (UnauthorizedException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (EntityNotFoundException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/choice")
    @StudentRequired
    public ResponseEntity<?> getChoice(@RequestHeader("Authorization") String authorization,
                                       @RequestParam Integer choiceNumber) {
        try {
            String username = jwtUtils.getUsernameFromAuthorizationHeader(authorization);
            logger.info(String.format("Getting choice from %s", username));

            ChoiceResponse response = this.studentChoiceService.getChoice(username, choiceNumber);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (EntityNotFoundException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/choice")
    @StudentRequired
    public ResponseEntity<?> deleteClassFromChoice(@RequestBody DeleteClassFromChoiceRequest
                                                           deleteClassFromChoiceRequest) {
        logger.info("Deleting class from choice");

        try {
            ChoiceResponse response = this.studentChoiceService.deleteClassFromChoice(deleteClassFromChoiceRequest);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (EntityNotFoundException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/choices")
    @StudentRequired
    public ResponseEntity<?> getChoice(@RequestHeader("Authorization") String authorization) {

        try {
            String username = jwtUtils.getUsernameFromAuthorizationHeader(authorization);

            List<ChoiceResponse> response = this.studentChoiceService.getChoices(username);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (EntityNotFoundException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/tapeClasses")
    @StudentRequired
    public ResponseEntity<?> getTapeClasses(@RequestHeader("Authorization") String authorization) {
        logger.info("Getting tapes");

        try {
            String username = jwtUtils.getUsernameFromAuthorizationHeader(authorization);

            List<TapeClassResponse> responses = this.studentChoiceService.getTapesForChoice(username);
            return ResponseEntity.status(HttpStatus.OK).body(responses);
        } catch (EntityNotFoundException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/subjectTapes")
    @StudentRequired
    public ResponseEntity<?> getSubjectTapes(@RequestHeader("Authorization") String authorization) {
        logger.info("Getting tapes");

        try {
            String username = jwtUtils.getUsernameFromAuthorizationHeader(authorization);

            List<SubjectTapeResponse> responses = this.studentChoiceService.getTapesOfSubjects(username);
            return ResponseEntity.status(HttpStatus.OK).body(responses);
        } catch (EntityNotFoundException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.INTERNAL_SERVER_ERROR);
        }
    }
}
