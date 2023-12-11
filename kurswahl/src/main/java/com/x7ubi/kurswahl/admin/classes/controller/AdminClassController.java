package com.x7ubi.kurswahl.admin.classes.controller;

import com.x7ubi.kurswahl.admin.authentication.AdminRequired;
import com.x7ubi.kurswahl.admin.classes.request.ClassCreationRequest;
import com.x7ubi.kurswahl.admin.classes.response.ClassResponse;
import com.x7ubi.kurswahl.admin.classes.response.ClassResponses;
import com.x7ubi.kurswahl.admin.classes.service.ClassCreationService;
import com.x7ubi.kurswahl.common.error.ErrorMessage;
import com.x7ubi.kurswahl.common.exception.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> createClass(@RequestBody ClassCreationRequest classCreationRequest) {
        logger.info("Creating class");

        try {
            this.classCreationService.createClass(classCreationRequest);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (EntityNotFoundException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/class")
    @AdminRequired
    public ResponseEntity<?> editClass(@RequestParam Long classId,
                                       @RequestBody ClassCreationRequest classCreationRequest) {
        logger.info("Editing class");

        try {
            this.classCreationService.editClass(classId, classCreationRequest);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (EntityNotFoundException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/class")
    @AdminRequired
    public ResponseEntity<?> getClass(@RequestParam Long classId) {

        logger.info("Getting class");

        try {
            ClassResponse classResponse = this.classCreationService.getClassByClassId(classId);
            return ResponseEntity.status(HttpStatus.OK).body(classResponse);
        } catch (EntityNotFoundException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/class")
    @AdminRequired
    public ResponseEntity<?> deleteClass(@RequestParam Long classId) {
        logger.info("Deleting class");

        try {
            this.classCreationService.deleteClass(classId);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (EntityNotFoundException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("classes")
    public ResponseEntity<?> getAllClasses(@RequestParam Integer year) {
        logger.info("Getting all Classes");

        try {
            ClassResponses classResponses = this.classCreationService.getAllClasses(year);
            return ResponseEntity.status(HttpStatus.OK).body(classResponses);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.INTERNAL_SERVER_ERROR);
        }
    }
}
