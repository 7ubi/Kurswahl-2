package com.x7ubi.kurswahl.admin.classes.request;

import java.util.List;

public class RuleCreationRequest {
    private Integer year;

    private String name;

    private List<Long> subjectIds;

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Long> getSubjectIds() {
        return subjectIds;
    }

    public void setSubjectIds(List<Long> subjectIds) {
        this.subjectIds = subjectIds;
    }
}
