package com.x7ubi.kurswahl.admin.choice;

import com.x7ubi.kurswahl.KurswahlServiceTest;
import com.x7ubi.kurswahl.admin.choice.response.ClassStudentsResponse;
import com.x7ubi.kurswahl.admin.choice.response.StudentSurveillanceResponse;
import com.x7ubi.kurswahl.admin.choice.service.ChoiceHelper;
import com.x7ubi.kurswahl.common.settings.service.SettingsService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@KurswahlServiceTest
public class ChoiceHelperTest {

    @Autowired
    private SettingsService settingsService;

    @BeforeEach
    public void setupSettings() {
        this.settingsService.getOrCreateSetting(SettingsService.CLASS_SIZE_WARNING, 1);
        this.settingsService.getOrCreateSetting(SettingsService.CLASS_SIZE_CRITICAL, 2);
    }

    private List<ClassStudentsResponse> setupClassStudentsResponses(int numStudents) {

        ClassStudentsResponse classStudentsResponse = new ClassStudentsResponse();
        classStudentsResponse.setStudentSurveillanceResponses(new ArrayList<>());

        for (int i = 0; i < numStudents; i++) {
            StudentSurveillanceResponse studentSurveillanceResponse = new StudentSurveillanceResponse();

            classStudentsResponse.getStudentSurveillanceResponses().add(studentSurveillanceResponse);
        }

        return List.of(classStudentsResponse);
    }

    @Test
    public void testSetClassStudentsResponseWarningsSetWarning() {
        // given
        List<ClassStudentsResponse> classStudentsResponses = setupClassStudentsResponses(1);

        // when
        ChoiceHelper.setClassStudentsResponseWarnings(classStudentsResponses, settingsService);

        // then
        Assertions.assertTrue(classStudentsResponses.get(0).isSizeWarning());
        Assertions.assertFalse(classStudentsResponses.get(0).isSizeCritical());
    }

    @Test
    public void testSetClassStudentsResponseWarningsSetCritical() {
        // given
        List<ClassStudentsResponse> classStudentsResponses = setupClassStudentsResponses(2);

        // when
        ChoiceHelper.setClassStudentsResponseWarnings(classStudentsResponses, settingsService);

        // then
        Assertions.assertTrue(classStudentsResponses.get(0).isSizeWarning());
        Assertions.assertTrue(classStudentsResponses.get(0).isSizeCritical());
    }
}
