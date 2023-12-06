package com.x7ubi.kurswahl.admin.response.user;

import com.x7ubi.kurswahl.common.response.ResultResponse;

public class StudentResultResponse extends ResultResponse {
    private StudentResponse studentResponse;

    public StudentResultResponse() {
    }

    public StudentResponse getStudentResponse() {
        return studentResponse;
    }

    public void setStudentResponse(StudentResponse studentResponse) {
        this.studentResponse = studentResponse;
    }
}
