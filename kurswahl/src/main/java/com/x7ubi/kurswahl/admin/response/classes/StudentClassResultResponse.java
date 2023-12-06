package com.x7ubi.kurswahl.admin.response.classes;

import com.x7ubi.kurswahl.common.response.ResultResponse;

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
