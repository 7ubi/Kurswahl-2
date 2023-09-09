package com.x7ubi.kurswahl.request.admin;

import com.x7ubi.kurswahl.request.auth.SignupRequest;

public class AdminSignupRequest extends SignupRequest {

    AdminSignupRequest() {}

    AdminSignupRequest(String username, String firstname, String surname, String password) {
        super(username, firstname, surname, password);
    }
}
