package com.x7ubi.kurswahl.admin.request;

public class LessonCreationRequest {

    Integer day;

    Integer hour;

    Long tapeId;

    public LessonCreationRequest() {
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Integer getHour() {
        return hour;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }

    public Long getTapeId() {
        return tapeId;
    }

    public void setTapeId(Long tapeId) {
        this.tapeId = tapeId;
    }
}
