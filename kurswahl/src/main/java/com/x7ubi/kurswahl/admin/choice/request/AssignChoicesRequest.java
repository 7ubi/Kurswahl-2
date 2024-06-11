package com.x7ubi.kurswahl.admin.choice.request;

import java.util.List;

public class AssignChoicesRequest {
    private List<Long> studentIds;
    private Long classId;
    private Integer choiceNumber;

    public List<Long> getStudentIds() {
        return studentIds;
    }

    public void setStudentIds(List<Long> studentIds) {
        this.studentIds = studentIds;
    }

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

    public Integer getChoiceNumber() {
        return choiceNumber;
    }

    public void setChoiceNumber(Integer choiceNumber) {
        this.choiceNumber = choiceNumber;
    }
}
