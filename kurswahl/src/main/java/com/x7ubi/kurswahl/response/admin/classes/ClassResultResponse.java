package com.x7ubi.kurswahl.response.admin.classes;

import com.x7ubi.kurswahl.response.common.ResultResponse;

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
