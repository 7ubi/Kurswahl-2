package com.x7ubi.kurswahl.response.admin.user;

import com.x7ubi.kurswahl.response.common.ResultResponse;

public class TeacherResultResponse extends ResultResponse {
    private TeacherResponse teacherResponse;

    public TeacherResultResponse() {
    }

    public TeacherResponse getTeacherResponse() {
        return teacherResponse;
    }

    public void setTeacherResponse(TeacherResponse teacherResponse) {
        this.teacherResponse = teacherResponse;
    }
}
