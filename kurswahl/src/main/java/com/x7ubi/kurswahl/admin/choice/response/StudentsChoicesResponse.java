package com.x7ubi.kurswahl.admin.choice.response;

import java.util.List;

public class StudentsChoicesResponse {

    private List<StudentRuleResponse> studentRuleResponses;

    private List<ChoiceResponse> choiceResponses;

    public List<StudentRuleResponse> getStudentRuleResponses() {
        return studentRuleResponses;
    }

    public void setStudentRuleResponses(List<StudentRuleResponse> studentRuleResponses) {
        this.studentRuleResponses = studentRuleResponses;
    }

    public List<ChoiceResponse> getChoiceResponses() {
        return choiceResponses;
    }

    public void setChoiceResponses(List<ChoiceResponse> choiceResponses) {
        this.choiceResponses = choiceResponses;
    }
}
