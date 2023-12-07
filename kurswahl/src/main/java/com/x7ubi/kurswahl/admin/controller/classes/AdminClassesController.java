package com.x7ubi.kurswahl.admin.controller.classes;

import com.x7ubi.kurswahl.admin.request.ClassCreationRequest;
import com.x7ubi.kurswahl.admin.request.LessonCreationRequest;
import com.x7ubi.kurswahl.admin.request.StudentClassCreationRequest;
import com.x7ubi.kurswahl.admin.request.TapeCreationRequest;
import com.x7ubi.kurswahl.admin.response.classes.*;
import com.x7ubi.kurswahl.admin.service.authentication.AdminRequired;
import com.x7ubi.kurswahl.admin.service.classes.ClassCreationService;
import com.x7ubi.kurswahl.admin.service.classes.LessonCreationService;
import com.x7ubi.kurswahl.admin.service.classes.StudentClassCreationService;
import com.x7ubi.kurswahl.admin.service.classes.TapeCreationService;
import com.x7ubi.kurswahl.common.response.ResultResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminClassesController {

    private final Logger logger = LoggerFactory.getLogger(AdminClassesController.class);

    private final StudentClassCreationService studentClassCreationService;

    private final TapeCreationService tapeCreationService;

    private final ClassCreationService classCreationService;

    private final LessonCreationService lessonCreationService;

    public AdminClassesController(StudentClassCreationService studentClassCreationService,
                                  TapeCreationService tapeCreationService, ClassCreationService classCreationService,
                                  LessonCreationService lessonCreationService) {
        this.studentClassCreationService = studentClassCreationService;
        this.tapeCreationService = tapeCreationService;
        this.classCreationService = classCreationService;
        this.lessonCreationService = lessonCreationService;
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

    @PostMapping("lesson")
    @AdminRequired
    public ResponseEntity<?> createLesson(@RequestBody LessonCreationRequest lessonCreationRequest) {

        logger.info("Creating new Lesson");

        ResultResponse response = this.lessonCreationService.createLesson(lessonCreationRequest);

        if (response.getErrorMessages().isEmpty()) {
            return ResponseEntity.ok().body(response);
        }

        return ResponseEntity.badRequest().body(response);
    }

    @DeleteMapping("lesson")
    @AdminRequired
    public ResponseEntity<?> deleteLesson(@RequestParam Long lessonId) {

        logger.info("Deleting Lesson");

        ResultResponse response = this.lessonCreationService.deleteLesson(lessonId);

        if (response.getErrorMessages().isEmpty()) {
            return ResponseEntity.ok().body(response);
        }

        return ResponseEntity.badRequest().body(response);
    }
}