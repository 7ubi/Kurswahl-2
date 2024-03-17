package com.x7ubi.kurswahl.admin.settings.service;

import com.x7ubi.kurswahl.admin.settings.response.ClassSizeSettingResponse;
import com.x7ubi.kurswahl.common.repository.SettingRepo;
import com.x7ubi.kurswahl.common.settings.service.SettingsService;
import org.springframework.stereotype.Service;

@Service
public class AdminSettingsService {

    private final SettingsService settingsService;

    private final SettingRepo settingRepo;

    public AdminSettingsService(SettingsService settingsService, SettingRepo settingRepo) {
        this.settingsService = settingsService;
        this.settingRepo = settingRepo;
    }

    public ClassSizeSettingResponse getClassSizeSettings() {
        ClassSizeSettingResponse response = new ClassSizeSettingResponse();

        response.setClassSizeCritical(this.settingsService.getOrCreateSetting(SettingsService.CLASS_SIZE_CRITICAL,
                SettingsService.CLASS_SIZE_CRITICAL_DEFAULT_VALUE).getValue());
        response.setClassSizeWarning(this.settingsService.getOrCreateSetting(SettingsService.CLASS_SIZE_WARNING,
                SettingsService.CLASS_SIZE_WARNING_DEFAULT_VALUE).getValue());

        return response;
    }
}
