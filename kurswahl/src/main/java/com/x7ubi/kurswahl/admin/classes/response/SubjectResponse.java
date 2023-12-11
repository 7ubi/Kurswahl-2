package com.x7ubi.kurswahl.admin.classes.response;

public class SubjectResponse {

    private Long subjectId;

    private String name;

    private SubjectAreaResponse subjectAreaResponse;

    public SubjectResponse() {}

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SubjectAreaResponse getSubjectAreaResponse() {
        return subjectAreaResponse;
    }

    public void setSubjectAreaResponse(SubjectAreaResponse subjectAreaResponse) {
        this.subjectAreaResponse = subjectAreaResponse;
    }
}
