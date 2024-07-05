package com.x7ubi.kurswahl.admin.settings.response;

public class SettingsResponse {

    private Integer classSizeWarning;

    private Integer classSizeCritical;

    private boolean choiceOpen;

    private boolean resultOpen11;

    private boolean resultOpen12;

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

    public boolean isResultOpen11() {
        return resultOpen11;
    }

    public void setResultOpen11(boolean resultOpen11) {
        this.resultOpen11 = resultOpen11;
    }

    public boolean isResultOpen12() {
        return resultOpen12;
    }

    public void setResultOpen12(boolean resultOpen12) {
        this.resultOpen12 = resultOpen12;
    }
}
