package com.x7ubi.kurswahl.response.admin;

public class StudentResponse extends UserResponse {

    Long studentId;

    public StudentResponse() {}

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }
}
