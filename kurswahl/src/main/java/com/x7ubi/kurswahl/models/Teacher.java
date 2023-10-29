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

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Class> classes;

    @OneToMany
    private Set<StudentClass> studentClasses;

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
}