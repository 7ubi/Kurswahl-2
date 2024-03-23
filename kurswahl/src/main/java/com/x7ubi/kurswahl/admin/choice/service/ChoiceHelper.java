package com.x7ubi.kurswahl.admin.choice.service;

import com.x7ubi.kurswahl.admin.choice.response.ClassStudentsResponse;
import com.x7ubi.kurswahl.common.settings.service.SettingsService;

import java.util.List;

public class ChoiceHelper {
    public static void setClassStudentsResponseWarnings(List<ClassStudentsResponse> classStudentsResponses,
                                                        SettingsService settingsService) {
        Integer sizeWarning = settingsService.getOrCreateSetting(SettingsService.CLASS_SIZE_WARNING,
                SettingsService.CLASS_SIZE_WARNING_DEFAULT_VALUE).getValue();
        Integer sizeCritical = settingsService.getOrCreateSetting(SettingsService.CLASS_SIZE_CRITICAL,
                SettingsService.CLASS_SIZE_CRITICAL_DEFAULT_VALUE).getValue();
        classStudentsResponses.forEach(classStudentsResponse -> {
            classStudentsResponse.setSizeWarning(classStudentsResponse.getStudentSurveillanceResponses().size() >= sizeWarning);
            classStudentsResponse.setSizeCritical(classStudentsResponse.getStudentSurveillanceResponses().size() >= sizeCritical);
        });
    }
}