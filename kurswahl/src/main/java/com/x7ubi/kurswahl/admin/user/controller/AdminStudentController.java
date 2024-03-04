package com.x7ubi.kurswahl.admin.user.controller;

import com.x7ubi.kurswahl.admin.authentication.AdminRequired;
import com.x7ubi.kurswahl.admin.user.request.StudentCsvRequest;
import com.x7ubi.kurswahl.admin.user.request.StudentSignupRequest;
import com.x7ubi.kurswahl.admin.user.response.StudentResponse;
import com.x7ubi.kurswahl.admin.user.service.StudentCreationService;
import com.x7ubi.kurswahl.admin.user.service.StudentCsvService;
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
    public ResponseEntity<?> createStudent(
            @RequestBody StudentSignupRequest studentSignupRequest
    ) {
        logger.info("Signing up new Student");

        try {
            this.studentCreationService.registerStudent(studentSignupRequest);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (EntityNotFoundException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/student")
    @AdminRequired
    public ResponseEntity<?> editStudent(
            @RequestParam Long studentId,
            @RequestBody StudentSignupRequest studentSignupRequest
    ) {
        logger.info("Editing Student");

        try {
            this.studentCreationService.editStudent(studentId, studentSignupRequest);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (EntityNotFoundException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/student")
    @AdminRequired
    public ResponseEntity<?> getStudent(
            @RequestParam Long studentId
    ) {
        logger.info("Getting Student");

        try {
            StudentResponse response = this.studentCreationService.getStudent(studentId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (EntityNotFoundException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/student")
    @AdminRequired
    public ResponseEntity<?> deleteStudent(
            @RequestParam Long studentId
    ) {
        logger.info("Deleting Student");

        try {
            List<StudentResponse> response = this.studentCreationService.deleteStudent(studentId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (EntityNotFoundException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/students")
    @AdminRequired
    public ResponseEntity<?> getStudents() {
        logger.info("Getting all Students");

        try {
            List<StudentResponse> responses = this.studentCreationService.getAllStudents();
            return ResponseEntity.status(HttpStatus.OK).body(responses);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping("/students")
    @AdminRequired
    public ResponseEntity<?> deleteStudents(
            @RequestBody List<Long> studentIds
    ) {
        logger.info("Deleting Students");

        try {
            List<StudentResponse> response = this.studentCreationService.deleteStudents(studentIds);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (EntityNotFoundException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/csvStudents")
    @AdminRequired
    public ResponseEntity<?> importStudentsFromCsv(@RequestBody StudentCsvRequest studentCsvRequest) {
        logger.info("Importing Students from csv");


        try {
            List<StudentResponse> response = this.studentCsvService.importCsv(studentCsvRequest);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.INTERNAL_SERVER_ERROR);
        }
    }
}
