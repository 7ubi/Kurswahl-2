package com.x7ubi.kurswahl.common.settings.service;

import com.x7ubi.kurswahl.common.models.Setting;
import com.x7ubi.kurswahl.common.repository.SettingRepo;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SettingsService {

    public static final String CLASS_SIZE_WARNING = "CLASS_SIZE_WARNING";

    public static final Integer CLASS_SIZE_WARNING_DEFAULT_VALUE = 20;

    public static final String CLASS_SIZE_CRITICAL = "CLASS_SIZE_CRITICAL";

    public static final Integer CLASS_SIZE_CRITICAL_DEFAULT_VALUE = 25;

    private final SettingRepo settingRepo;

    public SettingsService(SettingRepo settingRepo) {
        this.settingRepo = settingRepo;
    }

    public Setting getOrCreateSetting(String name, Integer defaultValue) {
        Optional<Setting> settingOptional = this.settingRepo.findSettingByName(name);

        if (settingOptional.isPresent()) {
            return settingOptional.get();
        } else {
            Setting setting = new Setting();
            setting.setName(name);
            setting.setValue(defaultValue);

            return this.settingRepo.save(setting);
        }
    }

    public void saveSetting(String name, Integer newValue) {

        Optional<Setting> settingOptional = this.settingRepo.findSettingByName(name);

        Setting setting;
        if (settingOptional.isPresent()) {
            setting = settingOptional.get();
        } else {
            setting = new Setting();
            setting.setName(name);
        }
        setting.setValue(newValue);
        this.settingRepo.save(setting);
    }
}
