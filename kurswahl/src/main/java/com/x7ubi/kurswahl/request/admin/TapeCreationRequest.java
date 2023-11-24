package com.x7ubi.kurswahl.request.admin;

public class TapeCreationRequest {
    private String name;

    private Boolean isLk;

    private Integer year;

    public TapeCreationRequest() {
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

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }
}
