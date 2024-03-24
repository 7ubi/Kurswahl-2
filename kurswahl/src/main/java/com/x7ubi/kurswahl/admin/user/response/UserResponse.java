package com.x7ubi.kurswahl.admin.user.response;

import io.swagger.v3.oas.annotations.media.Schema;

public class UserResponse {

    @Schema(type = "Long", example = "1")
    private Long userId;
    @Schema(type = "String", example = "test.test")
    private String username;

    @Schema(type = "String", example = "Max")
    private String firstname;

    @Schema(type = "String", example = "Mustermann")
    private String surname;

    @Schema(type = "String", example = "password123")
    private String generatedPassword;

    public UserResponse() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getGeneratedPassword() {
        return generatedPassword;
    }

    public void setGeneratedPassword(String generatedPassword) {
        this.generatedPassword = generatedPassword;
    }
}
