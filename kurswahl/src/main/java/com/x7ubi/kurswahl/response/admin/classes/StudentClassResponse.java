package com.x7ubi.kurswahl.response.admin.classes;

import com.x7ubi.kurswahl.response.admin.user.StudentResponse;
import com.x7ubi.kurswahl.response.admin.user.TeacherResponse;

import java.util.List;

public class StudentClassResponse {
    private Long studentClassId;

    private String name;

    private List<StudentResponse> students;

    private TeacherResponse teacher;

    private Integer year;

    private Integer releaseYear;

    public StudentClassResponse() {
    }

    public Long getStudentClassId() {
        return studentClassId;
    }

    public void setStudentClassId(Long studentClassId) {
        this.studentClassId = studentClassId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<StudentResponse> getStudents() {
        return students;
    }

    public void setStudents(List<StudentResponse> students) {
        this.students = students;
    }

    public TeacherResponse getTeacher() {
        return teacher;
    }

    public void setTeacher(TeacherResponse teacher) {
        this.teacher = teacher;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(Integer releaseYear) {
        this.releaseYear = releaseYear;
    }
}
