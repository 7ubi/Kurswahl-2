package com.x7ubi.kurswahl.admin.response.classes;

import com.x7ubi.kurswahl.common.response.ResultResponse;

public class SubjectAreaResultResponse extends ResultResponse {
    private SubjectAreaResponse subjectAreaResponse;

    public SubjectAreaResultResponse() {
    }

    public SubjectAreaResponse getSubjectAreaResponse() {
        return subjectAreaResponse;
    }

    public void setSubjectAreaResponse(SubjectAreaResponse subjectAreaResponse) {
        this.subjectAreaResponse = subjectAreaResponse;
    }
}
