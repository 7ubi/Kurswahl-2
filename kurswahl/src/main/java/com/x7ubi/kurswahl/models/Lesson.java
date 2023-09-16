package com.x7ubi.kurswahl.models;

import jakarta.persistence.*;

@Entity
@Table(name = "LESSON")
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    private Long lessonId;

    @Column(nullable = false)
    private int day;

    @Column(nullable = false)
    private int hour;

    @Column(nullable = false)
    private int releaseYear;

    @ManyToOne(cascade = CascadeType.ALL)
    @Column(nullable = true)
    private Tape tape;



    public Lesson() {}

    public Long getLessonId() {
        return lessonId;
    }

    public void setLessonId(Long lessonId) {
        this.lessonId = lessonId;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public Tape getTape() {
        return tape;
    }

    public void setTape(Tape tape) {
        this.tape = tape;
    }
}
