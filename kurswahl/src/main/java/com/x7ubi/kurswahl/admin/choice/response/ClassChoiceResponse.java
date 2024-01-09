package com.x7ubi.kurswahl.admin.choice.response;

import com.x7ubi.kurswahl.admin.user.response.TeacherResponse;

import java.util.List;

public class ClassChoiceResponse {

    private String name;

    private TeacherResponse teacherResponse;

    private List<ChoiceResponse> choiceResponses;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TeacherResponse getTeacherResponse() {
        return teacherResponse;
    }

    public void setTeacherResponse(TeacherResponse teacherResponse) {
        this.teacherResponse = teacherResponse;
    }

    public List<ChoiceResponse> getChoiceResponses() {
        return choiceResponses;
    }

    public void setChoiceResponses(List<ChoiceResponse> choiceResponses) {
        this.choiceResponses = choiceResponses;
    }
}
