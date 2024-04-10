package com.x7ubi.kurswahl.common.message.response;

import java.time.LocalDateTime;
import java.util.List;

public class MessageResponse {

    private Long messageId;

    private String title;

    private String message;

    private UserMessageResponse senderResponse;

    private List<UserMessageResponse> addresseeResponses;

    private LocalDateTime date;

    private boolean readMessage = true;

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UserMessageResponse getSenderResponse() {
        return senderResponse;
    }

    public void setSenderResponse(UserMessageResponse senderResponse) {
        this.senderResponse = senderResponse;
    }

    public List<UserMessageResponse> getAddresseeResponses() {
        return addresseeResponses;
    }

    public void setAddresseeResponses(List<UserMessageResponse> addresseeResponses) {
        this.addresseeResponses = addresseeResponses;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public boolean isReadMessage() {
        return readMessage;
    }

    public void setReadMessage(boolean readMessage) {
        this.readMessage = readMessage;
    }
}
