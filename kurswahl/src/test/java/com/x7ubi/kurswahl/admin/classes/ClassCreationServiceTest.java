package com.x7ubi.kurswahl.admin.classes;

import com.x7ubi.kurswahl.KurswahlServiceTest;
import com.x7ubi.kurswahl.admin.request.ClassCreationRequest;
import com.x7ubi.kurswahl.admin.response.classes.ClassResponse;
import com.x7ubi.kurswahl.admin.response.classes.ClassResponses;
import com.x7ubi.kurswahl.admin.response.classes.ClassResultResponse;
import com.x7ubi.kurswahl.admin.service.classes.ClassCreationService;
import com.x7ubi.kurswahl.common.error.ErrorMessage;
import com.x7ubi.kurswahl.common.models.Class;
import com.x7ubi.kurswahl.common.models.*;
import com.x7ubi.kurswahl.common.repository.*;
import com.x7ubi.kurswahl.common.response.ResultResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Year;
import java.util.HashSet;
import java.util.List;

@KurswahlServiceTest
public class ClassCreationServiceTest {

    @Autowired
    private ClassCreationService classCreationService;

    @Autowired
    private ClassRepo classRepo;

    @Autowired
    private TapeRepo tapeRepo;

    private Tape tape;

    private Tape otherTape;


    @Autowired
    private SubjectAreaRepo subjectAreaRepo;

    @Autowired
    private SubjectRepo subjectRepo;

    private Subject subject;

    private Subject subjectOther;

    @Autowired
    private TeacherRepo teacherRepo;

    private Teacher teacher;

    private Class aClass;

    @BeforeEach
    public void setupTest() {
        setupTapes();
        setupSubjects();
        setupTeachers();
        setupClasses();
    }

    private void setupTapes() {
        tape = new Tape();
        tape.setName("GK 1");
        tape.setYear(11);
        tape.setReleaseYear(Year.now().getValue());
        tape.setLk(false);

        tapeRepo.save(tape);

        otherTape = new Tape();
        otherTape.setName("LK 1");
        otherTape.setYear(12);
        otherTape.setReleaseYear(Year.now().getValue());
        otherTape.setLk(true);

        tapeRepo.save(otherTape);
    }

    public void setupSubjects() {
        SubjectArea subjectArea = new SubjectArea();
        subjectArea.setName("Subject Area");

        this.subjectAreaRepo.save(subjectArea);
        subjectArea = this.subjectAreaRepo.findSubjectAreaByName(subjectArea.getName()).get();

        subject = new Subject();
        subject.setName("test");
        subject.setSubjectArea(subjectArea);
        this.subjectRepo.save(subject);
        subjectArea.getSubjects().add(subject);
        this.subjectAreaRepo.save(subjectArea);

        SubjectArea subjectAreaOther = new SubjectArea();
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

    public void setupTeachers() {
        User user = new User();
        user.setUsername("test.user");
        user.setFirstname("Test");
        user.setSurname("User");
        user.setPassword("Password");
        teacher = new Teacher();
        teacher.setAbbreviation("NN");
        teacher.setUser(user);

        this.teacherRepo.save(teacher);
        teacher = this.teacherRepo.findTeacherByUser_Username(teacher.getUser().getUsername()).get();
    }

    private void setupClasses() {
        aClass = new Class();
        aClass.setName("test");
        aClass.setTape(tape);
        aClass.setSubject(subject);
        aClass.setTeacher(teacher);
        this.classRepo.save(aClass);

        teacher.getClasses().add(aClass);
        this.teacherRepo.save(teacher);

        subject.setClasses(new HashSet<>());
        subject.getClasses().add(aClass);
        this.subjectRepo.save(subject);

        tape.setaClass(new HashSet<>());
        tape.getaClass().add(aClass);
        tapeRepo.save(tape);
    }

    @Test
    public void testCreateClass() {
        // Given
        ClassCreationRequest classCreationRequest = new ClassCreationRequest();
        classCreationRequest.setName("test");
        classCreationRequest.setTapeId(tape.getTapeId());
        classCreationRequest.setTeacherId(teacher.getTeacherId());
        classCreationRequest.setSubjectId(subject.getSubjectId());

        // When
        ResultResponse response = this.classCreationService.createClass(classCreationRequest);

        // Then
        Assertions.assertTrue(response.getErrorMessages().isEmpty());

        List<Class> createdClasses = this.classRepo.findAll();

        Assertions.assertEquals(createdClasses.size(), 2);

        Class createdClass = this.classRepo.findClassByClassId(2L).get();

        Assertions.assertEquals(createdClass.getName(), classCreationRequest.getName());
        Assertions.assertEquals(createdClass.getTeacher().getTeacherId(), classCreationRequest.getTeacherId());
        Assertions.assertEquals(createdClass.getSubject().getSubjectId(), classCreationRequest.getSubjectId());
        Assertions.assertEquals(createdClass.getTape().getTapeId(), classCreationRequest.getTapeId());
    }

    @Test
    public void testCreateClassWrongTapeId() {
        // Given
        ClassCreationRequest classCreationRequest = new ClassCreationRequest();
        classCreationRequest.setName("test");
        classCreationRequest.setTapeId(tape.getTapeId() + 3);
        classCreationRequest.setTeacherId(teacher.getTeacherId());
        classCreationRequest.setSubjectId(subject.getSubjectId());

        // When
        ResultResponse response = this.classCreationService.createClass(classCreationRequest);

        // Then
        Assertions.assertEquals(response.getErrorMessages().size(), 1);
        Assertions.assertEquals(response.getErrorMessages().get(0).getMessage(),
                ErrorMessage.Administration.TAPE_NOT_FOUND);

        List<Class> createdClasses = this.classRepo.findAll();

        Assertions.assertEquals(createdClasses.size(), 1);
    }

    @Test
    public void testCreateClassWrongTeacherId() {
        // Given
        ClassCreationRequest classCreationRequest = new ClassCreationRequest();
        classCreationRequest.setName("test");
        classCreationRequest.setTapeId(tape.getTapeId());
        classCreationRequest.setTeacherId(teacher.getTeacherId() + 3);
        classCreationRequest.setSubjectId(subject.getSubjectId());

        // When
        ResultResponse response = this.classCreationService.createClass(classCreationRequest);

        // Then
        Assertions.assertEquals(response.getErrorMessages().size(), 1);
        Assertions.assertEquals(response.getErrorMessages().get(0).getMessage(),
                ErrorMessage.Administration.TEACHER_NOT_FOUND);

        List<Class> createdClasses = this.classRepo.findAll();

        Assertions.assertEquals(createdClasses.size(), 1);
    }

    @Test
    public void testCreateClassWrongSubjectId() {
        // Given
        ClassCreationRequest classCreationRequest = new ClassCreationRequest();
        classCreationRequest.setName("test");
        classCreationRequest.setTapeId(tape.getTapeId());
        classCreationRequest.setTeacherId(teacher.getTeacherId());
        classCreationRequest.setSubjectId(subject.getSubjectId() + 3);

        // When
        ResultResponse response = this.classCreationService.createClass(classCreationRequest);

        // Then
        Assertions.assertEquals(response.getErrorMessages().size(), 1);
        Assertions.assertEquals(response.getErrorMessages().get(0).getMessage(),
                ErrorMessage.Administration.SUBJECT_NOT_FOUND);

        List<Class> createdClasses = this.classRepo.findAll();

        Assertions.assertEquals(createdClasses.size(), 1);
    }

    @Test
    public void testEditClass() {
        // Given
        Long classId = this.classRepo.findClassByName("test").get().getClassId();
        ClassCreationRequest classCreationRequest = new ClassCreationRequest();
        classCreationRequest.setName("test edit");
        classCreationRequest.setTapeId(otherTape.getTapeId());
        classCreationRequest.setTeacherId(teacher.getTeacherId());
        classCreationRequest.setSubjectId(subjectOther.getSubjectId());

        // When
        ResultResponse response = this.classCreationService.editClass(classId, classCreationRequest);

        // Then
        Assertions.assertTrue(response.getErrorMessages().isEmpty());

        List<Class> createdClasses = this.classRepo.findAll();

        Assertions.assertEquals(createdClasses.size(), 1);

        Class createdClass = this.classRepo.findClassByName("test edit").get();

        Assertions.assertEquals(createdClass.getName(), classCreationRequest.getName());
        Assertions.assertEquals(createdClass.getTeacher().getTeacherId(), classCreationRequest.getTeacherId());
        Assertions.assertEquals(createdClass.getSubject().getSubjectId(), classCreationRequest.getSubjectId());
        Assertions.assertEquals(createdClass.getTape().getTapeId(), classCreationRequest.getTapeId());
    }

    @Test
    public void testEditClassWrongClassId() {
        // Given
        Long classId = this.classRepo.findClassByName("test").get().getClassId() + 3;
        ClassCreationRequest classCreationRequest = new ClassCreationRequest();
        classCreationRequest.setName("test edit");
        classCreationRequest.setTapeId(otherTape.getTapeId());
        classCreationRequest.setTeacherId(teacher.getTeacherId());
        classCreationRequest.setSubjectId(subjectOther.getSubjectId());

        // When
        ResultResponse response = this.classCreationService.editClass(classId, classCreationRequest);

        // Then
        Assertions.assertEquals(response.getErrorMessages().size(), 1);
        Assertions.assertEquals(response.getErrorMessages().get(0).getMessage(),
                ErrorMessage.Administration.CLASS_NOT_FOUND);

        List<Class> createdClasses = this.classRepo.findAll();

        Assertions.assertEquals(createdClasses.size(), 1);

        Class createdClass = this.classRepo.findClassByName("test").get();

        Assertions.assertEquals(createdClass.getName(), aClass.getName());
        Assertions.assertEquals(createdClass.getTeacher().getTeacherId(), aClass.getTeacher().getTeacherId());
        Assertions.assertEquals(createdClass.getSubject().getSubjectId(), aClass.getSubject().getSubjectId());
        Assertions.assertEquals(createdClass.getTape().getTapeId(), aClass.getTape().getTapeId());
    }

    @Test
    public void testEditClassWrongTapeId() {
        // Given
        Long classId = this.classRepo.findClassByName("test").get().getClassId();
        ClassCreationRequest classCreationRequest = new ClassCreationRequest();
        classCreationRequest.setName("test edit");
        classCreationRequest.setTapeId(otherTape.getTapeId() + 3);
        classCreationRequest.setTeacherId(teacher.getTeacherId());
        classCreationRequest.setSubjectId(subjectOther.getSubjectId());

        // When
        ResultResponse response = this.classCreationService.editClass(classId, classCreationRequest);

        // Then
        Assertions.assertEquals(response.getErrorMessages().size(), 1);
        Assertions.assertEquals(response.getErrorMessages().get(0).getMessage(),
                ErrorMessage.Administration.TAPE_NOT_FOUND);

        List<Class> createdClasses = this.classRepo.findAll();

        Assertions.assertEquals(createdClasses.size(), 1);

        Class createdClass = this.classRepo.findClassByName("test").get();

        Assertions.assertEquals(createdClass.getName(), aClass.getName());
        Assertions.assertEquals(createdClass.getTeacher().getTeacherId(), aClass.getTeacher().getTeacherId());
        Assertions.assertEquals(createdClass.getSubject().getSubjectId(), aClass.getSubject().getSubjectId());
        Assertions.assertEquals(createdClass.getTape().getTapeId(), aClass.getTape().getTapeId());
    }

    @Test
    public void testEditClassWrongTeacherId() {
        // Given
        Long classId = this.classRepo.findClassByName("test").get().getClassId();
        ClassCreationRequest classCreationRequest = new ClassCreationRequest();
        classCreationRequest.setName("test edit");
        classCreationRequest.setTapeId(otherTape.getTapeId());
        classCreationRequest.setTeacherId(teacher.getTeacherId() + 3);
        classCreationRequest.setSubjectId(subjectOther.getSubjectId());

        // When
        ResultResponse response = this.classCreationService.editClass(classId, classCreationRequest);

        // Then
        Assertions.assertEquals(response.getErrorMessages().size(), 1);
        Assertions.assertEquals(response.getErrorMessages().get(0).getMessage(),
                ErrorMessage.Administration.TEACHER_NOT_FOUND);

        List<Class> createdClasses = this.classRepo.findAll();

        Assertions.assertEquals(createdClasses.size(), 1);

        Class createdClass = this.classRepo.findClassByName("test").get();

        Assertions.assertEquals(createdClass.getName(), aClass.getName());
        Assertions.assertEquals(createdClass.getTeacher().getTeacherId(), aClass.getTeacher().getTeacherId());
        Assertions.assertEquals(createdClass.getSubject().getSubjectId(), aClass.getSubject().getSubjectId());
        Assertions.assertEquals(createdClass.getTape().getTapeId(), aClass.getTape().getTapeId());
    }

    @Test
    public void testEditClassWrongSubjectId() {
        // Given
        Long classId = this.classRepo.findClassByName("test").get().getClassId();
        ClassCreationRequest classCreationRequest = new ClassCreationRequest();
        classCreationRequest.setName("test edit");
        classCreationRequest.setTapeId(otherTape.getTapeId());
        classCreationRequest.setTeacherId(teacher.getTeacherId());
        classCreationRequest.setSubjectId(subjectOther.getSubjectId() + 3);

        // When
        ResultResponse response = this.classCreationService.editClass(classId, classCreationRequest);

        // Then
        Assertions.assertEquals(response.getErrorMessages().size(), 1);
        Assertions.assertEquals(response.getErrorMessages().get(0).getMessage(),
                ErrorMessage.Administration.SUBJECT_NOT_FOUND);

        List<Class> createdClasses = this.classRepo.findAll();

        Assertions.assertEquals(createdClasses.size(), 1);

        Class createdClass = this.classRepo.findClassByName("test").get();

        Assertions.assertEquals(createdClass.getName(), aClass.getName());
        Assertions.assertEquals(createdClass.getTeacher().getTeacherId(), aClass.getTeacher().getTeacherId());
        Assertions.assertEquals(createdClass.getSubject().getSubjectId(), aClass.getSubject().getSubjectId());
        Assertions.assertEquals(createdClass.getTape().getTapeId(), aClass.getTape().getTapeId());
    }

    @Test
    public void testGetClass() {
        // Given
        aClass = this.classRepo.findClassByName("test").get();
        Long classId = aClass.getClassId();

        // When
        ClassResultResponse response = this.classCreationService.getClassByClassId(classId);

        // Then
        Assertions.assertTrue(response.getErrorMessages().isEmpty());

        ClassResponse classResponse = response.getClassResponse();
        Assertions.assertEquals(classResponse.getClassId(), aClass.getClassId());
        Assertions.assertEquals(classResponse.getName(), aClass.getName());
        Assertions.assertEquals(classResponse.getTapeResponse().getTapeId(), aClass.getTape().getTapeId());
        Assertions.assertEquals(classResponse.getTeacherResponse().getTeacherId(), aClass.getTeacher().getTeacherId());
        Assertions.assertEquals(classResponse.getSubjectResponse().getSubjectId(), aClass.getSubject().getSubjectId());
    }

    @Test
    public void testGetClassWrongId() {
        // Given
        aClass = this.classRepo.findClassByName("test").get();
        Long classId = aClass.getClassId() + 3;

        // When
        ClassResultResponse response = this.classCreationService.getClassByClassId(classId);

        // Then
        Assertions.assertEquals(response.getErrorMessages().size(), 1);
        Assertions.assertEquals(response.getErrorMessages().get(0).getMessage(),
                ErrorMessage.Administration.CLASS_NOT_FOUND);

        Assertions.assertNull(response.getClassResponse());
    }

    @Test
    public void testAllClasses() {
        // Given
        Integer year = 11;

        // When
        ClassResponses response = this.classCreationService.getAllClasses(year);

        // Then
        Assertions.assertEquals(response.getClassResponses().size(), 1);

        ClassResponse classResponse = response.getClassResponses().get(0);
        Assertions.assertEquals(classResponse.getClassId(), aClass.getClassId());
        Assertions.assertEquals(classResponse.getName(), aClass.getName());
        Assertions.assertEquals(classResponse.getTapeResponse().getTapeId(), aClass.getTape().getTapeId());
        Assertions.assertEquals(classResponse.getTeacherResponse().getTeacherId(), aClass.getTeacher().getTeacherId());
        Assertions.assertEquals(classResponse.getSubjectResponse().getSubjectId(), aClass.getSubject().getSubjectId());
    }

    @Test
    public void testDeleteClass() {
        // Given
        aClass = this.classRepo.findClassByName("test").get();
        Long classId = aClass.getClassId();

        // When
        ResultResponse response = this.classCreationService.deleteClass(classId);

        // Then
        Assertions.assertTrue(response.getErrorMessages().isEmpty());

        Assertions.assertFalse(this.classRepo.existsClassByClassId(classId));
        teacher = this.teacherRepo.findTeacherByTeacherId(teacher.getTeacherId()).get();
        Assertions.assertTrue(teacher.getClasses().isEmpty());
        subject = this.subjectRepo.findSubjectBySubjectId(aClass.getSubject().getSubjectId()).get();
        Assertions.assertTrue(subject.getClasses().isEmpty());
        tape = this.tapeRepo.findTapeByTapeId(aClass.getTape().getTapeId()).get();
        Assertions.assertTrue(tape.getaClass().isEmpty());
    }

    @Test
    public void testDeleteClassWrongId() {
        // Given
        aClass = this.classRepo.findClassByName("test").get();
        Long classId = aClass.getClassId() + 3;

        // When
        ResultResponse response = this.classCreationService.deleteClass(classId);

        // Then
        Assertions.assertEquals(response.getErrorMessages().size(), 1);
        Assertions.assertEquals(response.getErrorMessages().get(0).getMessage(),
                ErrorMessage.Administration.CLASS_NOT_FOUND);

        Assertions.assertTrue(this.classRepo.existsClassByClassId(aClass.getClassId()));
        teacher = this.teacherRepo.findTeacherByTeacherId(teacher.getTeacherId()).get();
        Assertions.assertFalse(teacher.getClasses().isEmpty());
        subject = this.subjectRepo.findSubjectBySubjectId(aClass.getSubject().getSubjectId()).get();
        Assertions.assertFalse(subject.getClasses().isEmpty());
        tape = this.tapeRepo.findTapeByTapeId(aClass.getTape().getTapeId()).get();
        Assertions.assertFalse(tape.getaClass().isEmpty());
    }
}
