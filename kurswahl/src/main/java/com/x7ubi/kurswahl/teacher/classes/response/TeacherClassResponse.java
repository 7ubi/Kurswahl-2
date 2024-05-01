package com.x7ubi.kurswahl.teacher.classes.response;

import java.util.List;

public class TeacherClassResponse {

    private String name;

    private List<TeacherClassStudentResponse> teacherClassStudentRespons;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TeacherClassStudentResponse> getStudentResponses() {
        return teacherClassStudentRespons;
    }

    public void setStudentResponses(List<TeacherClassStudentResponse> teacherClassStudentRespons) {
        this.teacherClassStudentRespons = teacherClassStudentRespons;
    }
}
