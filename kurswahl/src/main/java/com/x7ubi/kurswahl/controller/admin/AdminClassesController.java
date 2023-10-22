package com.x7ubi.kurswahl.controller.admin;

import com.x7ubi.kurswahl.request.admin.SubjectAreaCreationRequest;
import com.x7ubi.kurswahl.request.admin.SubjectCreationRequest;
import com.x7ubi.kurswahl.request.admin.TapeCreationRequest;
import com.x7ubi.kurswahl.response.admin.classes.SubjectAreaResponses;
import com.x7ubi.kurswahl.response.admin.classes.SubjectResponses;
import com.x7ubi.kurswahl.response.common.ResultResponse;
import com.x7ubi.kurswahl.service.admin.classes.SubjectAreaCreationService;
import com.x7ubi.kurswahl.service.admin.classes.SubjectCreationService;
import com.x7ubi.kurswahl.service.admin.classes.TapeCreationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminClassesController {

    private final Logger logger = LoggerFactory.getLogger(AdminClassesController.class);

    private final SubjectAreaCreationService subjectAreaCreationService;

    private final SubjectCreationService subjectCreationService;

    private final TapeCreationService tapeCreationService;

    public AdminClassesController(
            SubjectAreaCreationService subjectAreaCreationService,
            SubjectCreationService subjectCreationService,
            TapeCreationService tapeCreationService
    ) {
        this.subjectAreaCreationService = subjectAreaCreationService;
        this.subjectCreationService = subjectCreationService;
        this.tapeCreationService = tapeCreationService;
    }

    @PostMapping("/subjectArea")
    public ResponseEntity<?> createSubjectArea(
            @RequestBody SubjectAreaCreationRequest subjectAreaCreationRequest
    ) {
        logger.info("Creating new Subject area");

        ResultResponse response = this.subjectAreaCreationService.createSubjectArea(subjectAreaCreationRequest);

        if(response.getErrorMessages().isEmpty()) {
            return ResponseEntity.ok().body(response);
        }

        return ResponseEntity.badRequest().body(response);
    }

    @DeleteMapping("/subjectArea")
    public ResponseEntity<?> deleteSubjectArea(@RequestParam Long subjectAreaId) {
        logger.info("Deleting Subject area");

        ResultResponse response = this.subjectAreaCreationService.deleteSubjectArea(subjectAreaId);

        if(response.getErrorMessages().isEmpty()) {
            return ResponseEntity.ok().body(response);
        }

        return ResponseEntity.badRequest().body(response);
    }

    @GetMapping("/subjectAreas")
    public ResponseEntity<?> getSubjectAreas() {

        SubjectAreaResponses response = this.subjectAreaCreationService.getAllSubjectAreas();

        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/subject")
    public ResponseEntity<?> createSubject(
            @RequestBody SubjectCreationRequest subjectCreationRequest
    ) {
        logger.info("Creating new Subject area");

        ResultResponse response = this.subjectCreationService.createSubject(subjectCreationRequest);

        if(response.getErrorMessages().isEmpty()) {
            return ResponseEntity.ok().body(response);
        }

        return ResponseEntity.badRequest().body(response);
    }

    @DeleteMapping("/subject")
    public ResponseEntity<?> deleteSubject(
            @RequestParam Long subjectId
    ) {
        logger.info("Creating new Subject area");

        ResultResponse response = this.subjectCreationService.deleteSubject(subjectId);

        if(response.getErrorMessages().isEmpty()) {
            return ResponseEntity.ok().body(response);
        }

        return ResponseEntity.badRequest().body(response);
    }

    @GetMapping("/subjects")
    public ResponseEntity<?> getSubjects() {

        SubjectResponses response = this.subjectCreationService.getAllSubjects();

        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/tape")
    public ResponseEntity<?> createTape(
            @RequestBody TapeCreationRequest tapeCreationRequest
    ) {
        logger.info("Creating new Tape");

        ResultResponse response = this.tapeCreationService.createTape(tapeCreationRequest);

        if(response.getErrorMessages().isEmpty()) {
            return ResponseEntity.ok().body(response);
        }

        return ResponseEntity.badRequest().body(response);
    }
}
