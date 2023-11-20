package com.x7ubi.kurswahl.response.admin.classes;

import com.x7ubi.kurswahl.response.common.ResultResponse;

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
