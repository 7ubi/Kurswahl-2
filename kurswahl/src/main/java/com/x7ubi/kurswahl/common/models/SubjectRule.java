package com.x7ubi.kurswahl.common.models;

import jakarta.persistence.*;

@Entity
@Table(name = "SUBJECT_RULE")
public class SubjectRule {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    private Long subjectRuleId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rule_id")
    private Rule rule;

    public Long getSubjectRuleId() {
        return subjectRuleId;
    }

    public void setSubjectRuleId(Long subjectRuleId) {
        this.subjectRuleId = subjectRuleId;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Rule getRule() {
        return rule;
    }

    public void setRule(Rule rule) {
        this.rule = rule;
    }
}
