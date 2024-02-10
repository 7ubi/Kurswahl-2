package com.x7ubi.kurswahl.student.choice.response;

import com.x7ubi.kurswahl.common.rule.response.RuleResponse;

import java.util.List;

public class ChoiceResponse {

    private Long choiceId;

    private Integer choiceNumber;

    private List<ClassChoiceResponse> classChoiceResponses;

    private List<RuleResponse> ruleResponses;

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

    public List<RuleResponse> getRuleResponses() {
        return ruleResponses;
    }

    public void setRuleResponses(List<RuleResponse> ruleResponses) {
        this.ruleResponses = ruleResponses;
    }
}
