package com.x7ubi.kurswahl.admin.choice.response;

import com.x7ubi.kurswahl.admin.user.response.TeacherResponse;

public class ClassChoiceResponse {

    private Long classId;

    private String name;

    private Long tapeId;

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

    public Long getTapeId() {
        return tapeId;
    }

    public void setTapeId(Long tapeId) {
        this.tapeId = tapeId;
    }

    public TeacherResponse getTeacherResponse() {
        return teacherResponse;
    }

    public void setTeacherResponse(TeacherResponse teacherResponse) {
        this.teacherResponse = teacherResponse;
    }
}
