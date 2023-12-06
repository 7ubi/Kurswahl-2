package com.x7ubi.kurswahl.admin.request;

public class StudentClassCreationRequest {
    private String name;

    private Integer year;

    private Long teacherId;

    public StudentClassCreationRequest() {
    }

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

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }
}
