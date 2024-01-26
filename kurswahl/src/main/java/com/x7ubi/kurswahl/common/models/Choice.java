package com.x7ubi.kurswahl.common.models;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "CHOICE")
public class Choice {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    private Long choiceId;

    @Column(nullable = false)
    private Integer choiceNumber;

    @Column(nullable = false)
    private Integer releaseYear;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn()
    private Student student;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinColumn()
    private Set<ChoiceClass> choiceClasses;

    public Choice() {
    }

    public Long getChoiceId() {
        return choiceId;
    }

    public void setChoiceId(Long choiceId) {
        this.choiceId = choiceId;
    }

    public Integer getChoiceNumber() {
        return choiceNumber;
    }

    public void setChoiceNumber(Integer choiceNumber) {
        this.choiceNumber = choiceNumber;
    }

    public Integer getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(Integer releaseYear) {
        this.releaseYear = releaseYear;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Set<ChoiceClass> getChoiceClasses() {
        return choiceClasses;
    }

    public void setChoiceClasses(Set<ChoiceClass> choiceClasses) {
        this.choiceClasses = choiceClasses;
    }
}
