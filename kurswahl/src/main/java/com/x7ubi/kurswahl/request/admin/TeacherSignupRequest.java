package com.x7ubi.kurswahl.request.admin;

import com.x7ubi.kurswahl.request.auth.SignupRequest;

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
