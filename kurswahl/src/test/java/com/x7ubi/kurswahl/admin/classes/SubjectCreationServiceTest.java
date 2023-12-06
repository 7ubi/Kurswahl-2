package com.x7ubi.kurswahl.admin.classes;

import com.x7ubi.kurswahl.KurswahlServiceTest;
import com.x7ubi.kurswahl.admin.request.SubjectCreationRequest;
import com.x7ubi.kurswahl.admin.response.classes.SubjectResponse;
import com.x7ubi.kurswahl.admin.response.classes.SubjectResponses;
import com.x7ubi.kurswahl.admin.service.classes.SubjectCreationService;
import com.x7ubi.kurswahl.common.error.ErrorMessage;
import com.x7ubi.kurswahl.common.exception.EntityCreationException;
import com.x7ubi.kurswahl.common.exception.EntityNotFoundException;
import com.x7ubi.kurswahl.common.models.Subject;
import com.x7ubi.kurswahl.common.models.SubjectArea;
import com.x7ubi.kurswahl.common.repository.SubjectAreaRepo;
import com.x7ubi.kurswahl.common.repository.SubjectRepo;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@KurswahlServiceTest
public class SubjectCreationServiceTest {

    @Autowired
    private SubjectCreationService subjectCreationService;

    @Autowired
    private SubjectAreaRepo subjectAreaRepo;

    @Autowired
    private SubjectRepo subjectRepo;

    private SubjectArea subjectArea;

    private Subject subject;

    private SubjectArea subjectAreaOther;

    private Subject subjectOther;

    @BeforeEach
    public void setupTests() {
        subjectArea = new SubjectArea();
        subjectArea.setName("Subject Area");

        this.subjectAreaRepo.save(subjectArea);
        subjectArea = this.subjectAreaRepo.findSubjectAreaByName(subjectArea.getName()).get();

        subject = new Subject();
        subject.setName("test");
        subject.setSubjectArea(subjectArea);
        this.subjectRepo.save(subject);
        subjectArea.getSubjects().add(subject);
        this.subjectAreaRepo.save(subjectArea);

        setupOtherSubject();
    }

    public void setupOtherSubject() {
        subjectAreaOther = new SubjectArea();
        subjectAreaOther.setName("Subject Area Other");

        this.subjectAreaRepo.save(subjectAreaOther);
        subjectAreaOther = this.subjectAreaRepo.findSubjectAreaByName(subjectAreaOther.getName()).get();

        subjectOther = new Subject();
        subjectOther.setName("test other");
        subjectOther.setSubjectArea(subjectAreaOther);
        this.subjectRepo.save(subjectOther);
        subjectAreaOther.getSubjects().add(subjectOther);
        this.subjectAreaRepo.save(subjectAreaOther);
    }


    @Test
    public void testCreateSubject() {
        // Given
        SubjectCreationRequest subjectCreationRequest = new SubjectCreationRequest();
        subjectCreationRequest.setName("Subject");
        subjectCreationRequest.setSubjectAreaId(subjectArea.getSubjectAreaId());

        // When
        this.subjectCreationService.createSubject(subjectCreationRequest);

        // Then
        Subject createdSubject = this.subjectRepo.findSubjectByName("Subject").get();
        subjectArea = this.subjectAreaRepo.findSubjectAreaByName(subjectArea.getName()).get();
        Assertions.assertEquals(createdSubject.getName(), subjectCreationRequest.getName());
        Assertions.assertEquals(createdSubject.getSubjectArea().getName(), subjectArea.getName());
        Assertions.assertEquals(subjectArea.getSubjects().size(), 2);
    }

    @Test
    public void testCreateSubjectNameExists() {
        // Given
        SubjectCreationRequest subjectCreationRequest = new SubjectCreationRequest();
        subjectCreationRequest.setName("test");
        subjectCreationRequest.setSubjectAreaId(subjectArea.getSubjectAreaId());

        // When
        EntityCreationException entityCreationException = Assert.assertThrows(EntityCreationException.class, () ->
                this.subjectCreationService.createSubject(subjectCreationRequest));

        // Then
        Assertions.assertEquals(entityCreationException.getMessage(), ErrorMessage.Administration.SUBJECT_ALREADY_EXISTS);

        subject = this.subjectRepo.findSubjectByName("test").get();
        subjectArea = this.subjectAreaRepo.findSubjectAreaByName(subjectArea.getName()).get();
        Assertions.assertEquals(subject.getName(), subjectCreationRequest.getName());
        Assertions.assertEquals(subject.getSubjectArea().getName(), subjectArea.getName());
        Assertions.assertEquals(subjectArea.getSubjects().size(), 1);
    }

    @Test
    public void testCreateSubjectSubjectAreaNotFound() {
        // Given
        SubjectCreationRequest subjectCreationRequest = new SubjectCreationRequest();
        subjectCreationRequest.setName("Subject");
        subjectCreationRequest.setSubjectAreaId(subjectArea.getSubjectAreaId() + 3);

        // When
        EntityNotFoundException entityNotFoundException = Assert.assertThrows(EntityNotFoundException.class, () ->
                this.subjectCreationService.createSubject(subjectCreationRequest));

        // Then
        Assertions.assertEquals(entityNotFoundException.getMessage(), ErrorMessage.Administration.SUBJECT_AREA_NOT_FOUND);

        subjectArea = this.subjectAreaRepo.findSubjectAreaByName(subjectArea.getName()).get();
        Assertions.assertEquals(subjectArea.getSubjects().size(), 1);
    }

    @Test
    public void testEditSubject() {
        // Given
        Long subjectId = this.subject.getSubjectId();
        SubjectCreationRequest subjectCreationRequest = new SubjectCreationRequest();
        subjectCreationRequest.setName("Subject");
        subjectCreationRequest.setSubjectAreaId(subjectAreaOther.getSubjectAreaId());

        // When
        this.subjectCreationService.editSubject(subjectId, subjectCreationRequest);

        // Then
        Subject editedSubject = this.subjectRepo.findSubjectByName("Subject").get();
        subjectArea = this.subjectAreaRepo.findSubjectAreaByName(subjectArea.getName()).get();
        subjectAreaOther = this.subjectAreaRepo.findSubjectAreaByName(subjectAreaOther.getName()).get();

        Assertions.assertEquals(editedSubject.getName(), subjectCreationRequest.getName());
        Assertions.assertEquals(editedSubject.getSubjectArea().getName(), subjectAreaOther.getName());
        Assertions.assertEquals(subjectArea.getSubjects().size(), 0);
        Assertions.assertEquals(subjectAreaOther.getSubjects().size(), 2);
    }

    @Test
    public void testEditSubjectNameExists() {
        // Given
        Long subjectId = this.subject.getSubjectId();
        SubjectCreationRequest subjectCreationRequest = new SubjectCreationRequest();
        subjectCreationRequest.setName("test other");
        subjectCreationRequest.setSubjectAreaId(subjectArea.getSubjectAreaId());

        // When
        EntityCreationException entityCreationException = Assert.assertThrows(EntityCreationException.class, () ->
                this.subjectCreationService.editSubject(subjectId, subjectCreationRequest));

        // Then
        Assertions.assertEquals(entityCreationException.getMessage(), ErrorMessage.Administration.SUBJECT_ALREADY_EXISTS);

        Subject editedSubject = this.subjectRepo.findSubjectByName("test other").get();
        subjectArea = this.subjectAreaRepo.findSubjectAreaByName(subjectArea.getName()).get();
        subjectAreaOther = this.subjectAreaRepo.findSubjectAreaByName(subjectAreaOther.getName()).get();

        Assertions.assertEquals(editedSubject.getName(), subjectCreationRequest.getName());
        Assertions.assertEquals(editedSubject.getSubjectArea().getName(), subjectAreaOther.getName());
        Assertions.assertEquals(subjectArea.getSubjects().size(), 1);
        Assertions.assertEquals(subjectAreaOther.getSubjects().size(), 1);
    }

    @Test
    public void testEditSubjectWrongSubjectId() {
        // Given
        Long subjectId = this.subject.getSubjectId() + 3;
        SubjectCreationRequest subjectCreationRequest = new SubjectCreationRequest();
        subjectCreationRequest.setName("Subject");
        subjectCreationRequest.setSubjectAreaId(subjectArea.getSubjectAreaId());

        // When
        EntityNotFoundException entityNotFoundException = Assert.assertThrows(EntityNotFoundException.class, () ->
                this.subjectCreationService.editSubject(subjectId, subjectCreationRequest));

        // Then
        Assertions.assertEquals(entityNotFoundException.getMessage(), ErrorMessage.Administration.SUBJECT_NOT_FOUND);

        subjectArea = this.subjectAreaRepo.findSubjectAreaByName(subjectArea.getName()).get();
        subjectAreaOther = this.subjectAreaRepo.findSubjectAreaByName(subjectAreaOther.getName()).get();

        Assertions.assertFalse(this.subjectRepo.existsSubjectByName("Subject"));
        Assertions.assertEquals(subjectArea.getSubjects().size(), 1);
        Assertions.assertEquals(subjectAreaOther.getSubjects().size(), 1);
    }

    @Test
    public void testEditSubjectWrongSubjectAreaId() {
        // Given
        Long subjectId = this.subject.getSubjectId();
        SubjectCreationRequest subjectCreationRequest = new SubjectCreationRequest();
        subjectCreationRequest.setName("Subject");
        subjectCreationRequest.setSubjectAreaId(subjectArea.getSubjectAreaId() + 3);

        // When
        EntityNotFoundException entityNotFoundException = Assert.assertThrows(EntityNotFoundException.class, () ->
                this.subjectCreationService.editSubject(subjectId, subjectCreationRequest));

        // Then
        Assertions.assertEquals(entityNotFoundException.getMessage(), ErrorMessage.Administration.SUBJECT_AREA_NOT_FOUND);

        subjectArea = this.subjectAreaRepo.findSubjectAreaByName(subjectArea.getName()).get();
        subjectAreaOther = this.subjectAreaRepo.findSubjectAreaByName(subjectAreaOther.getName()).get();

        Assertions.assertFalse(this.subjectRepo.existsSubjectByName("Subject"));
        Assertions.assertEquals(subjectArea.getSubjects().size(), 1);
        Assertions.assertEquals(subjectAreaOther.getSubjects().size(), 1);
    }

    @Test
    public void testGetSubject() {
        // Given
        Long subjectId = this.subject.getSubjectId();

        // When
        SubjectResponse response = this.subjectCreationService.getSubject(subjectId);

        // Then
        Assertions.assertEquals(response.getSubjectId(), subjectId);
        Assertions.assertEquals(response.getName(), subject.getName());
        Assertions.assertEquals(response.getSubjectAreaResponse().getSubjectAreaId(),
                subject.getSubjectArea().getSubjectAreaId());
    }

    @Test
    public void testGetSubjectWrongId() {
        // Given
        Long subjectId = this.subject.getSubjectId() + 3;

        // When
        EntityNotFoundException entityNotFoundException = Assert.assertThrows(EntityNotFoundException.class, () ->
                this.subjectCreationService.getSubject(subjectId));

        // Then
        Assertions.assertEquals(entityNotFoundException.getMessage(), ErrorMessage.Administration.SUBJECT_NOT_FOUND);
    }

    @Test
    public void testGetAllSubjects() {
        // When
        SubjectResponses response = this.subjectCreationService.getAllSubjects();

        // Then
        Assertions.assertEquals(response.getSubjectResponses().size(), 2);

        SubjectResponse subject1 = response.getSubjectResponses().get(0);
        Assertions.assertEquals(subject1.getSubjectId(), subject.getSubjectId());
        Assertions.assertEquals(subject1.getName(), subject.getName());
        Assertions.assertEquals(subject1.getSubjectAreaResponse().getSubjectAreaId(),
                subject.getSubjectArea().getSubjectAreaId());

        SubjectResponse subject2 = response.getSubjectResponses().get(1);
        Assertions.assertEquals(subject2.getSubjectId(), subjectOther.getSubjectId());
        Assertions.assertEquals(subject2.getName(), subjectOther.getName());
        Assertions.assertEquals(subject2.getSubjectAreaResponse().getSubjectAreaId(),
                subjectOther.getSubjectArea().getSubjectAreaId());
    }

    @Test
    public void testDeleteSubject() {
        // Given
        subject = this.subjectRepo.findSubjectByName(subject.getName()).get();

        // When
        this.subjectCreationService.deleteSubject(subject.getSubjectId());

        // Then
        subjectArea = this.subjectAreaRepo.findSubjectAreaBySubjectAreaId(subjectArea.getSubjectAreaId()).get();
        Assertions.assertFalse(this.subjectRepo.existsSubjectByName(this.subject.getName()));
        Assertions.assertTrue(subjectArea.getSubjects().isEmpty());
    }

    @Test
    public void testDeleteSubjectWrongId() {
        // Given
        this.subject = this.subjectRepo.findSubjectByName(this.subject.getName()).get();
        Long id = this.subject.getSubjectId() + 3;

        // When
        EntityNotFoundException entityNotFoundException = Assert.assertThrows(EntityNotFoundException.class, () ->
                this.subjectCreationService.deleteSubject(id));

        // Then
        Assertions.assertEquals(entityNotFoundException.getMessage(), ErrorMessage.Administration.SUBJECT_NOT_FOUND);

        Assertions.assertTrue(this.subjectRepo.existsSubjectByName(this.subject.getName()));
        Assertions.assertEquals(subjectArea.getSubjects().size(), 1);
    }
}
