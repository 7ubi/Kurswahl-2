package com.x7ubi.kurswahl.admin.choice.response;

import com.x7ubi.kurswahl.admin.user.response.TeacherResponse;

import java.util.List;

public class ClassStudentsResponse {

    private String name;

    private TeacherResponse teacherResponse;

    private List<StudentSurveillanceResponse> studentSurveillanceResponses;

    private String tapeName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TeacherResponse getTeacherResponse() {
        return teacherResponse;
    }

    public void setTeacherResponse(TeacherResponse teacherResponse) {
        this.teacherResponse = teacherResponse;
    }

    public List<StudentSurveillanceResponse> getStudentSurveillanceResponses() {
        return studentSurveillanceResponses;
    }

    public void setStudentSurveillanceResponses(List<StudentSurveillanceResponse> studentSurveillanceResponses) {
        this.studentSurveillanceResponses = studentSurveillanceResponses;
    }

    public String getTapeName() {
        return tapeName;
    }

    public void setTapeName(String tapeName) {
        this.tapeName = tapeName;
    }
}
