package com.x7ubi.kurswahl.student.choice.response;

import java.util.List;

public class ChoiceResponse {

    private Long choiceId;

    private Integer choiceNumber;

    private List<ClassChoiceResponse> classChoiceResponses;

    public Long getChoiceId() {
        return choiceId;
    }

    public void setChoiceId(Long choiceId) {
        this.choiceId = choiceId;
    }

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
