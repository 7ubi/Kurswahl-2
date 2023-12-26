package com.x7ubi.kurswahl.admin.classes.controller;

import com.x7ubi.kurswahl.admin.authentication.AdminRequired;
import com.x7ubi.kurswahl.admin.classes.request.TapeCreationRequest;
import com.x7ubi.kurswahl.admin.classes.response.TapeResponse;
import com.x7ubi.kurswahl.admin.classes.service.TapeCreationService;
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
public class AdminTapeController {

    private final Logger logger = LoggerFactory.getLogger(AdminTapeController.class);

    private final TapeCreationService tapeCreationService;

    public AdminTapeController(TapeCreationService tapeCreationService) {
        this.tapeCreationService = tapeCreationService;
    }

    @PostMapping("/tape")
    @AdminRequired
    public ResponseEntity<?> createTape(
            @RequestBody TapeCreationRequest tapeCreationRequest
    ) {
        logger.info("Creating new Tape");

        try {
            this.tapeCreationService.createTape(tapeCreationRequest);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (EntityCreationException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/tape")
    @AdminRequired
    public ResponseEntity<?> editTape(
            @RequestParam Long tapeId,
            @RequestBody TapeCreationRequest tapeCreationRequest
    ) {
        logger.info("Editing Tape");

        try {
            this.tapeCreationService.editTape(tapeId, tapeCreationRequest);
            return ResponseEntity.status(HttpStatus.OK).build();
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

    @GetMapping("/tape")
    @AdminRequired
    public ResponseEntity<?> getTape(@RequestParam Long tapeId) {
        logger.info("Getting Tape");

        try {
            TapeResponse response = this.tapeCreationService.getTape(tapeId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (EntityNotFoundException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/tape")
    @AdminRequired
    public ResponseEntity<?> deleteTape(@RequestParam Long tapeId) {
        logger.info("Deleting Tape");

        try {
            List<TapeResponse> tapeResponses = this.tapeCreationService.deleteTape(tapeId);
            return ResponseEntity.status(HttpStatus.OK).body(tapeResponses);
        } catch (EntityNotFoundException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/tapes")
    public ResponseEntity<?> getAllTapes(@RequestParam Integer year) {
        logger.info("Getting all Tapes");

        try {
            List<TapeResponse> tapeResponses = this.tapeCreationService.getAllTapes(year);
            return ResponseEntity.status(HttpStatus.OK).body(tapeResponses);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/tapes")
    @AdminRequired
    public ResponseEntity<?> deleteTapes(@RequestBody List<Long> tapeIds) {
        logger.info("Deleting Tapes");

        try {
            List<TapeResponse> tapeResponses = this.tapeCreationService.deleteTapes(tapeIds);
            return ResponseEntity.status(HttpStatus.OK).body(tapeResponses);
        } catch (EntityNotFoundException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.INTERNAL_SERVER_ERROR);
        }
    }
}
