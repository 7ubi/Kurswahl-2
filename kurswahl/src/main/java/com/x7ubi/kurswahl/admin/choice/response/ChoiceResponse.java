package com.x7ubi.kurswahl.admin.choice.response;

import java.util.List;

public class ChoiceResponse {
    private Integer choiceNumber;

    private List<ClassChoiceResponse> classChoiceResponses;

    public Integer getChoiceNumber() {
        return choiceNumber;
    }

    public void setChoiceNumber(Integer choiceNumber) {
        this.choiceNumber = choiceNumber;
    }

    public List<ClassChoiceResponse> getClassChoiceResponses() {
        return classChoiceResponses;
    }

    public void setClassChoiceResponses(List<ClassChoiceResponse> classChoiceResponses) {
        this.classChoiceResponses = classChoiceResponses;
    }
}
