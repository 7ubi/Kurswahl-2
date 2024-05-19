package com.x7ubi.kurswahl.admin.choice.response;

import com.x7ubi.kurswahl.common.rule.response.RuleResponse;

import java.util.List;

public class StudentRuleResponse extends StudentSurveillanceResponse {

    private List<RuleResponse> ruleResponses;

    public List<RuleResponse> getRuleResponses() {
        return ruleResponses;
    }

    public void setRuleResponses(List<RuleResponse> ruleResponses) {
        this.ruleResponses = ruleResponses;
    }
}
