package com.x7ubi.kurswahl.student.choice.response;

import java.util.List;

public class RuleResponse {
    private String name;

    private List<SubjectResponse> subjectResponses;

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
