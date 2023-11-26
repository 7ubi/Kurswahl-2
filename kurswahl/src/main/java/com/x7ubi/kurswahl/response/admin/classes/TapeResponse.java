package com.x7ubi.kurswahl.response.admin.classes;

public class TapeResponse {
    private Long tapeId;

    private String name;


    private Boolean isLk;

    private Integer year;

    private Integer releaseYear;

    public TapeResponse() {
    }

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
}
