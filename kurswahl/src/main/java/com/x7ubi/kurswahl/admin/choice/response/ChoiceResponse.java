package com.x7ubi.kurswahl.admin.choice.response;

public class ChoiceResponse {
    private Integer choiceNumber;

    private StudentSurveillanceResponse studentSurveillanceResponse;

    public Integer getChoiceNumber() {
        return choiceNumber;
    }

    public void setChoiceNumber(Integer choiceNumber) {
        this.choiceNumber = choiceNumber;
    }

    public StudentSurveillanceResponse getStudentSurveillanceResponse() {
        return studentSurveillanceResponse;
    }

    public void setStudentSurveillanceResponse(StudentSurveillanceResponse studentSurveillanceResponse) {
        this.studentSurveillanceResponse = studentSurveillanceResponse;
    }
}
