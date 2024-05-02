package com.x7ubi.kurswahl.teacher.classes.response;

import java.util.List;

public class TeacherClassResponse {

    private String name;

    private List<TeacherClassStudentResponse> teacherClassStudentResponses;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TeacherClassStudentResponse> getTeacherClassStudentResponses() {
        return teacherClassStudentResponses;
    }

    public void setTeacherClassStudentResponses(List<TeacherClassStudentResponse> teacherClassStudentResponses) {
        this.teacherClassStudentResponses = teacherClassStudentResponses;
    }
}
