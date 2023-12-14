package com.x7ubi.kurswahl.student.choice.response;

import java.util.List;

public class ChoiceResponse {
    private Integer choiceNumber;

    private List<ClassResponse> classResponses;

    public Integer getChoiceNumber() {
        return choiceNumber;
    }

    public void setChoiceNumber(Integer choiceNumber) {
        this.choiceNumber = choiceNumber;
    }

    public List<ClassResponse> getClassResponses() {
        return classResponses;
    }

    public void setClassResponses(List<ClassResponse> classResponses) {
        this.classResponses = classResponses;
    }
}
