package com.x7ubi.kurswahl.admin.controller.user;

import com.x7ubi.kurswahl.admin.request.TeacherSignupRequest;
import com.x7ubi.kurswahl.admin.response.user.TeacherResponses;
import com.x7ubi.kurswahl.admin.response.user.TeacherResultResponse;
import com.x7ubi.kurswahl.admin.service.authentication.AdminRequired;
import com.x7ubi.kurswahl.admin.service.user.TeacherCreationService;
import com.x7ubi.kurswahl.common.response.ResultResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminUserController {

    Logger logger = LoggerFactory.getLogger(AdminUserController.class);

    private final TeacherCreationService teacherCreationService;

    public AdminUserController(TeacherCreationService teacherCreationService) {
        this.teacherCreationService = teacherCreationService;
    }

    @PostMapping("/teacher")
    @AdminRequired
    public ResponseEntity<?> createTeacher(
            @RequestBody TeacherSignupRequest teacherSignupRequest
    ) {
        logger.info("Signing up new Teacher");

        this.teacherCreationService.registerTeacher(teacherSignupRequest);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/teacher")
    @AdminRequired
    public ResponseEntity<?> editTeacher(
            @RequestParam Long teacherId,
            @RequestBody TeacherSignupRequest teacherSignupRequest
    ) {
        logger.info("Editing Teacher");

        ResultResponse response = this.teacherCreationService.editTeacher(teacherId, teacherSignupRequest);

        if (response.getErrorMessages().isEmpty()) {
            return ResponseEntity.ok().body(response);
        }

        return ResponseEntity.badRequest().body(response);
    }


    @GetMapping("/teacher")
    @AdminRequired
    public ResponseEntity<?> getTeacher(
            @RequestParam Long teacherId
    ) {
        logger.info("Getting Teacher");

        TeacherResultResponse teacherResultResponse = this.teacherCreationService.getTeacher(teacherId);

        if (teacherResultResponse.getErrorMessages().isEmpty()) {
            return ResponseEntity.ok().body(teacherResultResponse);
        }

        return ResponseEntity.badRequest().body(teacherResultResponse);
    }

    @DeleteMapping("/teacher")
    @AdminRequired
    public ResponseEntity<?> deleteTeacher(@RequestParam Long teacherId) {
        logger.info("Deleting Teacher");

        ResultResponse response = this.teacherCreationService.deleteTeacher(teacherId);

        if(response.getErrorMessages().isEmpty()) {
            return ResponseEntity.ok().body(response);
        }

        return ResponseEntity.badRequest().body(response);
    }

    @GetMapping("/teachers")
    @AdminRequired
    public ResponseEntity<?> getTeacher() {
        logger.info("Getting all Teachers");

        TeacherResponses response = this.teacherCreationService.getAllTeachers();

        return ResponseEntity.ok().body(response);
    }
}
