package com.x7ubi.kurswahl.admin.classes.controller;


import com.x7ubi.kurswahl.admin.authentication.AdminRequired;
import com.x7ubi.kurswahl.admin.classes.request.StudentClassCreationRequest;
import com.x7ubi.kurswahl.admin.classes.response.StudentClassResponse;
import com.x7ubi.kurswahl.admin.classes.service.StudentClassCreationService;
import com.x7ubi.kurswahl.common.error.ErrorMessage;
import com.x7ubi.kurswahl.common.exception.EntityCreationException;
import com.x7ubi.kurswahl.common.exception.EntityNotFoundException;
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
    public ResponseEntity<?> createStudentClass(@RequestBody StudentClassCreationRequest studentClassCreationRequest) {

        logger.info("Creating new Student Class");

        try {
            this.studentClassCreationService.createStudentClass(studentClassCreationRequest);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (EntityNotFoundException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (EntityCreationException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/studentClass")
    @AdminRequired
    public ResponseEntity<?> editStudentClass(
            @RequestParam Long studentClassId,
            @RequestBody StudentClassCreationRequest studentClassCreationRequest) {

        logger.info("Editing Student Class");

        try {
            this.studentClassCreationService.editStudentClass(studentClassId, studentClassCreationRequest);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (EntityNotFoundException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (EntityCreationException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/studentClass")
    @AdminRequired
    public ResponseEntity<?> deleteStudentClass(@RequestParam Long studentClassId) {
        logger.info("Deleting Student Class");

        try {
            List<StudentClassResponse> responses = this.studentClassCreationService.deleteStudentClass(studentClassId);
            return ResponseEntity.status(HttpStatus.OK).body(responses);
        } catch (EntityNotFoundException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/studentClass")
    public ResponseEntity<?> getStudentClass(@RequestParam Long studentClassId) {
        logger.info("Getting Student Class");

        try {
            StudentClassResponse response = this.studentClassCreationService.getStudentClass(studentClassId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (EntityNotFoundException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/studentClasses")
    public ResponseEntity<?> getAllStudentClasses() {
        logger.info("Getting all Student Classes");

        try {
            List<StudentClassResponse> responses = this.studentClassCreationService.getAllStudentClasses();
            return ResponseEntity.status(HttpStatus.OK).body(responses);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/studentClasses")
    @AdminRequired
    public ResponseEntity<?> deleteStudentClasses(@RequestBody List<Long> studentClassIds) {
        logger.info("Deleting Student Classes");

        try {
            List<StudentClassResponse> responses = this.studentClassCreationService.deleteStudentClasses(studentClassIds);
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
