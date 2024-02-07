package com.x7ubi.kurswahl.admin.choice.response;

import com.x7ubi.kurswahl.admin.user.response.TeacherResponse;

public class ChoiceTapeClassResponse {
    private Long classId;

    private String name;

    private TeacherResponse teacherResponse;

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TeacherResponse getTeacherResponse() {
        return teacherResponse;
    }

    public void setTeacherResponse(TeacherResponse teacherResponse) {
        this.teacherResponse = teacherResponse;
    }
}
