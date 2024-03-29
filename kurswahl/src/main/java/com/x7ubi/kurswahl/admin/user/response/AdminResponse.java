package com.x7ubi.kurswahl.admin.user.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema
public class AdminResponse extends UserResponse {
    
    Long adminId;

    public AdminResponse() {
    }

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }
}
