package com.x7ubi.kurswahl.request.admin;

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
