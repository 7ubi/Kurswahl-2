package com.x7ubi.kurswahl.response.admin;

public class TeacherResponse extends UserResponse {
    private Long teacherId;

    public TeacherResponse() {}

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }
}
