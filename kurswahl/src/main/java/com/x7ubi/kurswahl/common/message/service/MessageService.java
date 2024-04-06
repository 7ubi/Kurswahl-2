package com.x7ubi.kurswahl.common.message.service;

import com.x7ubi.kurswahl.common.error.ErrorMessage;
import com.x7ubi.kurswahl.common.exception.EntityNotFoundException;
import com.x7ubi.kurswahl.common.message.mapper.MessageMapper;
import com.x7ubi.kurswahl.common.message.request.CreateMessageRequest;
import com.x7ubi.kurswahl.common.message.response.MessageResponse;
import com.x7ubi.kurswahl.common.models.AddresseeMessage;
import com.x7ubi.kurswahl.common.models.Message;
import com.x7ubi.kurswahl.common.models.User;
import com.x7ubi.kurswahl.common.repository.AddresseeMessageRepo;
import com.x7ubi.kurswahl.common.repository.MessageRepo;
import com.x7ubi.kurswahl.common.repository.UserRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

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
    public void createMessage(String username, CreateMessageRequest createMessageRequest) throws EntityNotFoundException {
        User sender = getUser(username);

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

        this.messageRepo.save(message);
    }

    public MessageResponse getMessage(Long messageId) throws EntityNotFoundException {
        Optional<Message> messageOptional = this.messageRepo.findMessageByMessageId(messageId);
        if (messageOptional.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.MESSAGE_NOT_FOUND);
        }

        return this.messageMapper.mapMessageToMessageResponse(messageOptional.get());
    }

    public List<MessageResponse> getMessages(String username) throws EntityNotFoundException {
        User user = getUser(username);

        return this.messageMapper.mapAddresseeMessagesToMessageResponses(user.getAddresseeMessage());
    }

    private User getUser(String username) throws EntityNotFoundException {
        Optional<User> userOptional = this.userRepo.findByUsername(username);

        if (userOptional.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.USER_NOT_FOUND);
        }

        return userOptional.get();
    }

    public List<MessageResponse> getSentMessages(String username) throws EntityNotFoundException {
        User user = getUser(username);

        return this.messageMapper.mapMessagesToMessageResponses(user.getSentMessages());
    }
}
