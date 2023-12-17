package com.x7ubi.kurswahl.student.choice.response;

import java.util.List;

public class TapeClassResponse {

    private Long tapeId;

    private String name;

    private List<ClassResponse> classResponses;

    private List<LessonResponse> lessonResponses;

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

    public List<ClassResponse> getClassResponses() {
        return classResponses;
    }

    public void setClassResponses(List<ClassResponse> classResponses) {
        this.classResponses = classResponses;
    }

    public List<LessonResponse> getLessonResponses() {
        return lessonResponses;
    }

    public void setLessonResponses(List<LessonResponse> lessonResponses) {
        this.lessonResponses = lessonResponses;
    }
}
