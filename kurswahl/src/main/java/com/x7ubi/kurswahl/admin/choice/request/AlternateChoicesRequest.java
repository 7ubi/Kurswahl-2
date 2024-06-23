package com.x7ubi.kurswahl.admin.choice.request;

import java.util.List;

public class AlternateChoicesRequest {

    private List<Long> studentIds;

    private Long classId;

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
}
