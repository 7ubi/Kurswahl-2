package com.x7ubi.kurswahl.admin.classes.response;

import com.x7ubi.kurswahl.common.response.ResultResponse;

public class ClassResultResponse extends ResultResponse {
    private ClassResponse classResponse;

    public ClassResultResponse() {
    }

    public ClassResponse getClassResponse() {
        return classResponse;
    }

    public void setClassResponse(ClassResponse classResponse) {
        this.classResponse = classResponse;
    }
}
