package com.x7ubi.kurswahl.common.response;

import java.util.List;

public class ResultResponse {

    private List<MessageResponse> errorMessages;

    public ResultResponse() {}

    public ResultResponse(List<MessageResponse> errorMessages) {
        this.errorMessages = errorMessages;
    }

    public List<MessageResponse> getErrorMessages() {
        return errorMessages;
    }

    public void setErrorMessages(List<MessageResponse> errorMessages) {
        this.errorMessages = errorMessages;
    }
}
