package com.x7ubi.kurswahl.common.message.request;

import java.util.List;

public class CreateMessageRequest {
    private String title;

    private String message;

    private List<Long> addresseeIds;

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

    public List<Long> getAddresseeIds() {
        return addresseeIds;
    }

    public void setAddresseeIds(List<Long> addresseeIds) {
        this.addresseeIds = addresseeIds;
    }
}
