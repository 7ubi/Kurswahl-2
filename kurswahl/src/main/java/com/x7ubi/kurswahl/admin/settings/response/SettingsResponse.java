package com.x7ubi.kurswahl.admin.settings.response;

public class SettingsResponse {

    private Integer classSizeWarning;

    private Integer classSizeCritical;

    private boolean choiceOpen;

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

    public boolean isChoiceOpen() {
        return choiceOpen;
    }

    public void setChoiceOpen(boolean choiceOpen) {
        this.choiceOpen = choiceOpen;
    }
}
