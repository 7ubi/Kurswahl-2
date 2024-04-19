package com.x7ubi.kurswahl.common.message.service;

import com.x7ubi.kurswahl.common.error.ErrorMessage;
import com.x7ubi.kurswahl.common.exception.EntityCreationException;
import com.x7ubi.kurswahl.common.exception.EntityNotFoundException;
import com.x7ubi.kurswahl.common.message.mapper.MessageMapper;
import com.x7ubi.kurswahl.common.message.request.CreateMessageRequest;
import com.x7ubi.kurswahl.common.message.response.MessageResponse;
import com.x7ubi.kurswahl.common.message.response.UserMessageResponse;
import com.x7ubi.kurswahl.common.models.AddresseeMessage;
import com.x7ubi.kurswahl.common.models.Message;
import com.x7ubi.kurswahl.common.models.User;
import com.x7ubi.kurswahl.common.repository.AddresseeMessageRepo;
import com.x7ubi.kurswahl.common.repository.MessageRepo;
import com.x7ubi.kurswahl.common.repository.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class MessageService {

    public static final int MAX_TITLE_LENGTH = 100;

    public static final int MAX_MESSAGE_LENGTH = 1000;

    private final Logger logger = LoggerFactory.getLogger(MessageService.class);

    private final UserRepo userRepo;

    private final MessageRepo messageRepo;

    private final AddresseeMessageRepo addresseeMessageRepo;

    private final MessageMapper messageMapper;

    public MessageService(UserRepo userRepo, MessageRepo messageRepo, AddresseeMessageRepo addresseeMessageRepo, MessageMapper messageMapper) {
        this.userRepo = userRepo;
        this.messageRepo = messageRepo;
        this.addresseeMessageRepo = addresseeMessageRepo;
        this.messageMapper = messageMapper;
    }

    @Transactional
    public void createMessage(String username, CreateMessageRequest createMessageRequest) throws EntityNotFoundException, EntityCreationException {
        User sender = getUser(username);

        verifyCreateMessageRequest(createMessageRequest);

        Message message = this.messageMapper.mapCreateMessageRequestToMessage(createMessageRequest);
        message.setSender(sender);

        message.setAddresseeMessage(new HashSet<>());

        message = this.messageRepo.save(message);

        for (Long addresseeId : createMessageRequest.getAddresseeIds()) {
            Optional<User> userOptional = this.userRepo.findUserByUserId(addresseeId);
            if (userOptional.isEmpty()) {
                throw new EntityNotFoundException(ErrorMessage.USER_NOT_FOUND);
            }

            AddresseeMessage addresseeMessage = new AddresseeMessage();
            addresseeMessage.setMessage(message);
            addresseeMessage.setUser(userOptional.get());

            this.addresseeMessageRepo.save(addresseeMessage);

            message.getAddresseeMessage().add(addresseeMessage);
        }

        logger.info(String.format("Message %s was created by %s", message.getTitle(), message.getSender().getUsername()));

        this.messageRepo.save(message);
    }

    private void verifyCreateMessageRequest(CreateMessageRequest createMessageRequest) throws EntityCreationException {
        if (createMessageRequest.getTitle().length() > MAX_TITLE_LENGTH) {
            throw new EntityCreationException(ErrorMessage.MESSAGE_TITLE_TOO_LONG);
        }

        if (createMessageRequest.getMessage().length() > MAX_MESSAGE_LENGTH) {
            throw new EntityCreationException(ErrorMessage.MESSAGE_TOO_LONG);
        }
    }

    @Transactional
    public MessageResponse getMessage(Long messageId, String username) throws EntityNotFoundException {
        Optional<Message> messageOptional = this.messageRepo.findMessageByMessageId(messageId);
        if (messageOptional.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.MESSAGE_NOT_FOUND);
        }
        Message message = messageOptional.get();

        User user = getUser(username);
        Optional<AddresseeMessage> addresseeMessageOptional = message.getAddresseeMessage().stream().filter(addresseeMessage ->
                Objects.equals(addresseeMessage.getUser().getUserId(), user.getUserId())).findFirst();
        if (addresseeMessageOptional.isPresent()) {
            AddresseeMessage addresseeMessage = addresseeMessageOptional.get();
            addresseeMessage.setReadMessage(true);
            this.addresseeMessageRepo.save(addresseeMessage);
        }

        return this.messageMapper.mapMessageToMessageResponse(message);
    }

    @Transactional(readOnly = true)
    public List<MessageResponse> getMessages(String username) throws EntityNotFoundException {
        User user = getUser(username);

        return this.messageMapper.mapAddresseeMessagesToMessageResponses(user.getAddresseeMessage());
    }

    @Transactional(readOnly = true)
    public List<MessageResponse> getSentMessages(String username) throws EntityNotFoundException {
        User user = getUser(username);

        return this.messageMapper.mapMessagesToMessageResponses(user.getSentMessages());
    }

    private User getUser(String username) throws EntityNotFoundException {
        Optional<User> userOptional = this.userRepo.findByUsername(username);

        if (userOptional.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.USER_NOT_FOUND);
        }

        return userOptional.get();
    }

    public List<UserMessageResponse> getUsers() {
        List<User> users = this.userRepo.findAll();

        return this.messageMapper.mapUserToUserMessageResponse(users);
    }
}
