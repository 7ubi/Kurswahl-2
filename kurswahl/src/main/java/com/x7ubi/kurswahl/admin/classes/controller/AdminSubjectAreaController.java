package com.x7ubi.kurswahl.admin.classes.controller;

import com.x7ubi.kurswahl.admin.authentication.AdminRequired;
import com.x7ubi.kurswahl.admin.classes.request.SubjectAreaCreationRequest;
import com.x7ubi.kurswahl.admin.classes.response.SubjectAreaResponse;
import com.x7ubi.kurswahl.admin.classes.service.SubjectAreaCreationService;
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
public class AdminSubjectAreaController {
    Logger logger = LoggerFactory.getLogger(AdminSubjectAreaController.class);

    private final SubjectAreaCreationService subjectAreaCreationService;

    public AdminSubjectAreaController(SubjectAreaCreationService subjectAreaCreationService) {
        this.subjectAreaCreationService = subjectAreaCreationService;
    }

    @PostMapping("/subjectArea")
    @AdminRequired
    public ResponseEntity<?> createSubjectArea(
            @RequestBody SubjectAreaCreationRequest subjectAreaCreationRequest
    ) {
        logger.info("Creating new Subject area");

        try {
            this.subjectAreaCreationService.createSubjectArea(subjectAreaCreationRequest);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (EntityCreationException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/subjectArea")
    @AdminRequired
    public ResponseEntity<?> editSubjectArea(
            @RequestParam Long subjectAreaId,
            @RequestBody SubjectAreaCreationRequest subjectAreaCreationRequest
    ) {
        logger.info("Editing Subject area");

        try {
            this.subjectAreaCreationService.editSubjectArea(subjectAreaId, subjectAreaCreationRequest);
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

    @GetMapping("/subjectArea")
    @AdminRequired
    public ResponseEntity<?> getSubjectArea(
            @RequestParam Long subjectAreaId
    ) {
        logger.info("Getting Subject area");
        try {
            SubjectAreaResponse response = this.subjectAreaCreationService.getSubjectArea(subjectAreaId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (EntityNotFoundException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/subjectArea")
    @AdminRequired
    public ResponseEntity<?> deleteSubjectArea(@RequestParam Long subjectAreaId) {
        logger.info("Deleting Subject area");

        try {
            List<SubjectAreaResponse> response = this.subjectAreaCreationService.deleteSubjectArea(subjectAreaId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (EntityNotFoundException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/subjectAreas")
    public ResponseEntity<?> getSubjectAreas() {
        logger.info("Getting all Subject areas");
        try {
            List<SubjectAreaResponse> response = this.subjectAreaCreationService.getAllSubjectAreas();

            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/subjectAreas")
    @AdminRequired
    public ResponseEntity<?> deleteSubjectArea(@RequestBody List<Long> subjectAreaIds) {
        logger.info("Deleting Subject areas");

        try {
            List<SubjectAreaResponse> response = this.subjectAreaCreationService.deleteSubjectAreas(subjectAreaIds);
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
