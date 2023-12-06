package com.x7ubi.kurswahl.admin.response.classes;

import com.x7ubi.kurswahl.common.response.ResultResponse;

public class SubjectResultResponse extends ResultResponse {
    private SubjectResponse subjectResponse;

    public SubjectResultResponse() {
    }

    public SubjectResponse getSubjectResponse() {
        return subjectResponse;
    }

    public void setSubjectResponse(SubjectResponse subjectResponse) {
        this.subjectResponse = subjectResponse;
    }
}
