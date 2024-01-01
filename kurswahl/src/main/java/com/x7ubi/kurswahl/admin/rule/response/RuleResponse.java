package com.x7ubi.kurswahl.admin.rule.response;

import com.x7ubi.kurswahl.admin.classes.response.SubjectResponse;

import java.util.List;

public class RuleResponse {

    private Long ruleId;

    private String name;

    private List<SubjectResponse> subjectResponses;

    public Long getRuleId() {
        return ruleId;
    }

    public void setRuleId(Long ruleId) {
        this.ruleId = ruleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SubjectResponse> getSubjectResponses() {
        return subjectResponses;
    }

    public void setSubjectResponses(List<SubjectResponse> subjectResponses) {
        this.subjectResponses = subjectResponses;
    }
}
