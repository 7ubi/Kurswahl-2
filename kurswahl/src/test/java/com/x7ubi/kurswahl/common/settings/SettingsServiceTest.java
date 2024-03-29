package com.x7ubi.kurswahl.common.settings;

import com.x7ubi.kurswahl.KurswahlServiceTest;
import com.x7ubi.kurswahl.common.models.Setting;
import com.x7ubi.kurswahl.common.repository.SettingRepo;
import com.x7ubi.kurswahl.common.settings.service.SettingsService;
import org.apache.commons.lang3.BooleanUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@KurswahlServiceTest
public class SettingsServiceTest {

    @Autowired
    private SettingsService settingsService;

    @Autowired
    private SettingRepo settingRepo;

    private void setupSetting() {
        Setting setting = new Setting();
        setting.setName(SettingsService.CLASS_SIZE_WARNING);
        setting.setValue(SettingsService.CLASS_SIZE_WARNING_DEFAULT_VALUE);

        this.settingRepo.save(setting);
    }

    private void setupBooleanSetting() {
        Setting setting = new Setting();
        setting.setName(SettingsService.CHOICE_OPEN);
        setting.setValue(BooleanUtils.toInteger(SettingsService.CHOICE_OPEN_DEFAULT_VALUE));

        this.settingRepo.save(setting);
    }

    @Test
    public void testGetOrCreateSetting() {
        // Given
        setupSetting();

        // When
        Setting result = this.settingsService.getOrCreateSetting(SettingsService.CLASS_SIZE_WARNING,
                5);

        // Then
        Assertions.assertNotNull(result.getSettingId());
        Assertions.assertEquals(result.getName(), SettingsService.CLASS_SIZE_WARNING);
        Assertions.assertEquals(result.getValue(), SettingsService.CLASS_SIZE_WARNING_DEFAULT_VALUE);
    }

    @Test
    public void testGetOrCreateSettingCreatesSetting() {
        // When
        Setting result = this.settingsService.getOrCreateSetting(SettingsService.CLASS_SIZE_WARNING,
                SettingsService.CLASS_SIZE_WARNING_DEFAULT_VALUE);

        // Then
        Assertions.assertNotNull(result.getSettingId());
        Assertions.assertEquals(result.getName(), SettingsService.CLASS_SIZE_WARNING);
        Assertions.assertEquals(result.getValue(), SettingsService.CLASS_SIZE_WARNING_DEFAULT_VALUE);
    }

    @Test
    public void testGetOrCreateSettingBoolean() {
        // Given
        setupBooleanSetting();

        // When
        Setting result = this.settingsService.getOrCreateSetting(SettingsService.CHOICE_OPEN,
                false);

        // Then
        Assertions.assertNotNull(result.getSettingId());
        Assertions.assertEquals(result.getName(), SettingsService.CHOICE_OPEN);
        Assertions.assertEquals(result.getValue(), 1);
        Assertions.assertTrue(BooleanUtils.toBoolean(result.getValue()));
    }

    @Test
    public void testUpdateSetting() {
        // Given
        setupSetting();

        // When
        Setting result = this.settingsService.updateSetting(SettingsService.CLASS_SIZE_WARNING, 5);

        // Then
        Assertions.assertNotNull(result.getSettingId());
        Assertions.assertEquals(result.getName(), SettingsService.CLASS_SIZE_WARNING);
        Assertions.assertEquals(result.getValue(), 5);
    }

    @Test
    public void testUpdateSettingCreatesSetting() {
        // When
        Setting result = this.settingsService.updateSetting(SettingsService.CLASS_SIZE_WARNING, 5);

        // Then
        Assertions.assertNotNull(result.getSettingId());
        Assertions.assertEquals(result.getName(), SettingsService.CLASS_SIZE_WARNING);
        Assertions.assertEquals(result.getValue(), 5);
    }

    @Test
    public void testUpdateSettingBoolean() {
        // Given
        setupBooleanSetting();

        // When
        Setting result = this.settingsService.updateSetting(SettingsService.CHOICE_OPEN, false);

        // Then
        Assertions.assertNotNull(result.getSettingId());
        Assertions.assertEquals(result.getName(), SettingsService.CHOICE_OPEN);
        Assertions.assertEquals(result.getValue(), 0);
        Assertions.assertFalse(BooleanUtils.toBoolean(result.getValue()));
    }
}
