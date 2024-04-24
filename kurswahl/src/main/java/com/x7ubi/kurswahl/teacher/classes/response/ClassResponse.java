package com.x7ubi.kurswahl.teacher.classes.response;

import java.util.List;

public class ClassResponse {

    private String name;

    private List<StudentResponse> studentResponses;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<StudentResponse> getStudentResponses() {
        return studentResponses;
    }

    public void setStudentResponses(List<StudentResponse> studentResponses) {
        this.studentResponses = studentResponses;
    }
}
