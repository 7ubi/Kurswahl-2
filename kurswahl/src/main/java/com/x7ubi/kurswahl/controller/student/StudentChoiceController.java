package com.x7ubi.kurswahl.controller.student;

import com.x7ubi.kurswahl.jwt.JwtUtils;
import com.x7ubi.kurswahl.request.student.AlterStudentChoiceRequest;
import com.x7ubi.kurswahl.response.common.ResultResponse;
import com.x7ubi.kurswahl.service.authentication.student.StudentRequired;
import com.x7ubi.kurswahl.service.student.StudentChoiceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/student")
public class StudentChoiceController {

    private final Logger logger = LoggerFactory.getLogger(StudentChoiceController.class);

    private final StudentChoiceService studentChoiceService;

    private final JwtUtils jwtUtils;

    public StudentChoiceController(StudentChoiceService studentChoiceService, JwtUtils jwtUtils) {
        this.studentChoiceService = studentChoiceService;
        this.jwtUtils = jwtUtils;
    }

    @PutMapping("/choice")
    @StudentRequired
    public ResponseEntity<?> alterChoice(@RequestHeader("Authorization") String authorization,
                                         @RequestBody AlterStudentChoiceRequest alterStudentChoiceRequest) {
        logger.info("Altering student choice");

        String username = jwtUtils.getUsernameFromAuthorizationHeader(authorization);

        ResultResponse resultResponse = this.studentChoiceService.alterChoice(username, alterStudentChoiceRequest);

        if (!resultResponse.getErrorMessages().isEmpty()) {
            return ResponseEntity.badRequest().body(resultResponse);
        }

        return ResponseEntity.ok().body(resultResponse);
    }
}
