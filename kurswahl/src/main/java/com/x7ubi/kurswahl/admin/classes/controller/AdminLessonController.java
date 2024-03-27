package com.x7ubi.kurswahl.admin.classes.controller;

import com.x7ubi.kurswahl.admin.authentication.AdminRequired;
import com.x7ubi.kurswahl.admin.classes.request.LessonCreationRequest;
import com.x7ubi.kurswahl.admin.classes.response.TapeResponse;
import com.x7ubi.kurswahl.admin.classes.service.LessonCreationService;
import com.x7ubi.kurswahl.common.exception.EntityCreationException;
import com.x7ubi.kurswahl.common.exception.EntityNotFoundException;
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
@RequestMapping("/api/admin")
public class AdminLessonController {

    private final Logger logger = LoggerFactory.getLogger(AdminLessonController.class);

    private final LessonCreationService lessonCreationService;

    public AdminLessonController(LessonCreationService lessonCreationService) {
        this.lessonCreationService = lessonCreationService;
    }

    @PostMapping("lesson")
    @AdminRequired
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Creating new lesson")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Created new lesson", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = TapeResponse.class)))
            }),
            @ApiResponse(responseCode = "404", description = "Tape could not be found.", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "400", description = "Lesson is not available.", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = String.class))})
    })
    public ResponseEntity<List<TapeResponse>> createLesson(@RequestBody LessonCreationRequest lessonCreationRequest) throws EntityNotFoundException, EntityCreationException {
        logger.info("Creating new Lesson");

        List<TapeResponse> responses = this.lessonCreationService.createLesson(lessonCreationRequest);
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @DeleteMapping("lesson")
    @AdminRequired
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Deleting lesson")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deleted lesson", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = TapeResponse.class)))
            }),
            @ApiResponse(responseCode = "404", description = "Lesson could not be found.", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = String.class))})
    })
    public ResponseEntity<List<TapeResponse>> deleteLesson(@RequestParam Long lessonId) throws EntityNotFoundException {

        logger.info("Deleting Lesson");

        List<TapeResponse> responses = this.lessonCreationService.deleteLesson(lessonId);
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }
}
