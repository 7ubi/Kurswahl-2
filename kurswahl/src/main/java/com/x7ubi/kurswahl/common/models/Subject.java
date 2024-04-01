package com.x7ubi.kurswahl.common.models;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "SUBJECT")
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false, name = "subject_id")
    private Long subjectId;

    @Column(length = 100, nullable = false)
    private String name;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "subject")
    private Set<Class> classes;

    @ManyToOne()
    @JoinColumn(nullable = false, name = "subject_area_id")
    private SubjectArea subjectArea;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "subject")
    private Set<SubjectRule> subjectRules;

    public Subject() {
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public Set<Class> getClasses() {
        return classes;
    }

    public void setClasses(Set<Class> classes) {
        this.classes = classes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SubjectArea getSubjectArea() {
        return subjectArea;
    }

    public void setSubjectArea(SubjectArea subjectArea) {
        this.subjectArea = subjectArea;
    }

    public Set<SubjectRule> getSubjectRules() {
        return subjectRules;
    }

    public void setSubjectRules(Set<SubjectRule> subjectRules) {
        this.subjectRules = subjectRules;
    }
}
