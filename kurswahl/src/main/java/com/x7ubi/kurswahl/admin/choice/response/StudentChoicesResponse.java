package com.x7ubi.kurswahl.admin.choice.response;


import com.x7ubi.kurswahl.common.rule.response.RuleResponse;

import java.util.List;

public class StudentChoicesResponse extends StudentSurveillanceResponse {

    private List<RuleResponse> ruleResponses;

    private List<ChoiceResponse> choiceResponses;

    public List<RuleResponse> getRuleResponses() {
        return ruleResponses;
    }

    public void setRuleResponses(List<RuleResponse> ruleResponses) {
        this.ruleResponses = ruleResponses;
    }

    public List<ChoiceResponse> getChoiceResponses() {
        return choiceResponses;
    }

    public void setChoiceResponses(List<ChoiceResponse> choiceResponses) {
        this.choiceResponses = choiceResponses;
    }
}
