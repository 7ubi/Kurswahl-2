package com.x7ubi.kurswahl.admin.user.controller;

import com.x7ubi.kurswahl.admin.authentication.AdminRequired;
import com.x7ubi.kurswahl.admin.user.request.TeacherSignupRequest;
import com.x7ubi.kurswahl.admin.user.response.TeacherResponse;
import com.x7ubi.kurswahl.admin.user.response.TeacherResponses;
import com.x7ubi.kurswahl.admin.user.service.TeacherCreationService;
import com.x7ubi.kurswahl.common.error.ErrorMessage;
import com.x7ubi.kurswahl.common.exception.EntityDependencyException;
import com.x7ubi.kurswahl.common.exception.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminTeacherController {

    Logger logger = LoggerFactory.getLogger(AdminTeacherController.class);

    private final TeacherCreationService teacherCreationService;

    public AdminTeacherController(TeacherCreationService teacherCreationService) {
        this.teacherCreationService = teacherCreationService;
    }

    @PostMapping("/teacher")
    @AdminRequired
    public ResponseEntity<?> createTeacher(
            @RequestBody TeacherSignupRequest teacherSignupRequest
    ) {
        logger.info("Signing up new Teacher");

        try {
            this.teacherCreationService.registerTeacher(teacherSignupRequest);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/teacher")
    @AdminRequired
    public ResponseEntity<?> editTeacher(
            @RequestParam Long teacherId,
            @RequestBody TeacherSignupRequest teacherSignupRequest
    ) {
        logger.info("Editing Teacher");

        try {
            this.teacherCreationService.editTeacher(teacherId, teacherSignupRequest);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (EntityNotFoundException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/teacher")
    @AdminRequired
    public ResponseEntity<?> getTeacher(
            @RequestParam Long teacherId
    ) {
        logger.info("Getting Teacher");

        try {
            TeacherResponse response = this.teacherCreationService.getTeacher(teacherId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (EntityNotFoundException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/teacher")
    @AdminRequired
    public ResponseEntity<?> deleteTeacher(@RequestParam Long teacherId) {
        logger.info("Deleting Teacher");

        try {
            this.teacherCreationService.deleteTeacher(teacherId);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (EntityNotFoundException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (EntityDependencyException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/teachers")
    @AdminRequired
    public ResponseEntity<?> getTeacher() {
        logger.info("Getting all Teachers");

        try {
            TeacherResponses responses = this.teacherCreationService.getAllTeachers();
            return ResponseEntity.status(HttpStatus.OK).body(responses);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.INTERNAL_SERVER_ERROR);
        }
    }
}
