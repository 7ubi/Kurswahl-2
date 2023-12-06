package com.x7ubi.kurswahl.student.request;

public class AlterStudentChoiceRequest {

    private Integer choiceNumber;

    private Long classId;

    public AlterStudentChoiceRequest() {
    }

    public Integer getChoiceNumber() {
        return choiceNumber;
    }

    public void setChoiceNumber(Integer choiceNumber) {
        this.choiceNumber = choiceNumber;
    }

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }
}
