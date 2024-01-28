package com.x7ubi.kurswahl.admin.choice.controller;

import com.x7ubi.kurswahl.admin.authentication.AdminRequired;
import com.x7ubi.kurswahl.admin.choice.response.ChoiceSurveillanceResponse;
import com.x7ubi.kurswahl.admin.choice.response.ClassStudentsResponse;
import com.x7ubi.kurswahl.admin.choice.response.StudentChoicesResponse;
import com.x7ubi.kurswahl.admin.choice.service.AssignChoiceService;
import com.x7ubi.kurswahl.admin.choice.service.ChoiceSurveillanceService;
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
public class AdminChoiceController {

    private final Logger logger = LoggerFactory.getLogger(AdminChoiceController.class);

    private final ChoiceSurveillanceService choiceSurveillanceService;

    private final AssignChoiceService assignChoiceService;

    public AdminChoiceController(ChoiceSurveillanceService choiceSurveillanceService, AssignChoiceService assignChoiceService) {
        this.choiceSurveillanceService = choiceSurveillanceService;
        this.assignChoiceService = assignChoiceService;
    }

    @GetMapping("/choiceSurveillance")
    @AdminRequired
    public ResponseEntity<?> getChoiceSurveillanceForStudents() {
        logger.info("Choice Surveillance For Students");

        try {
            List<ChoiceSurveillanceResponse> responses = this.choiceSurveillanceService.getChoiceSurveillanceForStudents();
            return ResponseEntity.status(HttpStatus.OK).body(responses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.INTERNAL_SERVER_ERROR);
        }
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
}
