package com.x7ubi.kurswahl.common.auth.request;

public class PasswordResetRequest {

    private Long userId;

    public PasswordResetRequest() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
