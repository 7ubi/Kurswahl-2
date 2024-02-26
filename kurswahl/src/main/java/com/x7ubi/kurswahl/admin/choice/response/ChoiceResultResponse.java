package com.x7ubi.kurswahl.admin.choice.response;

import java.util.ArrayList;
import java.util.List;

public class ChoiceResultResponse {
    private List<StudentSurveillanceResponse> studentsNotFulfilledRules = new ArrayList<>();

    private List<StudentSurveillanceResponse> studentsNotChosen = new ArrayList<>();

    private List<ClassStudentsResponse> classStudentsResponses;

    public List<StudentSurveillanceResponse> getStudentsNotFulfilledRules() {
        return studentsNotFulfilledRules;
    }

    public List<ClassStudentsResponse> getClassStudentsResponses() {
        return classStudentsResponses;
    }

    public void setClassStudentsResponses(List<ClassStudentsResponse> classStudentsResponses) {
        this.classStudentsResponses = classStudentsResponses;
    }

    public List<StudentSurveillanceResponse> getStudentsNotChosen() {
        return studentsNotChosen;
    }
}
