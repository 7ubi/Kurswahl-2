package com.x7ubi.kurswahl.admin.classes;

import com.x7ubi.kurswahl.KurswahlServiceTest;
import com.x7ubi.kurswahl.admin.classes.request.SubjectAreaCreationRequest;
import com.x7ubi.kurswahl.admin.classes.response.SubjectAreaResponse;
import com.x7ubi.kurswahl.admin.classes.service.SubjectAreaCreationService;
import com.x7ubi.kurswahl.common.error.ErrorMessage;
import com.x7ubi.kurswahl.common.exception.EntityCreationException;
import com.x7ubi.kurswahl.common.exception.EntityNotFoundException;
import com.x7ubi.kurswahl.common.models.SubjectArea;
import com.x7ubi.kurswahl.common.repository.SubjectAreaRepo;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

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
    public void testCreateSubjectArea() throws EntityCreationException {
        // Given
        SubjectAreaCreationRequest subjectAreaCreationRequest = new SubjectAreaCreationRequest();
        subjectAreaCreationRequest.setName("subject area");

        // When
        this.subjectAreaCreationService.createSubjectArea(subjectAreaCreationRequest);

        // Then
        SubjectArea createdSubjectArea = this.subjectAreaRepo.findSubjectAreaByName("subject area").get();
        Assertions.assertEquals(createdSubjectArea.getName(), subjectAreaCreationRequest.getName());
    }

    @Test
    public void testCreateSubjectAreaNameExists() {
        // Given
        SubjectAreaCreationRequest subjectAreaCreationRequest = new SubjectAreaCreationRequest();
        subjectAreaCreationRequest.setName("test");

        // When
        EntityCreationException entityCreationException = Assert.assertThrows(EntityCreationException.class, () ->
                this.subjectAreaCreationService.createSubjectArea(subjectAreaCreationRequest));

        // Then
        Assertions.assertEquals(entityCreationException.getMessage(), ErrorMessage.SUBJECT_AREA_ALREADY_EXISTS);
        SubjectArea createdSubjectArea = this.subjectAreaRepo.findSubjectAreaByName("test").get();
        Assertions.assertEquals(createdSubjectArea.getName(), subjectArea.getName());
    }

    @Test
    public void testEditSubjectArea() throws EntityCreationException, EntityNotFoundException {
        // Given
        subjectArea = this.subjectAreaRepo.findSubjectAreaByName("test").get();
        SubjectAreaCreationRequest subjectAreaCreationRequest = new SubjectAreaCreationRequest();
        subjectAreaCreationRequest.setName("subject area");

        // When
        this.subjectAreaCreationService.editSubjectArea(subjectArea.getSubjectAreaId(), subjectAreaCreationRequest);

        // Then
        SubjectArea editedSubjectArea = this.subjectAreaRepo.findSubjectAreaByName("subject area").get();
        Assertions.assertEquals(editedSubjectArea.getSubjectAreaId(), subjectArea.getSubjectAreaId());
        Assertions.assertEquals(editedSubjectArea.getName(), subjectAreaCreationRequest.getName());
        Assertions.assertFalse(this.subjectAreaRepo.existsSubjectAreaByName("test"));
    }

    @Test
    public void testEditSubjectAreaSameName() throws EntityCreationException, EntityNotFoundException {
        // Given
        subjectArea = this.subjectAreaRepo.findSubjectAreaByName("test").get();
        SubjectAreaCreationRequest subjectAreaCreationRequest = new SubjectAreaCreationRequest();
        subjectAreaCreationRequest.setName("test");

        // When
        this.subjectAreaCreationService.editSubjectArea(subjectArea.getSubjectAreaId(), subjectAreaCreationRequest);

        // Then
        SubjectArea editedSubjectArea = this.subjectAreaRepo.findSubjectAreaByName("test").get();
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
        EntityCreationException entityCreationException = Assert.assertThrows(EntityCreationException.class, () ->
                this.subjectAreaCreationService.createSubjectArea(subjectAreaCreationRequest));

        // Then
        Assertions.assertEquals(entityCreationException.getMessage(), ErrorMessage.SUBJECT_AREA_ALREADY_EXISTS);
        subjectArea = this.subjectAreaRepo.findSubjectAreaBySubjectAreaId(subjectArea.getSubjectAreaId()).get();
        otherSubjectArea = this.subjectAreaRepo.findSubjectAreaByName("test other").get();
        Assertions.assertEquals(subjectArea.getName(), "test");
        Assertions.assertEquals(otherSubjectArea.getName(), "test other");
    }

    @Test
    public void testEditSubjectAreaWrongId() {
        // Given
        subjectArea = this.subjectAreaRepo.findSubjectAreaByName("test").get();
        SubjectAreaCreationRequest subjectAreaCreationRequest = new SubjectAreaCreationRequest();
        subjectAreaCreationRequest.setName("test");

        // When
        EntityNotFoundException entityNotFoundException = Assert.assertThrows(EntityNotFoundException.class, () ->
                this.subjectAreaCreationService.editSubjectArea(subjectArea.getSubjectAreaId() + 5,
                        subjectAreaCreationRequest));

        // Then
        Assertions.assertEquals(entityNotFoundException.getMessage(), ErrorMessage.SUBJECT_AREA_NOT_FOUND);

        subjectArea = this.subjectAreaRepo.findSubjectAreaBySubjectAreaId(subjectArea.getSubjectAreaId()).get();
        otherSubjectArea = this.subjectAreaRepo.findSubjectAreaByName("test other").get();
        Assertions.assertEquals(subjectArea.getName(), "test");
        Assertions.assertEquals(otherSubjectArea.getName(), "test other");
    }

    @Test
    public void testGetAllSubjectAreas() {
        // When
        List<SubjectAreaResponse> subjectAreaResponses = this.subjectAreaCreationService.getAllSubjectAreas();

        // Then
        Assertions.assertEquals(subjectAreaResponses.size(), 2);

        SubjectAreaResponse subjectAreaResponse1 = subjectAreaResponses.get(0);
        Assertions.assertEquals(subjectAreaResponse1.getSubjectAreaId(), subjectArea.getSubjectAreaId());
        Assertions.assertEquals(subjectAreaResponse1.getName(), subjectArea.getName());

        SubjectAreaResponse subjectAreaResponse2 = subjectAreaResponses.get(1);
        Assertions.assertEquals(subjectAreaResponse2.getSubjectAreaId(), otherSubjectArea.getSubjectAreaId());
        Assertions.assertEquals(subjectAreaResponse2.getName(), otherSubjectArea.getName());
    }

    @Test
    public void testGetSubjectArea() throws EntityNotFoundException {
        // Given
        Long id = this.subjectAreaRepo.findSubjectAreaByName("test").get().getSubjectAreaId();

        // When
        SubjectAreaResponse subjectAreaResponse = this.subjectAreaCreationService.getSubjectArea(id);

        // Then
        Assertions.assertEquals(subjectAreaResponse.getSubjectAreaId(), subjectArea.getSubjectAreaId());
        Assertions.assertEquals(subjectAreaResponse.getName(), subjectArea.getName());
    }

    @Test
    public void testGetSubjectAreaWrongId() {
        // Given
        Long id = this.subjectAreaRepo.findSubjectAreaByName("test").get().getSubjectAreaId() + 3;

        // When
        EntityNotFoundException entityNotFoundException = Assert.assertThrows(EntityNotFoundException.class, () ->
                this.subjectAreaCreationService.getSubjectArea(id));

        // Then
        Assertions.assertEquals(entityNotFoundException.getMessage(), ErrorMessage.SUBJECT_AREA_NOT_FOUND);
    }

    @Test
    public void testDeleteSubjectArea() throws EntityNotFoundException {
        // Given
        this.subjectArea = this.subjectAreaRepo.findSubjectAreaByName(this.subjectArea.getName()).get();
        Long id = this.subjectArea.getSubjectAreaId();

        // When
        List<SubjectAreaResponse> responses = this.subjectAreaCreationService.deleteSubjectArea(id);

        // Then
        Assertions.assertFalse(this.subjectAreaRepo.existsSubjectAreaByName(this.subjectArea.getName()));

        Assertions.assertEquals(responses.size(), 1);
        Assertions.assertEquals(responses.get(0).getSubjectAreaId(), otherSubjectArea.getSubjectAreaId());
        Assertions.assertEquals(responses.get(0).getName(), otherSubjectArea.getName());
    }

    @Test
    public void testDeleteSubjectAreaWrongId() {
        // Given
        this.subjectArea = this.subjectAreaRepo.findSubjectAreaByName(this.subjectArea.getName()).get();
        Long id = this.subjectArea.getSubjectAreaId() + 3;

        // When
        EntityNotFoundException entityNotFoundException = Assert.assertThrows(EntityNotFoundException.class, () ->
                this.subjectAreaCreationService.deleteSubjectArea(id));

        // Then
        Assertions.assertEquals(entityNotFoundException.getMessage(), ErrorMessage.SUBJECT_AREA_NOT_FOUND);
        Assertions.assertTrue(this.subjectAreaRepo.existsSubjectAreaByName(this.subjectArea.getName()));
    }

    @Test
    public void testDeleteSubjectAreas() throws EntityNotFoundException {
        // Given
        this.subjectArea = this.subjectAreaRepo.findSubjectAreaByName(this.subjectArea.getName()).get();
        this.otherSubjectArea = this.subjectAreaRepo.findSubjectAreaByName(this.otherSubjectArea.getName()).get();
        List<Long> ids = List.of(subjectArea.getSubjectAreaId(), otherSubjectArea.getSubjectAreaId());

        // When
        List<SubjectAreaResponse> responses = this.subjectAreaCreationService.deleteSubjectAreas(ids);

        // Then
        Assertions.assertFalse(this.subjectAreaRepo.existsSubjectAreaByName(this.subjectArea.getName()));
        Assertions.assertFalse(this.subjectAreaRepo.existsSubjectAreaByName(this.otherSubjectArea.getName()));

        Assertions.assertTrue(responses.isEmpty());
    }
}
