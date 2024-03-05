package com.x7ubi.kurswahl.admin.user.request;

import com.x7ubi.kurswahl.common.auth.request.SignupRequest;

public class StudentSignupRequest extends SignupRequest {

    private Long studentClassId;

    public StudentSignupRequest() {
    }

    public StudentSignupRequest(String firstname, String surname) {
        super(firstname, surname);
    }

    public Long getStudentClassId() {
        return studentClassId;
    }

    public void setStudentClassId(Long studentClassId) {
        this.studentClassId = studentClassId;
    }
}
