package com.x7ubi.kurswahl.common.models;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "RULE_SET")
public class RuleSet {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false, name = "rule_set_id")
    private Long ruleSetId;

    @Column(nullable = false)
    private Integer year;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST, mappedBy = "ruleSet")
    private Set<Rule> rules;

    public Long getRuleSetId() {
        return ruleSetId;
    }

    public void setRuleSetId(Long ruleSetId) {
        this.ruleSetId = ruleSetId;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Set<Rule> getRules() {
        return rules;
    }

    public void setRules(Set<Rule> rules) {
        this.rules = rules;
    }
}
