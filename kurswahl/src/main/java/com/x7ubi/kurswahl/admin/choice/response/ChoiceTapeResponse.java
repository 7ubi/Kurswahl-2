package com.x7ubi.kurswahl.admin.choice.response;

import java.util.ArrayList;
import java.util.List;

public class ChoiceTapeResponse {
    private Long tapeId;

    private String name;

    private List<ChoiceTapeClassResponse> choiceTapeClassResponses = new ArrayList<>();

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

    public List<ChoiceTapeClassResponse> getChoiceTapeClassResponses() {
        return choiceTapeClassResponses;
    }

    public void setChoiceTapeClassResponse(List<ChoiceTapeClassResponse> choiceTapeClassResponses) {
        this.choiceTapeClassResponses = choiceTapeClassResponses;
    }
}
