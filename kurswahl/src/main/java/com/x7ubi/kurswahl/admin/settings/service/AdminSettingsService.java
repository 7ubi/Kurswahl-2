package com.x7ubi.kurswahl.admin.settings.service;

import com.x7ubi.kurswahl.admin.settings.request.EditClassSizeRequest;
import com.x7ubi.kurswahl.admin.settings.response.ClassSizeSettingResponse;
import com.x7ubi.kurswahl.common.models.Setting;
import com.x7ubi.kurswahl.common.settings.service.SettingsService;
import org.springframework.stereotype.Service;

@Service
public class AdminSettingsService {

    private final SettingsService settingsService;

    public AdminSettingsService(SettingsService settingsService) {
        this.settingsService = settingsService;
    }

    public ClassSizeSettingResponse getClassSizeSettings() {
        ClassSizeSettingResponse response = new ClassSizeSettingResponse();

        response.setClassSizeCritical(this.settingsService.getOrCreateSetting(SettingsService.CLASS_SIZE_CRITICAL,
                SettingsService.CLASS_SIZE_CRITICAL_DEFAULT_VALUE).getValue());
        response.setClassSizeWarning(this.settingsService.getOrCreateSetting(SettingsService.CLASS_SIZE_WARNING,
                SettingsService.CLASS_SIZE_WARNING_DEFAULT_VALUE).getValue());

        return response;
    }

    public ClassSizeSettingResponse editClassSizeSettings(EditClassSizeRequest editClassSizeRequest) {
        ClassSizeSettingResponse response = new ClassSizeSettingResponse();

        Setting classSizeWarning = this.settingsService.updateSetting(SettingsService.CLASS_SIZE_WARNING,
                editClassSizeRequest.getClassSizeWarning());
        response.setClassSizeWarning(classSizeWarning.getValue());
        Setting classSizeCritical = this.settingsService.updateSetting(SettingsService.CLASS_SIZE_CRITICAL,
                editClassSizeRequest.getClassSizeCritical());
        response.setClassSizeCritical(classSizeCritical.getValue());

        return response;
    }
}
