package com.x7ubi.kurswahl.common.message.mapper;

import com.x7ubi.kurswahl.common.message.request.CreateMessageRequest;
import com.x7ubi.kurswahl.common.message.response.MessageResponse;
import com.x7ubi.kurswahl.common.message.response.UserMessageResponse;
import com.x7ubi.kurswahl.common.models.AddresseeMessage;
import com.x7ubi.kurswahl.common.models.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.Set;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface MessageMapper {

    @Mapping(target = "sender", ignore = true)
    @Mapping(target = "addresseeMessage", ignore = true)
    Message mapCreateMessageRequestToMessage(CreateMessageRequest createMessageRequest);

    List<MessageResponse> mapMessagesToMessageResponses(Set<AddresseeMessage> messages);

    @Mapping(source = "message.messageId", target = "messageId")
    @Mapping(source = "message.message", target = "message")
    @Mapping(source = "message.title", target = "title")
    @Mapping(source = "message.sender", target = "senderResponse")
    @Mapping(source = "message.addresseeMessage", target = "addresseeResponses")
    MessageResponse mapMessageToMessageResponse(AddresseeMessage message);

    @Mapping(source = "user.userId", target = "userId")
    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "user.firstname", target = "firstname")
    @Mapping(source = "user.surname", target = "surname")
    UserMessageResponse mapAddresseeMessageToUserMessageResponse(AddresseeMessage addresseeMessage);
}
