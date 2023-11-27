package com.x7ubi.kurswahl.models;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "TEACHER")
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    private Long teacherId;

    @OneToOne(cascade = CascadeType.ALL)
    private User user;

    @OneToMany(fetch = FetchType.EAGER)
    private Set<Class> classes;

    @OneToMany(fetch = FetchType.EAGER)
    private Set<StudentClass> studentClasses;

    @Column(nullable = false)
    private String abbreviation;

    public Teacher() {
    }

    public Teacher(Long teacherId, User user) {
        this.teacherId = teacherId;
        this.user = user;
    }

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Class> getClasses() {
        return classes;
    }

    public void setClasses(Set<Class> classes) {
        this.classes = classes;
    }

    public Set<StudentClass> getStudentClasses() {
        return studentClasses;
    }

    public void setStudentClasses(Set<StudentClass> studentClasses) {
        this.studentClasses = studentClasses;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }
}
