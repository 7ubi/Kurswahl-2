package com.x7ubi.kurswahl.response.admin.classes;

public class SubjectAreaResponse {
    private String name;

    private Long subjectAreaId;

    public SubjectAreaResponse() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSubjectAreaId() {
        return subjectAreaId;
    }

    public void setSubjectAreaId(Long subjectAreaId) {
        this.subjectAreaId = subjectAreaId;
    }
}
