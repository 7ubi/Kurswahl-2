package com.x7ubi.kurswahl.admin.choice.response;

import java.util.List;

public class StudentChoicesResponse extends StudentSurveillanceResponse {

    private List<ChoiceResponse> choiceResponses;

    public List<ChoiceResponse> getChoiceResponses() {
        return choiceResponses;
    }

    public void setChoiceResponses(List<ChoiceResponse> choiceResponses) {
        this.choiceResponses = choiceResponses;
    }
}
