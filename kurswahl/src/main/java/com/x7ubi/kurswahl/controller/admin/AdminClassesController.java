package com.x7ubi.kurswahl.controller.admin;

import com.x7ubi.kurswahl.request.admin.SubjectAreaCreationRequest;
import com.x7ubi.kurswahl.response.admin.SubjectAreaResponses;
import com.x7ubi.kurswahl.response.common.ResultResponse;
import com.x7ubi.kurswahl.service.admin.classes.SubjectAreaCreationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminClassesController {

    private final Logger logger = LoggerFactory.getLogger(AdminClassesController.class);

    private final SubjectAreaCreationService subjectAreaCreationService;

    public AdminClassesController(SubjectAreaCreationService subjectAreaCreationService) {
        this.subjectAreaCreationService = subjectAreaCreationService;
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
}
