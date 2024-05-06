package com.x7ubi.kurswahl.teacher.classes.response;

import java.util.List;

public class TeacherClassResponse {

    private String name;

    private Integer year;

    private String tapeName;

    private List<TeacherClassStudentResponse> teacherClassStudentResponses;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getTapeName() {
        return tapeName;
    }

    public void setTapeName(String tapeName) {
        this.tapeName = tapeName;
    }

    public List<TeacherClassStudentResponse> getTeacherClassStudentResponses() {
        return teacherClassStudentResponses;
    }

    public void setTeacherClassStudentResponses(List<TeacherClassStudentResponse> teacherClassStudentResponses) {
        this.teacherClassStudentResponses = teacherClassStudentResponses;
    }
}
