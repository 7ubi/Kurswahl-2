package com.x7ubi.kurswahl.common.message;

import com.x7ubi.kurswahl.KurswahlServiceTest;
import com.x7ubi.kurswahl.common.error.ErrorMessage;
import com.x7ubi.kurswahl.common.exception.EntityCreationException;
import com.x7ubi.kurswahl.common.exception.EntityNotFoundException;
import com.x7ubi.kurswahl.common.message.request.CreateMessageRequest;
import com.x7ubi.kurswahl.common.message.response.MessageResponse;
import com.x7ubi.kurswahl.common.message.response.UserMessageResponse;
import com.x7ubi.kurswahl.common.message.service.MessageService;
import com.x7ubi.kurswahl.common.models.AddresseeMessage;
import com.x7ubi.kurswahl.common.models.Message;
import com.x7ubi.kurswahl.common.models.User;
import com.x7ubi.kurswahl.common.repository.AddresseeMessageRepo;
import com.x7ubi.kurswahl.common.repository.MessageRepo;
import com.x7ubi.kurswahl.common.repository.UserRepo;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

@KurswahlServiceTest
public class MessageServiceTest {

    @Autowired
    private MessageService messageService;

    @Autowired
    private MessageRepo messageRepo;

    @Autowired
    private AddresseeMessageRepo addresseeMessageRepo;

    @Autowired
    private UserRepo userRepo;

    private User sender;

    private User addressee;

    private Message message;

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

    private void setupMessage() {
        message = new Message();
        message.setTitle("Title");
        message.setMessage("Example Message");
        message.setSender(sender);
        message = this.messageRepo.save(message);

        AddresseeMessage addresseeMessage = new AddresseeMessage();
        addresseeMessage.setMessage(message);
        addresseeMessage.setUser(addressee);
        addresseeMessage = this.addresseeMessageRepo.save(addresseeMessage);

        message.setAddresseeMessage(Set.of(addresseeMessage));

        message = this.messageRepo.save(message);
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
        Assertions.assertFalse(message.getAddresseeMessage().stream().toList().get(0).isReadMessage());
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

    @Test
    public void testGetMessage() throws EntityNotFoundException {
        // Given
        setupMessage();

        // When
        MessageResponse messageResponse = this.messageService.getMessage(message.getMessageId(), addressee.getUsername());

        // Then
        Assertions.assertEquals(messageResponse.getMessageId(), message.getMessageId());
        Assertions.assertEquals(messageResponse.getTitle(), message.getTitle());
        Assertions.assertEquals(messageResponse.getMessage(), message.getMessage());
        Assertions.assertEquals(messageResponse.getSenderResponse().getUserId(), sender.getUserId());
        Assertions.assertEquals(messageResponse.getSenderResponse().getUsername(), sender.getUsername());
        Assertions.assertEquals(messageResponse.getAddresseeResponses().size(), 1);
        Assertions.assertEquals(messageResponse.getAddresseeResponses().get(0).getUserId(), addressee.getUserId());
        Assertions.assertEquals(messageResponse.getAddresseeResponses().get(0).getUsername(), addressee.getUsername());
        Assertions.assertTrue(messageResponse.isReadMessage());
    }

    @Test
    public void testGetMessageNotFound() {
        // Given
        setupMessage();

        // When
        EntityNotFoundException exception = Assert.assertThrows(EntityNotFoundException.class, () ->
                this.messageService.getMessage(message.getMessageId() + 3, addressee.getUsername()));

        // Then
        Assertions.assertEquals(exception.getMessage(), ErrorMessage.MESSAGE_NOT_FOUND);
    }

    @Test
    public void testGetMessages() throws EntityNotFoundException {
        // Given
        setupMessage();

        // When
        List<MessageResponse> messageResponses = this.messageService.getMessages(addressee.getUsername());

        // Then
        Assertions.assertEquals(messageResponses.size(), 1);

        MessageResponse messageResponse = messageResponses.get(0);

        Assertions.assertEquals(messageResponse.getMessageId(), message.getMessageId());
        Assertions.assertEquals(messageResponse.getTitle(), message.getTitle());
        Assertions.assertEquals(messageResponse.getMessage(), message.getMessage());
        Assertions.assertEquals(messageResponse.getSenderResponse().getUserId(), sender.getUserId());
        Assertions.assertEquals(messageResponse.getSenderResponse().getUsername(), sender.getUsername());
        Assertions.assertEquals(messageResponse.getAddresseeResponses().size(), 1);
        Assertions.assertEquals(messageResponse.getAddresseeResponses().get(0).getUserId(), addressee.getUserId());
        Assertions.assertEquals(messageResponse.getAddresseeResponses().get(0).getUsername(), addressee.getUsername());
    }

    @Test
    public void testGetMessagesAddresseeNotFound() {
        // Given
        setupMessage();

        // When
        EntityNotFoundException exception = Assert.assertThrows(EntityNotFoundException.class, () ->
                this.messageService.getMessages("not found"));

        // Then
        Assertions.assertEquals(exception.getMessage(), ErrorMessage.USER_NOT_FOUND);
    }

    @Test
    public void testGetSentMessages() throws EntityNotFoundException {
        // Given
        setupMessage();

        // When
        List<MessageResponse> messageResponses = this.messageService.getSentMessages(sender.getUsername());

        // Then
        Assertions.assertEquals(messageResponses.size(), 1);

        MessageResponse messageResponse = messageResponses.get(0);

        Assertions.assertEquals(messageResponse.getMessageId(), message.getMessageId());
        Assertions.assertEquals(messageResponse.getTitle(), message.getTitle());
        Assertions.assertEquals(messageResponse.getMessage(), message.getMessage());
        Assertions.assertEquals(messageResponse.getSenderResponse().getUserId(), sender.getUserId());
        Assertions.assertEquals(messageResponse.getSenderResponse().getUsername(), sender.getUsername());
        Assertions.assertEquals(messageResponse.getAddresseeResponses().size(), 1);
        Assertions.assertEquals(messageResponse.getAddresseeResponses().get(0).getUserId(), addressee.getUserId());
        Assertions.assertEquals(messageResponse.getAddresseeResponses().get(0).getUsername(), addressee.getUsername());
    }

    @Test
    public void testGetSentMessagesSenderNotFound() {
        // Given
        setupMessage();

        // When
        EntityNotFoundException exception = Assert.assertThrows(EntityNotFoundException.class, () ->
                this.messageService.getSentMessages("not found"));

        // Then
        Assertions.assertEquals(exception.getMessage(), ErrorMessage.USER_NOT_FOUND);
    }

    @Test
    public void testGetUsers() {
        // When
        List<UserMessageResponse> response = this.messageService.getUsers(sender.getUsername());

        // Then
        Assertions.assertEquals(response.size(), 1);
        Assertions.assertEquals(response.get(0).getUsername(), addressee.getUsername());
    }
}
