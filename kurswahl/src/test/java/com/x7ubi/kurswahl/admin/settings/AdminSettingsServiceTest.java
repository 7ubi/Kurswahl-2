package com.x7ubi.kurswahl.admin.settings;

import com.x7ubi.kurswahl.KurswahlServiceTest;
import com.x7ubi.kurswahl.admin.settings.request.EditSettingsRequest;
import com.x7ubi.kurswahl.admin.settings.response.SettingsResponse;
import com.x7ubi.kurswahl.admin.settings.service.AdminSettingsService;
import com.x7ubi.kurswahl.common.settings.service.SettingsService;
import org.apache.commons.lang3.BooleanUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@KurswahlServiceTest
public class AdminSettingsServiceTest {

    @Autowired
    private AdminSettingsService adminSettingsService;

    @Test
    public void testGetClassSizeSettings() {
        // When
        SettingsResponse result = this.adminSettingsService.getSettings();

        // Then
        Assertions.assertEquals(result.getClassSizeWarning(), SettingsService.CLASS_SIZE_WARNING_DEFAULT_VALUE);
        Assertions.assertEquals(result.getClassSizeCritical(), SettingsService.CLASS_SIZE_CRITICAL_DEFAULT_VALUE);
        Assertions.assertTrue(BooleanUtils.toBoolean(result.isChoiceOpen()));
    }

    @Test
    public void testEditClassSizeSettings() {
        // Given
        EditSettingsRequest editSettingsRequest = new EditSettingsRequest();
        editSettingsRequest.setClassSizeWarning(3);
        editSettingsRequest.setClassSizeCritical(5);
        editSettingsRequest.setChoiceOpen(false);

        // When
        SettingsResponse result = this.adminSettingsService.editSettings(editSettingsRequest);

        // Then
        Assertions.assertEquals(result.getClassSizeWarning(), 3);
        Assertions.assertEquals(result.getClassSizeCritical(), 5);
        Assertions.assertFalse(BooleanUtils.toBoolean(result.isChoiceOpen()));
    }
}
