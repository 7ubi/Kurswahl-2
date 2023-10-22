package com.x7ubi.kurswahl.response.admin.user;

public class AdminResponse extends UserResponse {

    Long adminId;

    public AdminResponse() {}

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }
}
