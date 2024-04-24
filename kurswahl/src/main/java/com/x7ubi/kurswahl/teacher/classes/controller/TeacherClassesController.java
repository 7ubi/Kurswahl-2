package com.x7ubi.kurswahl.teacher.classes.controller;

import com.x7ubi.kurswahl.common.jwt.JwtUtils;
import com.x7ubi.kurswahl.teacher.authentication.TeacherRequired;
import com.x7ubi.kurswahl.teacher.classes.response.ClassResponse;
import com.x7ubi.kurswahl.teacher.classes.service.TeacherClassesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teacher")
public class TeacherClassesController {

    private final Logger logger = LoggerFactory.getLogger(TeacherClassesController.class);

    private final TeacherClassesService teacherClassesService;

    private final JwtUtils jwtUtils;

    public TeacherClassesController(TeacherClassesService teacherClassesService, JwtUtils jwtUtils) {
        this.teacherClassesService = teacherClassesService;
        this.jwtUtils = jwtUtils;
    }

    @GetMapping("/classes")
    @TeacherRequired
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<ClassResponse>> getTeacherClasses(@RequestHeader("Authorization") String authorization) {
        logger.info("Getting all classes for teacher");

        String username = jwtUtils.getUsernameFromAuthorizationHeader(authorization);
        List<ClassResponse> response = this.teacherClassesService.getTeacherClasses(username);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
