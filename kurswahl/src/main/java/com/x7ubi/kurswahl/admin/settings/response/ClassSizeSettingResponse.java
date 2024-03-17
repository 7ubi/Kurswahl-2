package com.x7ubi.kurswahl.admin.settings.response;

public class ClassSizeSettingResponse {

    private Integer classSizeWarning;

    private Integer classSizeCritical;

    public Integer getClassSizeWarning() {
        return classSizeWarning;
    }

    public void setClassSizeWarning(Integer classSizeWarning) {
        this.classSizeWarning = classSizeWarning;
    }

    public Integer getClassSizeCritical() {
        return classSizeCritical;
    }

    public void setClassSizeCritical(Integer classSizeCritical) {
        this.classSizeCritical = classSizeCritical;
    }
}
