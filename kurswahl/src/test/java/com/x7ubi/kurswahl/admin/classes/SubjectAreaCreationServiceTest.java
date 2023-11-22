package com.x7ubi.kurswahl.admin.classes;

import com.x7ubi.kurswahl.KurswahlServiceTest;
import com.x7ubi.kurswahl.error.ErrorMessage;
import com.x7ubi.kurswahl.models.SubjectArea;
import com.x7ubi.kurswahl.repository.SubjectAreaRepo;
import com.x7ubi.kurswahl.request.admin.SubjectAreaCreationRequest;
import com.x7ubi.kurswahl.response.admin.classes.SubjectAreaResponse;
import com.x7ubi.kurswahl.response.admin.classes.SubjectAreaResponses;
import com.x7ubi.kurswahl.response.admin.classes.SubjectAreaResultResponse;
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

    private SubjectArea otherSubjectArea;

    @BeforeEach
    public void setupTests() {
        subjectArea = new SubjectArea();
        subjectArea.setName("test");

        this.subjectAreaRepo.save(subjectArea);

        otherSubjectArea = new SubjectArea();
        otherSubjectArea.setName("test other");

        this.subjectAreaRepo.save(otherSubjectArea);
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
    public void testEditSubjectArea() {
        // Given
        subjectArea = this.subjectAreaRepo.findSubjectAreaByName("test").get();
        SubjectAreaCreationRequest subjectAreaCreationRequest = new SubjectAreaCreationRequest();
        subjectAreaCreationRequest.setName("subject area");

        // When
        ResultResponse response = this.subjectAreaCreationService.editSubjectArea(subjectArea.getSubjectAreaId(),
                subjectAreaCreationRequest);

        // Then
        SubjectArea editedSubjectArea = this.subjectAreaRepo.findSubjectAreaByName("subject area").get();
        Assertions.assertTrue(response.getErrorMessages().isEmpty());
        Assertions.assertEquals(editedSubjectArea.getSubjectAreaId(), subjectArea.getSubjectAreaId());
        Assertions.assertEquals(editedSubjectArea.getName(), subjectAreaCreationRequest.getName());
        Assertions.assertFalse(this.subjectAreaRepo.existsSubjectAreaByName("test"));
    }

    @Test
    public void testEditSubjectAreaSameName() {
        // Given
        subjectArea = this.subjectAreaRepo.findSubjectAreaByName("test").get();
        SubjectAreaCreationRequest subjectAreaCreationRequest = new SubjectAreaCreationRequest();
        subjectAreaCreationRequest.setName("test");

        // When
        ResultResponse response = this.subjectAreaCreationService.editSubjectArea(subjectArea.getSubjectAreaId(),
                subjectAreaCreationRequest);

        // Then
        SubjectArea editedSubjectArea = this.subjectAreaRepo.findSubjectAreaByName("test").get();
        Assertions.assertTrue(response.getErrorMessages().isEmpty());
        Assertions.assertEquals(editedSubjectArea.getSubjectAreaId(), subjectArea.getSubjectAreaId());
        Assertions.assertEquals(editedSubjectArea.getName(), subjectAreaCreationRequest.getName());
    }

    @Test
    public void testEditSubjectAreaNameExists() {
        // Given
        subjectArea = this.subjectAreaRepo.findSubjectAreaByName("test").get();
        SubjectAreaCreationRequest subjectAreaCreationRequest = new SubjectAreaCreationRequest();
        subjectAreaCreationRequest.setName("test other");

        // When
        ResultResponse response = this.subjectAreaCreationService.editSubjectArea(subjectArea.getSubjectAreaId(),
                subjectAreaCreationRequest);

        // Then
        subjectArea = this.subjectAreaRepo.findSubjectAreaBySubjectAreaId(subjectArea.getSubjectAreaId()).get();
        otherSubjectArea = this.subjectAreaRepo.findSubjectAreaByName("test other").get();
        Assertions.assertEquals(response.getErrorMessages().size(), 1);
        Assertions.assertEquals(response.getErrorMessages().get(0).getMessage(), ErrorMessage.Administration.SUBJECT_AREA_ALREADY_EXISTS);
        Assertions.assertEquals(subjectArea.getName(), "test");
        Assertions.assertEquals(otherSubjectArea.getName(), "test other");
    }

    @Test
    public void testGetAllSubjectAreas() {
        // When
        SubjectAreaResponses subjectAreaResponses = this.subjectAreaCreationService.getAllSubjectAreas();

        // Then
        Assertions.assertEquals(subjectAreaResponses.getSubjectAreaResponses().size(), 2);

        SubjectAreaResponse subjectAreaResponse1 = subjectAreaResponses.getSubjectAreaResponses().get(0);
        Assertions.assertEquals(subjectAreaResponse1.getSubjectAreaId(), subjectArea.getSubjectAreaId());
        Assertions.assertEquals(subjectAreaResponse1.getName(), subjectArea.getName());

        SubjectAreaResponse subjectAreaResponse2 = subjectAreaResponses.getSubjectAreaResponses().get(1);
        Assertions.assertEquals(subjectAreaResponse2.getSubjectAreaId(), otherSubjectArea.getSubjectAreaId());
        Assertions.assertEquals(subjectAreaResponse2.getName(), otherSubjectArea.getName());
    }

    @Test
    public void testGetSubjectArea() {
        // Given
        Long id = this.subjectAreaRepo.findSubjectAreaByName("test").get().getSubjectAreaId();

        // When
        SubjectAreaResultResponse subjectAreaResponse = this.subjectAreaCreationService.getSubjectArea(id);

        // Then
        Assertions.assertTrue(subjectAreaResponse.getErrorMessages().isEmpty());

        Assertions.assertEquals(subjectAreaResponse.getSubjectAreaResponse().getSubjectAreaId(), subjectArea.getSubjectAreaId());
        Assertions.assertEquals(subjectAreaResponse.getSubjectAreaResponse().getName(), subjectArea.getName());
    }

    @Test
    public void testGetSubjectAreaWrongId() {
        // Given
        Long id = this.subjectAreaRepo.findSubjectAreaByName("test").get().getSubjectAreaId() + 3;

        // When
        SubjectAreaResultResponse subjectAreaResponse = this.subjectAreaCreationService.getSubjectArea(id);

        // Then
        Assertions.assertEquals(subjectAreaResponse.getErrorMessages().size(), 1);
        Assertions.assertEquals(subjectAreaResponse.getErrorMessages().get(0).getMessage(),
                ErrorMessage.Administration.SUBJECT_AREA_NOT_FOUND);

        Assertions.assertNull(subjectAreaResponse.getSubjectAreaResponse());
    }

    @Test
    public void testEditSubjectAreaWrongId() {
        // Given
        subjectArea = this.subjectAreaRepo.findSubjectAreaByName("test").get();
        SubjectAreaCreationRequest subjectAreaCreationRequest = new SubjectAreaCreationRequest();
        subjectAreaCreationRequest.setName("test");

        // When
        ResultResponse response = this.subjectAreaCreationService.editSubjectArea(subjectArea.getSubjectAreaId() + 3,
                subjectAreaCreationRequest);

        // Then
        subjectArea = this.subjectAreaRepo.findSubjectAreaBySubjectAreaId(subjectArea.getSubjectAreaId()).get();
        otherSubjectArea = this.subjectAreaRepo.findSubjectAreaByName("test other").get();
        Assertions.assertEquals(response.getErrorMessages().size(), 1);
        Assertions.assertEquals(response.getErrorMessages().get(0).getMessage(),
                ErrorMessage.Administration.SUBJECT_AREA_NOT_FOUND);
        Assertions.assertEquals(subjectArea.getName(), "test");
        Assertions.assertEquals(otherSubjectArea.getName(), "test other");
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
        Long id = this.subjectArea.getSubjectAreaId() + 3;

        // When
        ResultResponse response = this.subjectAreaCreationService.deleteSubjectArea(id);

        // Then
        Assertions.assertEquals(response.getErrorMessages().size(), 1);
        Assertions.assertEquals(response.getErrorMessages().get(0).getMessage(), ErrorMessage.Administration.SUBJECT_AREA_NOT_FOUND);
        Assertions.assertTrue(this.subjectAreaRepo.existsSubjectAreaByName(this.subjectArea.getName()));
    }
}
