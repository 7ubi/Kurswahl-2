package com.x7ubi.kurswahl.admin.classes.controller;

import com.x7ubi.kurswahl.admin.authentication.AdminRequired;
import com.x7ubi.kurswahl.admin.classes.request.TapeCreationRequest;
import com.x7ubi.kurswahl.admin.classes.response.TapeResponse;
import com.x7ubi.kurswahl.admin.classes.service.TapeCreationService;
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
public class AdminTapeController {

    private final Logger logger = LoggerFactory.getLogger(AdminTapeController.class);

    private final TapeCreationService tapeCreationService;

    public AdminTapeController(TapeCreationService tapeCreationService) {
        this.tapeCreationService = tapeCreationService;
    }

    @PostMapping("/tape")
    @AdminRequired
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(description = "Create new tape")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created new tape"),
            @ApiResponse(responseCode = "400", description = "Tape already exists.", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = String.class))})
    })
    public ResponseEntity<?> createTape(
            @RequestBody TapeCreationRequest tapeCreationRequest
    ) throws EntityCreationException {
        logger.info("Creating new Tape");

        this.tapeCreationService.createTape(tapeCreationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/tape")
    @AdminRequired
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Editing tape")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Edited tape"),
            @ApiResponse(responseCode = "400", description = "Tape already exists.", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "404", description = "Tape could not be found.", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = String.class))})
    })
    public ResponseEntity<?> editTape(
            @RequestParam Long tapeId,
            @RequestBody TapeCreationRequest tapeCreationRequest
    ) throws EntityCreationException, EntityNotFoundException {
        logger.info("Editing Tape");

        this.tapeCreationService.editTape(tapeId, tapeCreationRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/tape")
    @AdminRequired
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Getting tape")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found tape", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = TapeResponse.class))
            }),
            @ApiResponse(responseCode = "404", description = "Tape could not be found.", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = String.class))})
    })
    public ResponseEntity<TapeResponse> getTape(@RequestParam Long tapeId) throws EntityNotFoundException {
        logger.info("Getting Tape");

        TapeResponse response = this.tapeCreationService.getTape(tapeId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/tape")
    @AdminRequired
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Deleting tape")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deleted tape", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = TapeResponse.class)))
            }),
            @ApiResponse(responseCode = "404", description = "Tape could not be found.", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = String.class))})
    })
    public ResponseEntity<List<TapeResponse>> deleteTape(@RequestParam Long tapeId) throws EntityNotFoundException {
        logger.info("Deleting Tape");

        List<TapeResponse> tapeResponses = this.tapeCreationService.deleteTape(tapeId);
        return ResponseEntity.status(HttpStatus.OK).body(tapeResponses);
    }

    @GetMapping("/tapes")
    @AdminRequired
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Getting all tapes from year")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all tapes", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = TapeResponse.class)))
            })
    })
    public ResponseEntity<List<TapeResponse>> getAllTapes(@RequestParam Integer year) {
        logger.info("Getting all Tapes");

        List<TapeResponse> tapeResponses = this.tapeCreationService.getAllTapes(year);
        return ResponseEntity.status(HttpStatus.OK).body(tapeResponses);
    }

    @DeleteMapping("/tapes")
    @AdminRequired
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Deleting list of tapes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deleted tapes", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = TapeResponse.class)))
            }),
            @ApiResponse(responseCode = "404", description = "Tape could not be found.", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = String.class))})
    })
    public ResponseEntity<List<TapeResponse>> deleteTapes(@RequestBody List<Long> tapeIds) throws EntityNotFoundException {
        logger.info("Deleting Tapes");

        List<TapeResponse> tapeResponses = this.tapeCreationService.deleteTapes(tapeIds);
        return ResponseEntity.status(HttpStatus.OK).body(tapeResponses);
    }
}
