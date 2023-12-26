package com.x7ubi.kurswahl.admin.classes.controller;

import com.x7ubi.kurswahl.admin.authentication.AdminRequired;
import com.x7ubi.kurswahl.admin.classes.request.LessonCreationRequest;
import com.x7ubi.kurswahl.admin.classes.response.TapeResponse;
import com.x7ubi.kurswahl.admin.classes.service.LessonCreationService;
import com.x7ubi.kurswahl.common.error.ErrorMessage;
import com.x7ubi.kurswahl.common.exception.EntityCreationException;
import com.x7ubi.kurswahl.common.exception.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminLessonController {

    private final Logger logger = LoggerFactory.getLogger(AdminLessonController.class);

    private final LessonCreationService lessonCreationService;

    public AdminLessonController(LessonCreationService lessonCreationService) {
        this.lessonCreationService = lessonCreationService;
    }

    @PostMapping("lesson")
    @AdminRequired
    public ResponseEntity<?> createLesson(@RequestBody LessonCreationRequest lessonCreationRequest) {
        logger.info("Creating new Lesson");

        try {
            List<TapeResponse> responses = this.lessonCreationService.createLesson(lessonCreationRequest);
            return ResponseEntity.status(HttpStatus.OK).body(responses);
        } catch (EntityNotFoundException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (EntityCreationException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("lesson")
    @AdminRequired
    public ResponseEntity<?> deleteLesson(@RequestParam Long lessonId) {

        logger.info("Deleting Lesson");

        try {
            List<TapeResponse> responses = this.lessonCreationService.deleteLesson(lessonId);
            return ResponseEntity.status(HttpStatus.OK).body(responses);
        } catch (EntityNotFoundException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.INTERNAL_SERVER_ERROR);
        }
    }
}
