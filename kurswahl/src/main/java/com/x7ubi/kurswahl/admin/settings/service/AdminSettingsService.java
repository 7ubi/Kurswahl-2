package com.x7ubi.kurswahl.admin.settings.service;

import com.x7ubi.kurswahl.admin.settings.request.EditSettingsRequest;
import com.x7ubi.kurswahl.admin.settings.response.SettingsResponse;
import com.x7ubi.kurswahl.common.models.Setting;
import com.x7ubi.kurswahl.common.settings.service.SettingsService;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Service;

@Service
public class AdminSettingsService {

    private final SettingsService settingsService;

    public AdminSettingsService(SettingsService settingsService) {
        this.settingsService = settingsService;
    }

    public SettingsResponse getSettings() {
        SettingsResponse response = new SettingsResponse();

        response.setClassSizeCritical(this.settingsService.getOrCreateSetting(SettingsService.CLASS_SIZE_CRITICAL,
                SettingsService.CLASS_SIZE_CRITICAL_DEFAULT_VALUE).getValue());
        response.setClassSizeWarning(this.settingsService.getOrCreateSetting(SettingsService.CLASS_SIZE_WARNING,
                SettingsService.CLASS_SIZE_WARNING_DEFAULT_VALUE).getValue());
        response.setChoiceOpen(BooleanUtils.toBoolean(this.settingsService.getOrCreateSetting(SettingsService.CHOICE_OPEN,
                SettingsService.CHOICE_OPEN_DEFAULT_VALUE).getValue()));

        return response;
    }

    public SettingsResponse editSettings(EditSettingsRequest editSettingsRequest) {
        SettingsResponse response = new SettingsResponse();

        Setting classSizeWarning = this.settingsService.updateSetting(SettingsService.CLASS_SIZE_WARNING,
                editSettingsRequest.getClassSizeWarning());
        response.setClassSizeWarning(classSizeWarning.getValue());
        Setting classSizeCritical = this.settingsService.updateSetting(SettingsService.CLASS_SIZE_CRITICAL,
                editSettingsRequest.getClassSizeCritical());
        response.setClassSizeCritical(classSizeCritical.getValue());
        Setting choiceOpen = this.settingsService.updateSetting(SettingsService.CHOICE_OPEN,
                editSettingsRequest.isChoiceOpen());
        response.setChoiceOpen(BooleanUtils.toBoolean(choiceOpen.getValue()));

        return response;
    }
}
