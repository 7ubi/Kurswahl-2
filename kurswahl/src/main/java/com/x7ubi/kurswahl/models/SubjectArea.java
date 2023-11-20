package com.x7ubi.kurswahl.models;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "SUBJECT_AREA")
public class SubjectArea {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    private Long subjectAreaId;

    @Column(length = 100)
    private String name;

    @OneToMany(fetch = FetchType.EAGER)
    private Set<Subject> subjects;

    public SubjectArea() {}

    public Long getSubjectAreaId() {
        return subjectAreaId;
    }

    public void setSubjectAreaId(Long subjectAreaId) {
        this.subjectAreaId = subjectAreaId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(Set<Subject> subjects) {
        this.subjects = subjects;
    }
}
