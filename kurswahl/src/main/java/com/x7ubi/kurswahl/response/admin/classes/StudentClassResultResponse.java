package com.x7ubi.kurswahl.response.admin.classes;

import com.x7ubi.kurswahl.response.common.ResultResponse;

public class StudentClassResultResponse extends ResultResponse {
    private StudentClassResponse studentClassResponse;

    public StudentClassResultResponse() {
    }

    public StudentClassResponse getStudentClassResponse() {
        return studentClassResponse;
    }

    public void setStudentClassResponse(StudentClassResponse studentClassResponse) {
        this.studentClassResponse = studentClassResponse;
    }
}
