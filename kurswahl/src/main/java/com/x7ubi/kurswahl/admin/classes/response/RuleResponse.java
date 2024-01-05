package com.x7ubi.kurswahl.admin.classes.response;

import java.util.List;

public class RuleResponse {

    private Long ruleId;

    private String name;

    private Integer year;

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

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }
}
