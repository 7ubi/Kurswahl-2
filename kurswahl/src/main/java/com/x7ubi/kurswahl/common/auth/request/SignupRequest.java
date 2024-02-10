package com.x7ubi.kurswahl.common.auth.request;

public class SignupRequest {
    private String firstname;
    private String surname;

    public SignupRequest(String firstname, String surname) {
        this.firstname = firstname;
        this.surname = surname;
    }

    public SignupRequest() {
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
}
