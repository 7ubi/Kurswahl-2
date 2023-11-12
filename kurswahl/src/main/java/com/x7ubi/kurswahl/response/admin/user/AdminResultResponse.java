package com.x7ubi.kurswahl.response.admin.user;

import com.x7ubi.kurswahl.response.common.ResultResponse;

public class AdminResultResponse extends ResultResponse {
    private AdminResponse adminResponse;

    public AdminResultResponse() {
    }

    public AdminResponse getAdminResponse() {
        return adminResponse;
    }

    public void setAdminResponse(AdminResponse adminResponse) {
        this.adminResponse = adminResponse;
    }
}
