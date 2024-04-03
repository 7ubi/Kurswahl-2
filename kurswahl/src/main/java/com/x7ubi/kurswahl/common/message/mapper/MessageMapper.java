package com.x7ubi.kurswahl.common.message.mapper;

import com.x7ubi.kurswahl.common.message.request.CreateMessageRequest;
import com.x7ubi.kurswahl.common.models.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface MessageMapper {

    @Mapping(target = "sender", ignore = true)
    @Mapping(target = "addresseeMessage", ignore = true)
    Message mapCreateMessageRequestToMessage(CreateMessageRequest createMessageRequest);
}
