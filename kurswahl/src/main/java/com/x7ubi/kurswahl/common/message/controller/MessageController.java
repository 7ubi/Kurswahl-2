package com.x7ubi.kurswahl.common.message.controller;

import com.x7ubi.kurswahl.common.exception.EntityCreationException;
import com.x7ubi.kurswahl.common.exception.EntityNotFoundException;
import com.x7ubi.kurswahl.common.jwt.JwtUtils;
import com.x7ubi.kurswahl.common.message.request.CreateMessageRequest;
import com.x7ubi.kurswahl.common.message.response.MessageResponse;
import com.x7ubi.kurswahl.common.message.response.UserMessageResponse;
import com.x7ubi.kurswahl.common.message.service.MessageService;
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
@RequestMapping("/api/common")
public class MessageController {

    private final Logger logger = LoggerFactory.getLogger(MessageController.class);

    private final MessageService messageService;

    private final JwtUtils jwtUtils;

    public MessageController(MessageService messageService, JwtUtils jwtUtils) {
        this.messageService = messageService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/message")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(description = "Creating new Message")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Creating a new Message."),
            @ApiResponse(responseCode = "404", description = "User could not be found.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
            }),
            @ApiResponse(responseCode = "400", description = "Title or Message is too long", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
            })
    })
    public ResponseEntity<?> createMessage(@RequestHeader("Authorization") String authorization,
                                           @RequestBody CreateMessageRequest createMessageRequest)
            throws EntityNotFoundException, EntityCreationException {
        logger.info("Creating new Message");

        String username = jwtUtils.getUsernameFromAuthorizationHeader(authorization);
        this.messageService.createMessage(username, createMessageRequest);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/message")
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Getting Message by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found Message by id.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
            }),
            @ApiResponse(responseCode = "404", description = "Message could not be found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
            })
    })
    public ResponseEntity<MessageResponse> getMessage(@RequestHeader("Authorization") String authorization, @RequestParam Long messageId) throws EntityNotFoundException {
        logger.info("Getting Message");

        String username = jwtUtils.getUsernameFromAuthorizationHeader(authorization);
        MessageResponse responses = this.messageService.getMessage(messageId, username);

        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @GetMapping("/messages")
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Getting Messages, that user received")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found Messages.", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = MessageResponse.class)))
            }),
            @ApiResponse(responseCode = "404", description = "User could not be found.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
            })
    })
    public ResponseEntity<List<MessageResponse>> getMessages(@RequestHeader("Authorization") String authorization) throws EntityNotFoundException {
        logger.info("Getting Messages");

        String username = jwtUtils.getUsernameFromAuthorizationHeader(authorization);
        List<MessageResponse> responses = this.messageService.getMessages(username);

        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @GetMapping("/messages/sent")
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Getting Messages, that user sent")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found Messages.", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = MessageResponse.class)))
            }),
            @ApiResponse(responseCode = "404", description = "User could not be found.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
            })
    })
    public ResponseEntity<List<MessageResponse>> getSentMessages(@RequestHeader("Authorization") String authorization) throws EntityNotFoundException {
        logger.info("Getting sent Messages");

        String username = jwtUtils.getUsernameFromAuthorizationHeader(authorization);
        List<MessageResponse> responses = this.messageService.getSentMessages(username);

        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Getting Messages, that user received")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found Messages.", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = MessageResponse.class)))
            }),
            @ApiResponse(responseCode = "404", description = "User could not be found.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
            })
    })
    public ResponseEntity<List<UserMessageResponse>> getUsers() {
        logger.info("Getting Messages");

        List<UserMessageResponse> responses = this.messageService.getUsers();

        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }
}
