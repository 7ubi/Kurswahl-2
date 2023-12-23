package com.x7ubi.kurswahl.student.choice.response;

import java.util.List;

public class SubjectTapeResponse {

    private String name;

    private List<TapeResponse> tapeResponses;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TapeResponse> getTapeResponses() {
        return tapeResponses;
    }

    public void setTapeResponses(List<TapeResponse> tapeResponses) {
        this.tapeResponses = tapeResponses;
    }
}
