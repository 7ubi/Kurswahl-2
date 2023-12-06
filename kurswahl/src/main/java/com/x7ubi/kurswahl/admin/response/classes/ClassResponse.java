package com.x7ubi.kurswahl.admin.response.classes;

import com.x7ubi.kurswahl.admin.response.user.TeacherResponse;

public class ClassResponse {
    private Long classId;

    private String name;

    private TeacherResponse teacherResponse;

    private SubjectResponse subjectResponse;

    private TapeResponse tapeResponse;

    public ClassResponse() {
    }

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

    public SubjectResponse getSubjectResponse() {
        return subjectResponse;
    }

    public void setSubjectResponse(SubjectResponse subjectResponse) {
        this.subjectResponse = subjectResponse;
    }

    public TapeResponse getTapeResponse() {
        return tapeResponse;
    }

    public void setTapeResponse(TapeResponse tapeResponse) {
        this.tapeResponse = tapeResponse;
    }
}
