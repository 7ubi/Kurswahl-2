package com.x7ubi.kurswahl.admin.classes.controller;

import com.x7ubi.kurswahl.admin.authentication.AdminRequired;
import com.x7ubi.kurswahl.admin.classes.request.SubjectCreationRequest;
import com.x7ubi.kurswahl.admin.classes.response.SubjectResponse;
import com.x7ubi.kurswahl.admin.classes.service.SubjectCreationService;
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
public class AdminSubjectController {
    Logger logger = LoggerFactory.getLogger(AdminSubjectController.class);

    private final SubjectCreationService subjectCreationService;

    public AdminSubjectController(SubjectCreationService subjectCreationService) {
        this.subjectCreationService = subjectCreationService;
    }

    @PostMapping("/subject")
    @AdminRequired
    public ResponseEntity<?> createSubject(
            @RequestBody SubjectCreationRequest subjectCreationRequest
    ) {
        logger.info("Creating new Subject");

        try {
            this.subjectCreationService.createSubject(subjectCreationRequest);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (EntityCreationException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/subject")
    @AdminRequired
    public ResponseEntity<?> editSubject(
            @RequestParam Long subjectId,
            @RequestBody SubjectCreationRequest subjectCreationRequest
    ) {
        logger.info("Editing Subject");

        try {
            this.subjectCreationService.editSubject(subjectId, subjectCreationRequest);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (EntityCreationException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (EntityNotFoundException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/subject")
    @AdminRequired
    public ResponseEntity<?> getSubject(@RequestParam Long subjectId) {
        logger.info("Getting Subject");

        try {
            SubjectResponse response = this.subjectCreationService.getSubject(subjectId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (EntityNotFoundException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/subject")
    @AdminRequired
    public ResponseEntity<?> deleteSubject(
            @RequestParam Long subjectId
    ) {
        logger.info("Deleting Subject");

        try {
            List<SubjectResponse> response = this.subjectCreationService.deleteSubject(subjectId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (EntityNotFoundException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/subjects")
    public ResponseEntity<?> getSubjects() {
        try {
            List<SubjectResponse> response = this.subjectCreationService.getAllSubjects();

            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/subjects")
    @AdminRequired
    public ResponseEntity<?> deleteSubjects(
            @RequestBody List<Long> subjectIds
    ) {
        logger.info("Deleting Subjects");

        try {
            List<SubjectResponse> response = this.subjectCreationService.deleteSubjects(subjectIds);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (EntityNotFoundException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.INTERNAL_SERVER_ERROR);
        }
    }
}
