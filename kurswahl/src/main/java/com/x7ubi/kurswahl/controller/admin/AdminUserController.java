package com.x7ubi.kurswahl.controller.admin;

import com.x7ubi.kurswahl.request.admin.AdminSignupRequest;
import com.x7ubi.kurswahl.request.admin.StudentSignupRequest;
import com.x7ubi.kurswahl.request.admin.TeacherSignupRequest;
import com.x7ubi.kurswahl.response.admin.user.*;
import com.x7ubi.kurswahl.response.common.ResultResponse;
import com.x7ubi.kurswahl.service.admin.user.AdminCreationService;
import com.x7ubi.kurswahl.service.admin.user.StudentCreationService;
import com.x7ubi.kurswahl.service.admin.user.TeacherCreationService;
import com.x7ubi.kurswahl.service.authentication.admin.AdminRequired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminUserController {

    Logger logger = LoggerFactory.getLogger(AdminUserController.class);

    private final AdminCreationService adminCreationService;

    private final StudentCreationService studentCreationService;

    private final TeacherCreationService teacherCreationService;

    public AdminUserController(
            AdminCreationService adminCreationService,
            StudentCreationService studentCreationService,
            TeacherCreationService teacherCreationService) {
        this.adminCreationService = adminCreationService;
        this.studentCreationService = studentCreationService;
        this.teacherCreationService = teacherCreationService;
    }

    @PostMapping("/admin")
    @AdminRequired
    public ResponseEntity<?> createAdmin(
            @RequestBody AdminSignupRequest signupRequest
    ) {
        logger.info("Signing up new Admin");

        adminCreationService.registerAdmin(signupRequest);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/admin")
    @AdminRequired
    public ResponseEntity<?> editAdmin(
            @RequestParam Long adminId,
            @RequestBody AdminSignupRequest signupRequest
    ) {
        logger.info("Editing Admin");

        ResultResponse response = adminCreationService.editAdmin(adminId, signupRequest);

        if (response.getErrorMessages().isEmpty()) {
            return ResponseEntity.ok().body(response);
        }

        return ResponseEntity.badRequest().body(response);
    }

    @GetMapping("/admin")
    @AdminRequired
    public ResponseEntity<?> getAdmin(
            @RequestParam Long adminId
    ) {
        logger.info("Getting Admin");

        AdminResultResponse response = adminCreationService.getAdmin(adminId);

        if (response.getErrorMessages().isEmpty()) {
            return ResponseEntity.ok().body(response);
        }

        return ResponseEntity.badRequest().body(response);
    }

    @DeleteMapping("/admin")
    @AdminRequired
    public ResponseEntity<?> deleteAdmin(
            @RequestParam Long adminId
    ) {
        ResultResponse response = adminCreationService.deleteAdmin(adminId);

        if(response.getErrorMessages().isEmpty()) {
            return ResponseEntity.ok().body(response);
        }

        return ResponseEntity.badRequest().body(response);
    }

    @GetMapping("/admins")
    @AdminRequired
    public ResponseEntity<?> getAdmins() {
        logger.info("Getting all Admins");

        AdminResponses response = adminCreationService.getAllAdmins();

        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/student")
    @AdminRequired
    public ResponseEntity<?> createStudent(
            @RequestBody StudentSignupRequest studentSignupRequest
    ) {
        logger.info("Signing up new Student");

        ResultResponse response = this.studentCreationService.registerStudent(studentSignupRequest);

        if (response.getErrorMessages().isEmpty()) {
            return ResponseEntity.ok().body(response);
        }

        return ResponseEntity.badRequest().body(response);
    }

    @PutMapping("/student")
    @AdminRequired
    public ResponseEntity<?> editStudent(
            @RequestParam Long studentId,
            @RequestBody StudentSignupRequest studentSignupRequest
    ) {
        logger.info("Signing up new Student");

        ResultResponse response = this.studentCreationService.editStudent(studentId, studentSignupRequest);

        if (response.getErrorMessages().isEmpty()) {
            return ResponseEntity.ok().body(response);
        }

        return ResponseEntity.badRequest().body(response);
    }

    @GetMapping("/student")
    @AdminRequired
    public ResponseEntity<?> getStudent(
            @RequestParam Long studentId
    ) {
        logger.info("Getting Admin");

        StudentResultResponse response = this.studentCreationService.getStudent(studentId);

        if (response.getErrorMessages().isEmpty()) {
            return ResponseEntity.ok().body(response);
        }

        return ResponseEntity.badRequest().body(response);
    }

    @DeleteMapping("/student")
    @AdminRequired
    public ResponseEntity<?> deleteStudent(
            @RequestParam Long studentId
    ) {
        ResultResponse response = this.studentCreationService.deleteStudent(studentId);

        if(response.getErrorMessages().isEmpty()) {
            return ResponseEntity.ok().body(response);
        }

        return ResponseEntity.badRequest().body(response);
    }

    @GetMapping("/students")
    @AdminRequired
    public ResponseEntity<?> getStudents() {
        logger.info("Getting all Students");

        StudentResponses response = this.studentCreationService.getAllStudents();

        return ResponseEntity.ok().body(response);
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
