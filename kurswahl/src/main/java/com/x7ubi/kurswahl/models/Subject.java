package com.x7ubi.kurswahl.models;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "SUBJECT")
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    private Long subjectId;

    @Column(length = 100, nullable = false)
    private String name;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Class> classes;

    @ManyToOne()
    @JoinColumn(nullable = false)
    private SubjectArea subjectArea;

    public Subject() {}

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
}
