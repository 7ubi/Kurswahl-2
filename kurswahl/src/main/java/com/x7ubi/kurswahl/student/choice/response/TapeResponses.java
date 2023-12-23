package com.x7ubi.kurswahl.student.choice.response;

import java.util.List;

public class TapeResponses {
    private List<TapeClassResponse> tapeClassResponses;

    private List<SubjectTapeResponse> subjectTapeResponses;

    public List<TapeClassResponse> getTapeClassResponses() {
        return tapeClassResponses;
    }

    public void setTapeClassResponses(List<TapeClassResponse> tapeClassResponses) {
        this.tapeClassResponses = tapeClassResponses;
    }

    public List<SubjectTapeResponse> getSubjectTapeResponses() {
        return subjectTapeResponses;
    }

    public void setSubjectTapeResponses(List<SubjectTapeResponse> subjectTapeResponses) {
        this.subjectTapeResponses = subjectTapeResponses;
    }
}
