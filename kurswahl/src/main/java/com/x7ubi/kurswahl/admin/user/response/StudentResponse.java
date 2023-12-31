package com.x7ubi.kurswahl.admin.user.response;

import com.x7ubi.kurswahl.admin.classes.response.StudentClassResponse;

public class StudentResponse extends UserResponse {

    Long studentId;

    StudentClassResponse studentClassResponse;

    public StudentResponse() {}

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public StudentClassResponse getStudentClassResponse() {
        return studentClassResponse;
    }

    public void setStudentClassResponse(StudentClassResponse studentClassResponse) {
        this.studentClassResponse = studentClassResponse;
    }
}
