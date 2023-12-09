package com.x7ubi.kurswahl.admin.classes.controller;

import com.x7ubi.kurswahl.admin.authentication.AdminRequired;
import com.x7ubi.kurswahl.admin.classes.request.ClassCreationRequest;
import com.x7ubi.kurswahl.admin.classes.request.TapeCreationRequest;
import com.x7ubi.kurswahl.admin.classes.response.ClassResponses;
import com.x7ubi.kurswahl.admin.classes.response.TapeResponses;
import com.x7ubi.kurswahl.admin.classes.response.TapeResultResponse;
import com.x7ubi.kurswahl.admin.classes.service.ClassCreationService;
import com.x7ubi.kurswahl.admin.classes.service.TapeCreationService;
import com.x7ubi.kurswahl.common.response.ResultResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminClassesController {

    private final Logger logger = LoggerFactory.getLogger(AdminClassesController.class);

    private final TapeCreationService tapeCreationService;

    private final ClassCreationService classCreationService;

    public AdminClassesController(TapeCreationService tapeCreationService, ClassCreationService classCreationService) {
        this.tapeCreationService = tapeCreationService;
        this.classCreationService = classCreationService;
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
    public ResponseEntity<?> getAllTapes(@RequestParam Integer year) {
        logger.info("Getting all Tapes");

        TapeResponses tapeResponses = this.tapeCreationService.getAllTapes(year);

        return ResponseEntity.ok().body(tapeResponses);
    }

    @PostMapping("/class")
    @AdminRequired
    public ResponseEntity<?> createClass(@RequestBody ClassCreationRequest classCreationRequest) {
        logger.info("Creating class");

        ResultResponse response = this.classCreationService.createClass(classCreationRequest);

        if (response.getErrorMessages().isEmpty()) {
            return ResponseEntity.ok().body(response);
        }

        return ResponseEntity.badRequest().body(response);
    }

    @PutMapping("/class")
    @AdminRequired
    public ResponseEntity<?> editClass(@RequestParam Long classId,
                                       @RequestBody ClassCreationRequest classCreationRequest) {
        logger.info("Editing class");

        ResultResponse response = this.classCreationService.editClass(classId, classCreationRequest);

        if (response.getErrorMessages().isEmpty()) {
            return ResponseEntity.ok().body(response);
        }

        return ResponseEntity.badRequest().body(response);
    }

    @GetMapping("/class")
    @AdminRequired
    public ResponseEntity<?> getClass(@RequestParam Long classId) {

        logger.info("Getting class");

        ResultResponse response = this.classCreationService.getClassByClassId(classId);

        if (response.getErrorMessages().isEmpty()) {
            return ResponseEntity.ok().body(response);
        }

        return ResponseEntity.badRequest().body(response);
    }

    @DeleteMapping("/class")
    @AdminRequired
    public ResponseEntity<?> deleteClass(@RequestParam Long classId) {

        logger.info("Deleting class");

        ResultResponse response = this.classCreationService.deleteClass(classId);

        if (response.getErrorMessages().isEmpty()) {
            return ResponseEntity.ok().body(response);
        }

        return ResponseEntity.badRequest().body(response);
    }

    @GetMapping("classes")
    public ResponseEntity<?> getAllClasses(@RequestParam Integer year) {
        logger.info("Getting all Classes");

        ClassResponses classResponses = this.classCreationService.getAllClasses(year);

        return ResponseEntity.ok().body(classResponses);
    }
}
