package com.x7ubi.kurswahl.common.message;

import com.x7ubi.kurswahl.KurswahlServiceTest;
import com.x7ubi.kurswahl.common.error.ErrorMessage;
import com.x7ubi.kurswahl.common.exception.EntityCreationException;
import com.x7ubi.kurswahl.common.exception.EntityNotFoundException;
import com.x7ubi.kurswahl.common.message.request.CreateMessageRequest;
import com.x7ubi.kurswahl.common.message.service.MessageService;
import com.x7ubi.kurswahl.common.models.Message;
import com.x7ubi.kurswahl.common.models.User;
import com.x7ubi.kurswahl.common.repository.MessageRepo;
import com.x7ubi.kurswahl.common.repository.UserRepo;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

@KurswahlServiceTest
public class MessageServiceTest {

    @Autowired
    private MessageService messageService;

    @Autowired
    private MessageRepo messageRepo;

    @Autowired
    private UserRepo userRepo;

    private User sender;

    private User addressee;

    @BeforeEach
    public void setupTests() {
        sender = new User();
        sender.setSurname("Sender");
        sender.setFirstname("Sender");
        sender.setUsername("sender.sender");
        sender.setPassword("password");
        sender = this.userRepo.save(sender);


        addressee = new User();
        addressee.setSurname("Addressee");
        addressee.setFirstname("Addressee");
        addressee.setUsername("addressee.addressee");
        addressee.setPassword("password");
        addressee = this.userRepo.save(addressee);
    }

    @Test
    public void testCreateMessage() throws EntityNotFoundException, EntityCreationException {
        // Given
        CreateMessageRequest createMessageRequest = new CreateMessageRequest();
        createMessageRequest.setTitle("Title");
        createMessageRequest.setMessage("Message");
        createMessageRequest.setAddresseeIds(List.of(addressee.getUserId()));

        // When
        this.messageService.createMessage(sender.getUsername(), createMessageRequest);

        // Then
        Message message = this.messageRepo.findAll().get(0);
        Assertions.assertEquals(message.getTitle(), createMessageRequest.getTitle());
        Assertions.assertEquals(message.getMessage(), createMessageRequest.getMessage());
        Assertions.assertEquals(message.getAddresseeMessage().size(), 1);
        Assertions.assertEquals(message.getAddresseeMessage().stream().toList().get(0).getUser().getUserId(), addressee.getUserId());
        Assertions.assertEquals(message.getSender().getUserId(), sender.getUserId());
    }

    @Test
    public void testCreateMessageSenderNotFound() {
        // Given
        CreateMessageRequest createMessageRequest = new CreateMessageRequest();
        createMessageRequest.setTitle("Title");
        createMessageRequest.setMessage("Message");
        createMessageRequest.setAddresseeIds(List.of(addressee.getUserId()));

        // When
        EntityNotFoundException exception = Assert.assertThrows(EntityNotFoundException.class, () ->
                this.messageService.createMessage("wrong", createMessageRequest));

        // Then
        Assertions.assertEquals(exception.getMessage(), ErrorMessage.USER_NOT_FOUND);
    }

    @Test
    public void testCreateMessageAddresseeNotFound() {
        // Given
        CreateMessageRequest createMessageRequest = new CreateMessageRequest();
        createMessageRequest.setTitle("Title");
        createMessageRequest.setMessage("Message");
        createMessageRequest.setAddresseeIds(List.of(addressee.getUserId() + 3));

        // When
        EntityNotFoundException exception = Assert.assertThrows(EntityNotFoundException.class, () ->
                this.messageService.createMessage(sender.getUsername(), createMessageRequest));

        // Then
        Assertions.assertEquals(exception.getMessage(), ErrorMessage.USER_NOT_FOUND);
    }

    @Test
    public void testCreateMessageTitleTooLong() {
        // Given
        CreateMessageRequest createMessageRequest = new CreateMessageRequest();
        createMessageRequest.setTitle(Arrays.toString(new Byte[101]));
        createMessageRequest.setMessage("Message");
        createMessageRequest.setAddresseeIds(List.of(addressee.getUserId() + 3));

        // When
        EntityCreationException exception = Assert.assertThrows(EntityCreationException.class, () ->
                this.messageService.createMessage(sender.getUsername(), createMessageRequest));

        // Then
        Assertions.assertEquals(exception.getMessage(), ErrorMessage.MESSAGE_TITLE_TOO_LONG);
    }

    @Test
    public void testCreateMessageMessageTooLong() {
        // Given
        CreateMessageRequest createMessageRequest = new CreateMessageRequest();
        createMessageRequest.setTitle("Title");
        createMessageRequest.setMessage(Arrays.toString(new Byte[1001]));
        createMessageRequest.setAddresseeIds(List.of(addressee.getUserId() + 3));

        // When
        EntityCreationException exception = Assert.assertThrows(EntityCreationException.class, () ->
                this.messageService.createMessage(sender.getUsername(), createMessageRequest));

        // Then
        Assertions.assertEquals(exception.getMessage(), ErrorMessage.MESSAGE_TOO_LONG);
    }
}
