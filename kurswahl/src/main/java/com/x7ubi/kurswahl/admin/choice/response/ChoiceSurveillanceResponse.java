package com.x7ubi.kurswahl.admin.choice.response;

public class ChoiceSurveillanceResponse {

    private StudentSurveillanceResponse studentSurveillanceResponse;

    private Boolean chosen = false;

    private Boolean fulfilledRules = false;

    public StudentSurveillanceResponse getStudentSurveillanceResponse() {
        return studentSurveillanceResponse;
    }

    public void setStudentSurveillanceResponse(StudentSurveillanceResponse studentSurveillanceResponse) {
        this.studentSurveillanceResponse = studentSurveillanceResponse;
    }

    public Boolean getChosen() {
        return chosen;
    }

    public void setChosen(Boolean chosen) {
        this.chosen = chosen;
    }

    public Boolean getFulfilledRules() {
        return fulfilledRules;
    }

    public void setFulfilledRules(Boolean fulfilledRules) {
        this.fulfilledRules = fulfilledRules;
    }
}
