package com.x7ubi.kurswahl.admin.classes;

import com.x7ubi.kurswahl.error.ErrorMessage;
import com.x7ubi.kurswahl.models.Subject;
import com.x7ubi.kurswahl.models.SubjectArea;
import com.x7ubi.kurswahl.repository.SubjectAreaRepo;
import com.x7ubi.kurswahl.repository.SubjectRepo;
import com.x7ubi.kurswahl.request.admin.SubjectCreationRequest;
import com.x7ubi.kurswahl.response.common.ResultResponse;
import com.x7ubi.kurswahl.service.admin.classes.SubjectCreationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest()
@TestPropertySource(properties = {
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.url=jdbc:h2:mem:kurswahlTestdb;NON_KEYWORDS=user",
        "spring.jpa.properties.hibernate.globally_quoted_identifiers=true"
})
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class SubjectCreationServiceTest {

    @Autowired
    private SubjectCreationService subjectCreationService;

    @Autowired
    private SubjectAreaRepo subjectAreaRepo;

    @Autowired
    private SubjectRepo subjectRepo;

    private SubjectArea subjectArea;

    private Subject subject;

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
    }

    @Test
    public void createSubject() {
        // Given
        SubjectCreationRequest subjectCreationRequest = new SubjectCreationRequest();
        subjectCreationRequest.setName("Subject");
        subjectCreationRequest.setSubjectAreaId(subjectArea.getSubjectAreaId());

        // When
        ResultResponse response = this.subjectCreationService.createSubject(subjectCreationRequest);

        // Then
        Subject createdSubject = this.subjectRepo.findSubjectByName("Subject").get();
        subjectArea = this.subjectAreaRepo.findSubjectAreaByName(subjectArea.getName()).get();
        Assertions.assertTrue(response.getErrorMessages().isEmpty());
        Assertions.assertEquals(createdSubject.getName(), subjectCreationRequest.getName());
        Assertions.assertEquals(createdSubject.getSubjectArea().getName(), subjectArea.getName());
        Assertions.assertEquals(subjectArea.getSubjects().size(), 2);
    }

    @Test
    public void createSubjectNameExists() {
        // Given
        SubjectCreationRequest subjectCreationRequest = new SubjectCreationRequest();
        subjectCreationRequest.setName("test");
        subjectCreationRequest.setSubjectAreaId(subjectArea.getSubjectAreaId());

        // When
        ResultResponse response = this.subjectCreationService.createSubject(subjectCreationRequest);

        // Then
        subject = this.subjectRepo.findSubjectByName("test").get();
        subjectArea = this.subjectAreaRepo.findSubjectAreaByName(subjectArea.getName()).get();
        Assertions.assertEquals(response.getErrorMessages().size(), 1);
        Assertions.assertEquals(response.getErrorMessages().get(0).getMessage(), ErrorMessage.Administration.SUBJECT_ALREADY_EXISTS);
        Assertions.assertEquals(subject.getName(), subjectCreationRequest.getName());
        Assertions.assertEquals(subject.getSubjectArea().getName(), subjectArea.getName());
        Assertions.assertEquals(subjectArea.getSubjects().size(), 1);
    }

    @Test
    public void createSubjectSubjectAreaNotFound() {
        // Given
        SubjectCreationRequest subjectCreationRequest = new SubjectCreationRequest();
        subjectCreationRequest.setName("Subject");
        subjectCreationRequest.setSubjectAreaId(subjectArea.getSubjectAreaId() + 1);

        // When
        ResultResponse response = this.subjectCreationService.createSubject(subjectCreationRequest);

        // Then
        subjectArea = this.subjectAreaRepo.findSubjectAreaByName(subjectArea.getName()).get();
        Assertions.assertEquals(response.getErrorMessages().size(), 1);
        Assertions.assertEquals(response.getErrorMessages().get(0).getMessage(), ErrorMessage.Administration.SUBJECT_AREA_NOT_FOUND);
        Assertions.assertEquals(subjectArea.getSubjects().size(), 1);
    }

    @Test
    public void deleteSubject() {
        // Given
        subject = this.subjectRepo.findSubjectByName(subject.getName()).get();

        // When
        ResultResponse response = this.subjectCreationService.deleteSubject(subject.getSubjectId());

        // Then
        subjectArea = this.subjectAreaRepo.findSubjectAreaBySubjectAreaId(subjectArea.getSubjectAreaId()).get();
        Assertions.assertTrue(response.getErrorMessages().isEmpty());
        Assertions.assertFalse(this.subjectRepo.existsSubjectByName(this.subject.getName()));
        Assertions.assertTrue(subjectArea.getSubjects().isEmpty());
    }

    @Test
    public void testDeleteSubjectWrongId() {
        // Given
        this.subject = this.subjectRepo.findSubjectByName(this.subject.getName()).get();
        Long id = this.subject.getSubjectId() + 1;

        // When
        ResultResponse response = this.subjectCreationService.deleteSubject(id);

        // Then
        Assertions.assertEquals(response.getErrorMessages().size(), 1);
        Assertions.assertEquals(response.getErrorMessages().get(0).getMessage(), ErrorMessage.Administration.SUBJECT_NOT_FOUND);
        Assertions.assertTrue(this.subjectRepo.existsSubjectByName(this.subject.getName()));
        Assertions.assertEquals(subjectArea.getSubjects().size(), 1);
    }
}
