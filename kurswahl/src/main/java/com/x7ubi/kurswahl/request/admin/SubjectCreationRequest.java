package com.x7ubi.kurswahl.request.admin;

public class SubjectCreationRequest {
    private String name;

    private Long subjectAreaId;

    public SubjectCreationRequest() {}

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
