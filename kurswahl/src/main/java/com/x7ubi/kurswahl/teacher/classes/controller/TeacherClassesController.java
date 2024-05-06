package com.x7ubi.kurswahl.teacher.classes.controller;

import com.x7ubi.kurswahl.common.jwt.JwtUtils;
import com.x7ubi.kurswahl.teacher.authentication.TeacherRequired;
import com.x7ubi.kurswahl.teacher.classes.response.TeacherClassResponse;
import com.x7ubi.kurswahl.teacher.classes.service.TeacherClassesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(description = "Getting all classes for teacher")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all classes", content =
                    {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = TeacherClassResponse.class)))})
    })
    public ResponseEntity<List<TeacherClassResponse>> getTeacherClasses(@RequestHeader("Authorization") String authorization) {
        logger.info("Getting all classes for teacher");

        String username = jwtUtils.getUsernameFromAuthorizationHeader(authorization);
        List<TeacherClassResponse> response = this.teacherClassesService.getTeacherClasses(username);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
