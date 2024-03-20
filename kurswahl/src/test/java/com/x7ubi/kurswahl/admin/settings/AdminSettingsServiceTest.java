package com.x7ubi.kurswahl.admin.settings;

import com.x7ubi.kurswahl.KurswahlServiceTest;
import com.x7ubi.kurswahl.admin.settings.request.EditClassSizeRequest;
import com.x7ubi.kurswahl.admin.settings.response.ClassSizeSettingResponse;
import com.x7ubi.kurswahl.admin.settings.service.AdminSettingsService;
import com.x7ubi.kurswahl.common.settings.service.SettingsService;
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
        ClassSizeSettingResponse result = this.adminSettingsService.getClassSizeSettings();

        // Then
        Assertions.assertEquals(result.getClassSizeWarning(), SettingsService.CLASS_SIZE_WARNING_DEFAULT_VALUE);
        Assertions.assertEquals(result.getClassSizeCritical(), SettingsService.CLASS_SIZE_CRITICAL_DEFAULT_VALUE);
    }

    @Test
    public void testEditClassSizeSettings() {
        // Given
        EditClassSizeRequest editClassSizeRequest = new EditClassSizeRequest();
        editClassSizeRequest.setClassSizeWarning(3);
        editClassSizeRequest.setClassSizeCritical(5);

        // When
        ClassSizeSettingResponse result = this.adminSettingsService.editClassSizeSettings(editClassSizeRequest);

        // Then
        Assertions.assertEquals(result.getClassSizeWarning(), 3);
        Assertions.assertEquals(result.getClassSizeCritical(), 5);
    }
}
