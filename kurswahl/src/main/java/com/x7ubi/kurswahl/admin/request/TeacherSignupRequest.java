package com.x7ubi.kurswahl.admin.request;

import com.x7ubi.kurswahl.common.request.SignupRequest;

public class TeacherSignupRequest extends SignupRequest {

    private String abbreviation;

    public TeacherSignupRequest() {}

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }
}
