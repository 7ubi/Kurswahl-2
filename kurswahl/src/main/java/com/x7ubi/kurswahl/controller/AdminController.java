package com.x7ubi.kurswahl.controller;

import com.x7ubi.kurswahl.request.admin.AdminSignupRequest;
import com.x7ubi.kurswahl.request.admin.StudentSignupRequest;
import com.x7ubi.kurswahl.response.admin.AdminResponses;
import com.x7ubi.kurswahl.response.admin.StudentResponses;
import com.x7ubi.kurswahl.response.common.ResultResponse;
import com.x7ubi.kurswahl.service.admin.AdminCreationService;
import com.x7ubi.kurswahl.service.admin.StudentCreationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    Logger logger = LoggerFactory.getLogger(AdminController.class);

    private final AdminCreationService adminCreationService;

    private final StudentCreationService studentCreationService;

    public AdminController(
            AdminCreationService adminCreationService,
            StudentCreationService studentCreationService
    ) {
        this.adminCreationService = adminCreationService;
        this.studentCreationService = studentCreationService;
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
}
