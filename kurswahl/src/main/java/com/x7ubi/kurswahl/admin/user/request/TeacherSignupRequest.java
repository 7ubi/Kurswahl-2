package com.x7ubi.kurswahl.admin.user.request;

import com.x7ubi.kurswahl.common.auth.request.SignupRequest;

public class TeacherSignupRequest extends SignupRequest {

    private String abbreviation;

    public TeacherSignupRequest() {
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }
}
