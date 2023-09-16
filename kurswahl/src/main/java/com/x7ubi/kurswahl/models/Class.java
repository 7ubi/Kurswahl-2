package com.x7ubi.kurswahl.models;

import jakarta.persistence.*;

@Entity
@Table(name = "CLASS")
public class Class {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    private Long classId;

    @Column(length = 100, nullable = false)
    private String name;

    @ManyToOne(cascade = CascadeType.ALL)
    @Column(nullable = true)
    private Teacher teacher;

    @ManyToOne(cascade = CascadeType.ALL)
    @Column(nullable = false)
    private Subject subject;

    @OneToOne(cascade = CascadeType.ALL)
    @Column(nullable = false)
    private Tape tape;

    public Class() {}

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Tape getTape() {
        return tape;
    }

    public void setTape(Tape tape) {
        this.tape = tape;
    }
}
