package com.x7ubi.kurswahl.admin.choice.request;

public class AlternateChoiceRequest {
    private Long classId;

    private Long studentId;

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }
}
