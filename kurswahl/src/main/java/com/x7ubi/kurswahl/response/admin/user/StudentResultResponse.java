package com.x7ubi.kurswahl.response.admin.user;

import com.x7ubi.kurswahl.response.common.ResultResponse;

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
