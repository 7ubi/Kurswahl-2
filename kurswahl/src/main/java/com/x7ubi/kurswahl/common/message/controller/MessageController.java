package com.x7ubi.kurswahl.common.message.controller;

import com.x7ubi.kurswahl.common.jwt.JwtUtils;
import com.x7ubi.kurswahl.common.message.request.CreateMessageRequest;
import com.x7ubi.kurswahl.common.message.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> createMessage(@RequestHeader("Authorization") String authorization,
                                           @RequestBody CreateMessageRequest createMessageRequest) {
        logger.info("Creating new Message");

        String username = jwtUtils.getUsernameFromAuthorizationHeader(authorization);
        this.messageService.createMessage(username, createMessageRequest);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
