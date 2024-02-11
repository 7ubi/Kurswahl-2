package com.x7ubi.kurswahl.admin.choice.controller;

import com.x7ubi.kurswahl.admin.authentication.AdminRequired;
import com.x7ubi.kurswahl.admin.choice.request.AlternateChoiceRequest;
import com.x7ubi.kurswahl.admin.choice.response.ChoiceTapeResponse;
import com.x7ubi.kurswahl.admin.choice.response.ClassStudentsResponse;
import com.x7ubi.kurswahl.admin.choice.response.StudentChoicesResponse;
import com.x7ubi.kurswahl.admin.choice.service.AssignChoiceService;
import com.x7ubi.kurswahl.common.error.ErrorMessage;
import com.x7ubi.kurswahl.common.exception.EntityNotFoundException;
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
    public ResponseEntity<?> getClassesWithChoices(@RequestParam Integer year) {
        logger.info("Classes with choices");

        try {
            List<ClassStudentsResponse> responses = this.assignChoiceService.getClassesWithStudents(year);
            return ResponseEntity.status(HttpStatus.OK).body(responses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/studentChoices")
    @AdminRequired
    public ResponseEntity<?> getStudentChoices(@RequestParam Long studentId) {
        logger.info("Load Choices of Student");

        try {
            StudentChoicesResponse responses = this.assignChoiceService.getStundetChoices(studentId);
            return ResponseEntity.status(HttpStatus.OK).body(responses);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/assignChoice")
    @AdminRequired
    public ResponseEntity<?> assignAlternateChoice(@RequestBody AlternateChoiceRequest alternateChoiceRequest) {
        logger.info("Assigning alternate Choice to Student");

        try {
            StudentChoicesResponse responses = this.assignChoiceService.assignAlternateChoice(alternateChoiceRequest);
            return ResponseEntity.status(HttpStatus.OK).body(responses);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/assignChoice")
    @AdminRequired
    public ResponseEntity<?> assignChoice(@RequestParam Long choiceClassId) {
        logger.info("Assigning Choice to Student");

        try {
            StudentChoicesResponse responses = this.assignChoiceService.assignChoice(choiceClassId);
            return ResponseEntity.status(HttpStatus.OK).body(responses);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/assignChoice")
    @AdminRequired
    public ResponseEntity<?> deleteChoiceSelection(@RequestParam Long choiceClassId) {
        logger.info("Deleting Choice Selection from Student");

        try {
            StudentChoicesResponse responses = this.assignChoiceService.deleteChoiceSelection(choiceClassId);
            return ResponseEntity.status(HttpStatus.OK).body(responses);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/alternativeChoice")
    @AdminRequired
    public ResponseEntity<?> deleteAlternativeChoiceClass(@RequestParam Long choiceClassId) {
        logger.info("Deleting Alternative Choice Class");

        try {
            StudentChoicesResponse responses = this.assignChoiceService.deleteAlternativeChoiceClass(choiceClassId);
            return ResponseEntity.status(HttpStatus.OK).body(responses);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/choiceTapes")
    @AdminRequired
    public ResponseEntity<?> getTapes(@RequestParam Integer year) {
        logger.info("Getting Tapes");

        try {
            List<ChoiceTapeResponse> responses = this.assignChoiceService.getTapes(year);
            return ResponseEntity.status(HttpStatus.OK).body(responses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.INTERNAL_SERVER_ERROR);
        }
    }
}
