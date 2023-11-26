package com.x7ubi.kurswahl.controller.admin;

import com.x7ubi.kurswahl.request.admin.StudentClassCreationRequest;
import com.x7ubi.kurswahl.request.admin.SubjectAreaCreationRequest;
import com.x7ubi.kurswahl.request.admin.SubjectCreationRequest;
import com.x7ubi.kurswahl.request.admin.TapeCreationRequest;
import com.x7ubi.kurswahl.response.admin.classes.*;
import com.x7ubi.kurswahl.response.common.ResultResponse;
import com.x7ubi.kurswahl.service.admin.classes.StudentClassCreationService;
import com.x7ubi.kurswahl.service.admin.classes.SubjectAreaCreationService;
import com.x7ubi.kurswahl.service.admin.classes.SubjectCreationService;
import com.x7ubi.kurswahl.service.admin.classes.TapeCreationService;
import com.x7ubi.kurswahl.service.authentication.admin.AdminRequired;
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

    private final StudentClassCreationService studentClassCreationService;

    private final TapeCreationService tapeCreationService;

    public AdminClassesController(
            SubjectAreaCreationService subjectAreaCreationService, SubjectCreationService subjectCreationService,
            StudentClassCreationService studentClassCreationService, TapeCreationService tapeCreationService
    ) {
        this.subjectAreaCreationService = subjectAreaCreationService;
        this.subjectCreationService = subjectCreationService;
        this.studentClassCreationService = studentClassCreationService;
        this.tapeCreationService = tapeCreationService;
    }

    @PostMapping("/subjectArea")
    @AdminRequired
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

    @PutMapping("/subjectArea")
    @AdminRequired
    public ResponseEntity<?> editSubjectArea(
            @RequestParam Long subjectAreaId,
            @RequestBody SubjectAreaCreationRequest subjectAreaCreationRequest
    ) {
        logger.info("Editing Subject area");

        ResultResponse response = this.subjectAreaCreationService.editSubjectArea(subjectAreaId, subjectAreaCreationRequest);

        if (response.getErrorMessages().isEmpty()) {
            return ResponseEntity.ok().body(response);
        }

        return ResponseEntity.badRequest().body(response);
    }

    @GetMapping("/subjectArea")
    @AdminRequired
    public ResponseEntity<?> getSubjectArea(
            @RequestParam Long subjectAreaId
    ) {
        logger.info("Getting Subject area");

        SubjectAreaResultResponse response = this.subjectAreaCreationService.getSubjectArea(subjectAreaId);

        if (response.getErrorMessages().isEmpty()) {
            return ResponseEntity.ok().body(response);
        }

        return ResponseEntity.badRequest().body(response);
    }

    @DeleteMapping("/subjectArea")
    @AdminRequired
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
    @AdminRequired
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

    @PutMapping("/subject")
    @AdminRequired
    public ResponseEntity<?> editSubject(
            @RequestParam Long subjectId,
            @RequestBody SubjectCreationRequest subjectCreationRequest
    ) {
        logger.info("Editing Subject area");

        ResultResponse response = this.subjectCreationService.editSubject(subjectId, subjectCreationRequest);

        if (response.getErrorMessages().isEmpty()) {
            return ResponseEntity.ok().body(response);
        }

        return ResponseEntity.badRequest().body(response);
    }

    @GetMapping("/subject")
    @AdminRequired
    public ResponseEntity<?> getSubject(@RequestParam Long subjectId) {
        logger.info("Getting Subject");

        SubjectResultResponse response = this.subjectCreationService.getSubject(subjectId);

        if (response.getErrorMessages().isEmpty()) {
            return ResponseEntity.ok().body(response);
        }

        return ResponseEntity.badRequest().body(response);
    }

    @DeleteMapping("/subject")
    @AdminRequired
    public ResponseEntity<?> deleteSubject(
            @RequestParam Long subjectId
    ) {
        logger.info("Deleting Subject area");

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

    @PostMapping("/studentClass")
    @AdminRequired
    public ResponseEntity<?> createStudentClass(@RequestBody StudentClassCreationRequest studentClassCreationRequest) {

        logger.info("Creating new Student Class");

        ResultResponse response = this.studentClassCreationService.createStudentClass(studentClassCreationRequest);

        if (response.getErrorMessages().isEmpty()) {
            return ResponseEntity.ok().body(response);
        }

        return ResponseEntity.badRequest().body(response);
    }

    @PutMapping("/studentClass")
    @AdminRequired
    public ResponseEntity<?> editStudentClass(
            @RequestParam Long studentClassId,
            @RequestBody StudentClassCreationRequest studentClassCreationRequest) {

        logger.info("Editing Student Class");

        ResultResponse response
                = this.studentClassCreationService.editStudentClass(studentClassId, studentClassCreationRequest);

        if (response.getErrorMessages().isEmpty()) {
            return ResponseEntity.ok().body(response);
        }

        return ResponseEntity.badRequest().body(response);
    }

    @DeleteMapping("/studentClass")
    @AdminRequired
    public ResponseEntity<?> deleteStudentClass(@RequestParam Long studentClassId) {
        logger.info("Deleting Student Class");

        ResultResponse response = this.studentClassCreationService.deleteStudentClass(studentClassId);

        if (response.getErrorMessages().isEmpty()) {
            return ResponseEntity.ok().body(response);
        }

        return ResponseEntity.badRequest().body(response);
    }

    @GetMapping("/studentClass")
    public ResponseEntity<?> getStudentClass(@RequestParam Long studentClassId) {
        logger.info("Getting Student Class");

        StudentClassResultResponse response = this.studentClassCreationService.getStudentClass(studentClassId);

        if (response.getErrorMessages().isEmpty()) {
            return ResponseEntity.ok().body(response);
        }

        return ResponseEntity.badRequest().body(response);
    }

    @GetMapping("/studentClasses")
    public ResponseEntity<?> getAllStudentClasses() {
        logger.info("Getting all Student Classes");

        StudentClassResponses response = this.studentClassCreationService.getAllStudentClasses();

        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/tape")
    @AdminRequired
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

    @PutMapping("/tape")
    @AdminRequired
    public ResponseEntity<?> editTape(
            @RequestParam Long tapeId,
            @RequestBody TapeCreationRequest tapeCreationRequest
    ) {
        logger.info("Editing Tape");

        ResultResponse response = this.tapeCreationService.editTape(tapeId, tapeCreationRequest);

        if (response.getErrorMessages().isEmpty()) {
            return ResponseEntity.ok().body(response);
        }

        return ResponseEntity.badRequest().body(response);
    }

    @GetMapping("/tape")
    @AdminRequired
    public ResponseEntity<?> getTape(@RequestParam Long tapeId) {
        logger.info("Getting Tape");

        TapeResultResponse response = this.tapeCreationService.getTape(tapeId);

        if (response.getErrorMessages().isEmpty()) {
            return ResponseEntity.ok().body(response);
        }

        return ResponseEntity.badRequest().body(response);
    }

    @DeleteMapping("/tape")
    @AdminRequired
    public ResponseEntity<?> deleteTape(@RequestParam Long tapeId) {
        logger.info("Deleting Tape");

        ResultResponse response = this.tapeCreationService.deleteTape(tapeId);

        if (response.getErrorMessages().isEmpty()) {
            return ResponseEntity.ok().body(response);
        }

        return ResponseEntity.badRequest().body(response);
    }

    @GetMapping("/tapes")
    public ResponseEntity<?> getAllTapes() {
        logger.info("Getting all Tapes");

        TapeResponses tapeResponses = this.tapeCreationService.getAllTapes();

        return ResponseEntity.ok().body(tapeResponses);
    }
}
