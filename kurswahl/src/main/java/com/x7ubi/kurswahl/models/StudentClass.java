package com.x7ubi.kurswahl.models;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "STUDENT_CLASS")
public class StudentClass {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    private Long studentClassId;

    @Column(nullable = false)
    private String name;

    @OneToMany
    private Set<Student> students;

    @ManyToOne
    private Teacher teacher;

    @Column(nullable = false)
    private Integer year;

    @Column(nullable = false)
    private Integer releaseYear;

    public StudentClass() {}

    public Long getStudentClassId() {
        return studentClassId;
    }

    public void setStudentClassId(Long studentClassId) {
        this.studentClassId = studentClassId;
    }

    public Set<Student> getStudents() {
        return students;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(Integer releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
