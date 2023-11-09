package com.x7ubi.kurswahl.request.admin;

import com.x7ubi.kurswahl.request.auth.SignupRequest;

public class StudentSignupRequest extends SignupRequest {

    private Long studentClassId;

    public StudentSignupRequest() {}

    public Long getStudentClassId() {
        return studentClassId;
    }

    public void setStudentClassId(Long studentClassId) {
        this.studentClassId = studentClassId;
    }
}
