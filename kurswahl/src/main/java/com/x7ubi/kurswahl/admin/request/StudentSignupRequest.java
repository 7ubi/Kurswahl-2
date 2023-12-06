package com.x7ubi.kurswahl.admin.request;

import com.x7ubi.kurswahl.common.request.SignupRequest;

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
