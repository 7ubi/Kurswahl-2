package com.x7ubi.kurswahl.admin.classes;

import com.x7ubi.kurswahl.KurswahlServiceTest;
import com.x7ubi.kurswahl.error.ErrorMessage;
import com.x7ubi.kurswahl.models.SubjectArea;
import com.x7ubi.kurswahl.repository.SubjectAreaRepo;
import com.x7ubi.kurswahl.request.admin.SubjectAreaCreationRequest;
import com.x7ubi.kurswahl.response.common.ResultResponse;
import com.x7ubi.kurswahl.service.admin.classes.SubjectAreaCreationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@KurswahlServiceTest
public class SubjectAreaCreationServiceTest {

    @Autowired
    private SubjectAreaCreationService subjectAreaCreationService;

    @Autowired
    private SubjectAreaRepo subjectAreaRepo;

    private SubjectArea subjectArea;

    @BeforeEach
    public void setupTests() {
        subjectArea = new SubjectArea();
        subjectArea.setName("test");

        this.subjectAreaRepo.save(subjectArea);
    }

    @Test
    public void testCreateSubjectArea() {
        // Given
        SubjectAreaCreationRequest subjectAreaCreationRequest = new SubjectAreaCreationRequest();
        subjectAreaCreationRequest.setName("subject area");

        // When
        ResultResponse response = this.subjectAreaCreationService.createSubjectArea(subjectAreaCreationRequest);

        // Then
        SubjectArea createdSubjectArea = this.subjectAreaRepo.findSubjectAreaByName("subject area").get();
        Assertions.assertTrue(response.getErrorMessages().isEmpty());
        Assertions.assertEquals(createdSubjectArea.getName(), subjectAreaCreationRequest.getName());
    }

    @Test
    public void testCreateSubjectAreaNameExists() {
        // Given
        SubjectAreaCreationRequest subjectAreaCreationRequest = new SubjectAreaCreationRequest();
        subjectAreaCreationRequest.setName("test");

        // When
        ResultResponse response = this.subjectAreaCreationService.createSubjectArea(subjectAreaCreationRequest);

        // Then
        SubjectArea createdSubjectArea = this.subjectAreaRepo.findSubjectAreaByName("test").get();
        Assertions.assertEquals(response.getErrorMessages().size(), 1);
        Assertions.assertEquals(response.getErrorMessages().get(0).getMessage(), ErrorMessage.Administration.SUBJECT_AREA_ALREADY_EXISTS);
        Assertions.assertEquals(createdSubjectArea.getName(), subjectArea.getName());
    }

    @Test
    public void testDeleteSubjectArea() {
        // Given
        this.subjectArea = this.subjectAreaRepo.findSubjectAreaByName(this.subjectArea.getName()).get();
        Long id = this.subjectArea.getSubjectAreaId();

        // When
        ResultResponse response = this.subjectAreaCreationService.deleteSubjectArea(id);

        // Then
        Assertions.assertTrue(response.getErrorMessages().isEmpty());
        Assertions.assertFalse(this.subjectAreaRepo.existsSubjectAreaByName(this.subjectArea.getName()));
    }

    @Test
    public void testDeleteSubjectAreaWrongId() {
        // Given
        this.subjectArea = this.subjectAreaRepo.findSubjectAreaByName(this.subjectArea.getName()).get();
        Long id = this.subjectArea.getSubjectAreaId() + 1;

        // When
        ResultResponse response = this.subjectAreaCreationService.deleteSubjectArea(id);

        // Then
        Assertions.assertEquals(response.getErrorMessages().size(), 1);
        Assertions.assertEquals(response.getErrorMessages().get(0).getMessage(), ErrorMessage.Administration.SUBJECT_AREA_NOT_FOUND);
        Assertions.assertTrue(this.subjectAreaRepo.existsSubjectAreaByName(this.subjectArea.getName()));
    }
}
