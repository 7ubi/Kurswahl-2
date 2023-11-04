package com.x7ubi.kurswahl.response.admin.user;

public class TeacherResponse extends UserResponse {
    private Long teacherId;

    private String abbreviation;

    public TeacherResponse() {}

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }
}
