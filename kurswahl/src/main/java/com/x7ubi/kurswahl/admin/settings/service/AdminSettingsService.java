package com.x7ubi.kurswahl.admin.settings.service;

import com.x7ubi.kurswahl.admin.settings.request.EditClassSizeRequest;
import com.x7ubi.kurswahl.admin.settings.response.ClassSizeSettingResponse;
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

        this.settingsService.saveSetting(SettingsService.CLASS_SIZE_WARNING, editClassSizeRequest.getClassSizeWarning());
        this.settingsService.saveSetting(SettingsService.CLASS_SIZE_CRITICAL, editClassSizeRequest.getClassSizeCritical());
        
        return getClassSizeSettings();
    }
}
