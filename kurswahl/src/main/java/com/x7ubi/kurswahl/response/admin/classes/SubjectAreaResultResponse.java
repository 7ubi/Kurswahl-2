package com.x7ubi.kurswahl.response.admin.classes;

import com.x7ubi.kurswahl.response.common.ResultResponse;

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
