package com.x7ubi.kurswahl.admin.user.request;

public class StudentCsvRequest {
    private String csv;

    private Integer year;

    public String getCsv() {
        return csv;
    }

    public void setCsv(String csv) {
        this.csv = csv;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }
}
