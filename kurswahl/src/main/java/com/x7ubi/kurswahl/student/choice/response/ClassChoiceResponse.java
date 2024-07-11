package com.x7ubi.kurswahl.student.choice.response;

public class ClassChoiceResponse {
    private Long classId;

    private String name;

    private Long tapeId;

    private String teacherName;

    private String teacherAbbreviation;

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

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getTeacherAbbreviation() {
        return teacherAbbreviation;
    }

    public void setTeacherAbbreviation(String teacherAbbreviation) {
        this.teacherAbbreviation = teacherAbbreviation;
    }
}
