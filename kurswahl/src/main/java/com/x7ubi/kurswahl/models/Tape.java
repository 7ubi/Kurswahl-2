package com.x7ubi.kurswahl.models;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "TAPE")
public class Tape {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    private Long tapeId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Boolean isLk;

    @Column(nullable = false)
    private int year;

    @Column(nullable = false)
    private int releaseYear;

    @OneToOne(cascade = CascadeType.ALL)
    private Class aClass;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Lesson> lessons;

    public Tape() {}

    public Long getTapeId() {
        return tapeId;
    }

    public void setTapeId(Long tapeId) {
        this.tapeId = tapeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getLk() {
        return isLk;
    }

    public void setLk(Boolean lk) {
        isLk = lk;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public Class getaClass() {
        return aClass;
    }

    public void setaClass(Class aClass) {
        this.aClass = aClass;
    }

    public Set<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(Set<Lesson> lessons) {
        this.lessons = lessons;
    }
}
