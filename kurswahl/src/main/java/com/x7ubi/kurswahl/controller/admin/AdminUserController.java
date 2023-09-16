package com.x7ubi.kurswahl.controller.admin;

import com.x7ubi.kurswahl.request.admin.AdminSignupRequest;
import com.x7ubi.kurswahl.request.admin.StudentSignupRequest;
import com.x7ubi.kurswahl.request.admin.TeacherSignupRequest;
import com.x7ubi.kurswahl.response.admin.AdminResponses;
import com.x7ubi.kurswahl.response.admin.StudentResponses;
import com.x7ubi.kurswahl.response.admin.TeacherResponses;
import com.x7ubi.kurswahl.response.common.ResultResponse;
import com.x7ubi.kurswahl.service.admin.user.AdminCreationService;
import com.x7ubi.kurswahl.service.admin.user.StudentCreationService;
import com.x7ubi.kurswahl.service.admin.user.TeacherCreationService;
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
    public ResponseEntity<?> createAdmin(
            @RequestBody AdminSignupRequest signupRequest
    ) {
        logger.info("Signing up new Admin");

        ResultResponse response = adminCreationService.registerAdmin(signupRequest);

        if(response.getErrorMessages().isEmpty()) {
            return ResponseEntity.ok().body(response);
        }

        return ResponseEntity.badRequest().body(response);
    }

    @DeleteMapping("/admin")
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
    public ResponseEntity<?> getAdmins() {
        logger.info("Getting all Admins");

        AdminResponses response = adminCreationService.getAllAdmins();

        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/student")
    public ResponseEntity<?> createStudent(
            @RequestBody StudentSignupRequest studentSignupRequest
    ) {
        logger.info("Signing up new Student");

        ResultResponse response = this.studentCreationService.registerStudent(studentSignupRequest);

        if(response.getErrorMessages().isEmpty()) {
            return ResponseEntity.ok().body(response);
        }

        return ResponseEntity.badRequest().body(response);
    }

    @DeleteMapping("/student")
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
    public ResponseEntity<?> getStudents() {
        logger.info("Getting all Students");

        StudentResponses response = this.studentCreationService.getAllStudents();

        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/teacher")
    public ResponseEntity<?> createTeacher(
            @RequestBody TeacherSignupRequest teacherSignupRequest
    ) {
        logger.info("Signing up new Teacher");

        ResultResponse response = this.teacherCreationService.registerTeacher(teacherSignupRequest);

        if(response.getErrorMessages().isEmpty()) {
            return ResponseEntity.ok().body(response);
        }

        return ResponseEntity.badRequest().body(response);
    }

    @DeleteMapping("/teacher")
    public ResponseEntity<?> deleteTeacher(@RequestParam Long teacherId) {
        logger.info("Deleting Teacher");

        ResultResponse response = this.teacherCreationService.deleteTeacher(teacherId);

        if(response.getErrorMessages().isEmpty()) {
            return ResponseEntity.ok().body(response);
        }

        return ResponseEntity.badRequest().body(response);
    }

    @GetMapping("/teachers")
    public ResponseEntity<?> getTeacher() {
        logger.info("Getting all Teachers");

        TeacherResponses response = this.teacherCreationService.getAllTeachers();

        return ResponseEntity.ok().body(response);
    }
}
