package com.x7ubi.kurswahl.admin.choice;

import com.x7ubi.kurswahl.KurswahlServiceTest;
import com.x7ubi.kurswahl.admin.choice.request.AlternateChoiceRequest;
import com.x7ubi.kurswahl.admin.choice.response.ChoiceResponse;
import com.x7ubi.kurswahl.admin.choice.response.ChoiceTapeResponse;
import com.x7ubi.kurswahl.admin.choice.response.ClassStudentsResponse;
import com.x7ubi.kurswahl.admin.choice.response.StudentChoicesResponse;
import com.x7ubi.kurswahl.admin.choice.service.AssignChoiceService;
import com.x7ubi.kurswahl.common.error.ErrorMessage;
import com.x7ubi.kurswahl.common.exception.EntityNotFoundException;
import com.x7ubi.kurswahl.common.models.Class;
import com.x7ubi.kurswahl.common.models.*;
import com.x7ubi.kurswahl.common.repository.*;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Year;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

@KurswahlServiceTest
public class AssignChoiceServiceTest {
    @Autowired
    private AssignChoiceService assignChoiceService;

    @Autowired
    private ChoiceRepo choiceRepo;

    @Autowired
    private ClassRepo classRepo;

    @Autowired
    private TapeRepo tapeRepo;

    @Autowired
    private SubjectAreaRepo subjectAreaRepo;

    @Autowired
    private SubjectRepo subjectRepo;

    @Autowired
    private TeacherRepo teacherRepo;

    @Autowired
    private StudentRepo studentRepo;

    @Autowired
    private StudentClassRepo studentClassRepo;

    @Autowired
    private RuleSetRepo ruleSetRepo;

    @Autowired
    private RuleRepo ruleRepo;

    @Autowired
    private ChoiceClassRepo choiceClassRepo;

    private Student student;

    private Tape tape;

    private Tape otherTape;

    private Subject subject;

    private Subject otherSubject;

    private Teacher teacher;

    private Class aClass;

    private Class bClass;

    private Choice choice;

    private Choice secondChoice;

    private Choice alternativeChoice;

    private StudentClass studentClass;

    @BeforeEach
    public void setupTest() {
        setupTapes();
        setupSubjects();
        setupTeachers();
        setupStudent();
    }

    private void setupStudentClass() {
        studentClass = new StudentClass();
        studentClass.setTeacher(teacher);
        studentClass.setName("E2a");
        studentClass.setReleaseYear(Year.now().getValue());
        studentClass.setYear(11);

        this.studentClassRepo.save(studentClass);

        teacher.setStudentClasses(new HashSet<>());
        teacher.getStudentClasses().add(studentClass);
        this.teacherRepo.save(teacher);

        studentClass = this.studentClassRepo.findStudentClassByName("E2a").get();
    }

    private void setupStudent() {
        setupStudentClass();
        User user = new User();
        user.setUsername("test.student");
        user.setFirstname("Test");
        user.setSurname("User");
        user.setPassword("Password");
        student = new Student();
        student.setUser(user);
        student.setChoices(new HashSet<>());
        student.setStudentClass(studentClass);
        studentClass.getStudents().add(student);

        this.studentRepo.save(student);
        this.studentClassRepo.save(studentClass);
    }

    private void setupChoice(Choice _choice, Integer choiceNumber, Class c) {

        _choice = new Choice();
        _choice.setChoiceNumber(choiceNumber);
        _choice.setReleaseYear(Year.now().getValue());
        _choice.setChoiceClasses(new HashSet<>());
        _choice.setStudent(student);

        this.choiceRepo.save(_choice);

        if (c != null) {
            ChoiceClass choiceClass = new ChoiceClass();
            choiceClass.setChoice(_choice);
            choiceClass.setaClass(c);
            choiceClass.setSelected(true);
            this.choiceClassRepo.save(choiceClass);

            _choice = this.choiceRepo.findChoiceByChoiceNumberAndStudent_StudentIdAndReleaseYear(choiceNumber,
                    student.getStudentId(), Year.now().getValue()).get();

            _choice.getChoiceClasses().add(choiceClass);
            this.choiceRepo.save(_choice);

            c.getChoiceClasses().add(choiceClass);
            this.classRepo.save(c);
        }
        student.getChoices().add(_choice);
        this.studentRepo.save(student);
    }

    private void setupTapes() {
        tape = new Tape();
        tape.setName("GK 1");
        tape.setYear(11);
        tape.setReleaseYear(Year.now().getValue());
        tape.setLk(false);
        tape.setaClass(new HashSet<>());

        tapeRepo.save(tape);

        otherTape = new Tape();
        otherTape.setName("GK 2");
        otherTape.setYear(11);
        otherTape.setReleaseYear(Year.now().getValue());
        otherTape.setLk(false);
        otherTape.setaClass(new HashSet<>());

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

        otherSubject = new Subject();
        otherSubject.setName("test 2");
        otherSubject.setSubjectArea(subjectArea);
        this.subjectRepo.save(otherSubject);
        subjectArea.getSubjects().add(otherSubject);

        this.subjectAreaRepo.save(subjectArea);
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

    private void setupClasses(Class c, String name, Tape tape, Teacher teacher, Subject subject) {
        c = new Class();
        c.setName(name);
        c.setTape(tape);
        c.setSubject(subject);
        c.setTeacher(teacher);
        this.classRepo.save(c);

        teacher = this.teacherRepo.findTeacherByUser_Username(teacher.getUser().getUsername()).get();
        teacher.getClasses().add(c);
        this.teacherRepo.save(teacher);

        subject = subjectRepo.findSubjectByName(subject.getName()).get();
        subject.setClasses(new HashSet<>());
        subject.getClasses().add(c);
        this.subjectRepo.save(subject);

        tape = tapeRepo.findTapeByNameAndYearAndReleaseYear(tape.getName(), tape.getYear(), tape.getReleaseYear()).get();
        tape.getaClass().add(c);
        tapeRepo.save(tape);
    }

    @Test
    public void testGetClassesWithStudents() {
        // Given
        setupClasses(aClass, "test", tape, teacher, subject);
        this.aClass = this.classRepo.findClassByName("test").get();
        setupChoice(choice, 1, aClass);
        choice = this.choiceRepo.findChoiceByChoiceNumberAndStudent_StudentIdAndReleaseYear(1,
                student.getStudentId(), Year.now().getValue()).get();

        Integer year = 11;

        // When
        List<ClassStudentsResponse> classStudentsResponses = this.assignChoiceService.getClassesWithStudents(year);

        // Then
        Assertions.assertEquals(classStudentsResponses.size(), 1);
        Assertions.assertEquals(classStudentsResponses.get(0).getName(), aClass.getName());
        Assertions.assertEquals(classStudentsResponses.get(0).getTapeName(), tape.getName());
        Assertions.assertEquals(classStudentsResponses.get(0).getTeacherResponse().getTeacherId(), teacher.getTeacherId());
        Assertions.assertEquals(classStudentsResponses.get(0).getStudentSurveillanceResponses().size(), 1);
        Assertions.assertEquals(classStudentsResponses.get(0).getStudentSurveillanceResponses().get(0).getStudentId(), student.getStudentId());
    }

    @Test
    public void testGetStundetChoices() throws EntityNotFoundException {
        // Given
        setupClasses(aClass, "test", tape, teacher, subject);
        this.aClass = this.classRepo.findClassByName("test").get();
        setupChoice(choice, 1, aClass);
        choice = this.choiceRepo.findChoiceByChoiceNumberAndStudent_StudentIdAndReleaseYear(1,
                student.getStudentId(), Year.now().getValue()).get();
        this.aClass = this.classRepo.findClassByName("test").get();
        this.student = this.studentRepo.findStudentByUser_Username("test.student").get();
        setupChoice(secondChoice, 2, aClass);
        secondChoice = this.choiceRepo.findChoiceByChoiceNumberAndStudent_StudentIdAndReleaseYear(2,
                student.getStudentId(), Year.now().getValue()).get();

        // When
        StudentChoicesResponse studentChoicesResponse = this.assignChoiceService.getStudentChoices(student.getStudentId());

        // Then
        Assertions.assertEquals(studentChoicesResponse.getStudentId(), student.getStudentId());
        Assertions.assertEquals(studentChoicesResponse.getChoiceResponses().size(), 2);

        studentChoicesResponse.getChoiceResponses().sort(Comparator.comparing(ChoiceResponse::getChoiceNumber));

        Assertions.assertEquals(studentChoicesResponse.getChoiceResponses().get(0).getChoiceNumber(), 1);
        Assertions.assertEquals(studentChoicesResponse.getChoiceResponses().get(0).getClassChoiceResponses().size(), 1);
        Assertions.assertEquals(studentChoicesResponse.getChoiceResponses().get(0).getClassChoiceResponses().get(0)
                .getClassId(), this.aClass.getClassId());
        Assertions.assertEquals(studentChoicesResponse.getChoiceResponses().get(0).getClassChoiceResponses().get(0)
                .getTapeId(), this.tape.getTapeId());
        Assertions.assertEquals(studentChoicesResponse.getChoiceResponses().get(1).getChoiceNumber(), 2);
        Assertions.assertEquals(studentChoicesResponse.getChoiceResponses().get(1).getClassChoiceResponses().size(), 1);
        Assertions.assertEquals(studentChoicesResponse.getChoiceResponses().get(1).getClassChoiceResponses().get(0)
                .getClassId(), this.aClass.getClassId());
        Assertions.assertEquals(studentChoicesResponse.getChoiceResponses().get(1).getClassChoiceResponses().get(0)
                .getTapeId(), this.tape.getTapeId());
    }

    @Test
    public void testGetStundetChoicesStudentNotFound() {
        // Given
        setupClasses(aClass, "test", tape, teacher, subject);
        this.aClass = this.classRepo.findClassByName("test").get();
        setupChoice(choice, 1, aClass);
        choice = this.choiceRepo.findChoiceByChoiceNumberAndStudent_StudentIdAndReleaseYear(1,
                student.getStudentId(), Year.now().getValue()).get();
        this.aClass = this.classRepo.findClassByName("test").get();
        this.student = this.studentRepo.findStudentByUser_Username("test.student").get();
        setupChoice(secondChoice, 2, aClass);
        secondChoice = this.choiceRepo.findChoiceByChoiceNumberAndStudent_StudentIdAndReleaseYear(2,
                student.getStudentId(), Year.now().getValue()).get();

        // When
        EntityNotFoundException entityNotFoundException = Assert.assertThrows(EntityNotFoundException.class, () ->
                this.assignChoiceService.getStudentChoices(student.getStudentId() + 3));

        // Then
        Assertions.assertEquals(entityNotFoundException.getMessage(), ErrorMessage.STUDENT_NOT_FOUND);
    }

    @Test
    public void testAssignChoice() throws EntityNotFoundException {
        // Given
        setupClasses(aClass, "test", tape, teacher, subject);
        this.aClass = this.classRepo.findClassByName("test").get();
        setupChoice(choice, 1, aClass);
        choice = this.choiceRepo.findChoiceByChoiceNumberAndStudent_StudentIdAndReleaseYear(1,
                student.getStudentId(), Year.now().getValue()).get();
        List<ChoiceClass> choiceClasses = this.choiceClassRepo.findAllByChoice_ChoiceId(choice.getChoiceId());

        // When
        StudentChoicesResponse studentChoicesResponse = this.assignChoiceService.assignChoice(choiceClasses.get(0).getChoiceClassId());

        // Then
        Assertions.assertEquals(studentChoicesResponse.getChoiceResponses().get(0).getChoiceNumber(), 1);
        Assertions.assertEquals(studentChoicesResponse.getChoiceResponses().get(0).getClassChoiceResponses().size(), 1);
        Assertions.assertEquals(studentChoicesResponse.getChoiceResponses().get(0).getClassChoiceResponses().get(0)
                .getClassId(), this.aClass.getClassId());
        Assertions.assertEquals(studentChoicesResponse.getChoiceResponses().get(0).getClassChoiceResponses().get(0)
                .getTapeId(), this.tape.getTapeId());
        Assertions.assertTrue(studentChoicesResponse.getChoiceResponses().get(0).getClassChoiceResponses().get(0)
                .isSelected());
    }

    @Test
    public void testAssignChoiceChoiceClassNotFound() {
        // Given
        setupClasses(aClass, "test", tape, teacher, subject);
        this.aClass = this.classRepo.findClassByName("test").get();
        setupChoice(choice, 1, aClass);
        choice = this.choiceRepo.findChoiceByChoiceNumberAndStudent_StudentIdAndReleaseYear(1,
                student.getStudentId(), Year.now().getValue()).get();
        List<ChoiceClass> choiceClasses = this.choiceClassRepo.findAllByChoice_ChoiceId(choice.getChoiceId());

        // When
        EntityNotFoundException entityNotFoundException = Assert.assertThrows(EntityNotFoundException.class, () ->
                this.assignChoiceService.assignChoice(choiceClasses.get(0).getChoiceClassId() + 3));

        // Then
        Assertions.assertEquals(entityNotFoundException.getMessage(), ErrorMessage.CHOICE_NOT_FOUND);
    }

    @Test
    public void testAssignChoiceSameSubject() throws EntityNotFoundException {
        // Given
        setupClasses(aClass, "test", tape, teacher, subject);
        setupClasses(bClass, "test 2", otherTape, teacher, subject);
        this.aClass = this.classRepo.findClassByName("test").get();
        this.bClass = this.classRepo.findClassByName("test 2").get();
        setupChoice(choice, 1, aClass);
        choice = this.choiceRepo.findChoiceByChoiceNumberAndStudent_StudentIdAndReleaseYear(1,
                student.getStudentId(), Year.now().getValue()).get();
        this.student = this.studentRepo.findStudentByUser_Username("test.student").get();
        setupChoice(secondChoice, 2, bClass);
        secondChoice = this.choiceRepo.findChoiceByChoiceNumberAndStudent_StudentIdAndReleaseYear(2,
                student.getStudentId(), Year.now().getValue()).get();
        List<ChoiceClass> choiceClasses = this.choiceClassRepo.findAllByChoice_ChoiceId(choice.getChoiceId());

        // When
        StudentChoicesResponse studentChoicesResponse = this.assignChoiceService.assignChoice(choiceClasses.get(0).getChoiceClassId());

        // Then
        studentChoicesResponse.getChoiceResponses().sort(Comparator.comparing(ChoiceResponse::getChoiceNumber));

        Assertions.assertEquals(studentChoicesResponse.getChoiceResponses().get(0).getChoiceNumber(), 1);
        Assertions.assertEquals(studentChoicesResponse.getChoiceResponses().get(0).getClassChoiceResponses().size(), 1);
        Assertions.assertEquals(studentChoicesResponse.getChoiceResponses().get(0).getClassChoiceResponses().get(0)
                .getClassId(), this.aClass.getClassId());
        Assertions.assertEquals(studentChoicesResponse.getChoiceResponses().get(0).getClassChoiceResponses().get(0)
                .getTapeId(), this.tape.getTapeId());
        Assertions.assertTrue(studentChoicesResponse.getChoiceResponses().get(0).getClassChoiceResponses().get(0)
                .isSelected());
        Assertions.assertEquals(studentChoicesResponse.getChoiceResponses().get(1).getChoiceNumber(), 2);
        Assertions.assertEquals(studentChoicesResponse.getChoiceResponses().get(1).getClassChoiceResponses().size(), 1);
        Assertions.assertEquals(studentChoicesResponse.getChoiceResponses().get(1).getClassChoiceResponses().get(0)
                .getClassId(), this.bClass.getClassId());
        Assertions.assertEquals(studentChoicesResponse.getChoiceResponses().get(1).getClassChoiceResponses().get(0)
                .getTapeId(), this.otherTape.getTapeId());
        Assertions.assertFalse(studentChoicesResponse.getChoiceResponses().get(1).getClassChoiceResponses().get(0)
                .isSelected());
    }

    @Test
    public void testAssignChoiceSameTape() throws EntityNotFoundException {
        // Given
        setupClasses(aClass, "test", tape, teacher, subject);
        setupClasses(bClass, "test 2", tape, teacher, otherSubject);
        this.aClass = this.classRepo.findClassByName("test").get();
        this.bClass = this.classRepo.findClassByName("test 2").get();
        setupChoice(choice, 1, aClass);
        choice = this.choiceRepo.findChoiceByChoiceNumberAndStudent_StudentIdAndReleaseYear(1,
                student.getStudentId(), Year.now().getValue()).get();
        this.student = this.studentRepo.findStudentByUser_Username("test.student").get();
        setupChoice(secondChoice, 2, bClass);
        secondChoice = this.choiceRepo.findChoiceByChoiceNumberAndStudent_StudentIdAndReleaseYear(2,
                student.getStudentId(), Year.now().getValue()).get();
        List<ChoiceClass> choiceClasses = this.choiceClassRepo.findAllByChoice_ChoiceId(choice.getChoiceId());

        // When
        StudentChoicesResponse studentChoicesResponse = this.assignChoiceService.assignChoice(choiceClasses.get(0).getChoiceClassId());

        // Then
        studentChoicesResponse.getChoiceResponses().sort(Comparator.comparing(ChoiceResponse::getChoiceNumber));

        Assertions.assertEquals(studentChoicesResponse.getChoiceResponses().get(0).getChoiceNumber(), 1);
        Assertions.assertEquals(studentChoicesResponse.getChoiceResponses().get(0).getClassChoiceResponses().size(), 1);
        Assertions.assertEquals(studentChoicesResponse.getChoiceResponses().get(0).getClassChoiceResponses().get(0)
                .getClassId(), this.aClass.getClassId());
        Assertions.assertEquals(studentChoicesResponse.getChoiceResponses().get(0).getClassChoiceResponses().get(0)
                .getTapeId(), this.tape.getTapeId());
        Assertions.assertTrue(studentChoicesResponse.getChoiceResponses().get(0).getClassChoiceResponses().get(0)
                .isSelected());
        Assertions.assertEquals(studentChoicesResponse.getChoiceResponses().get(1).getChoiceNumber(), 2);
        Assertions.assertEquals(studentChoicesResponse.getChoiceResponses().get(1).getClassChoiceResponses().size(), 1);
        Assertions.assertEquals(studentChoicesResponse.getChoiceResponses().get(1).getClassChoiceResponses().get(0)
                .getClassId(), this.bClass.getClassId());
        Assertions.assertEquals(studentChoicesResponse.getChoiceResponses().get(1).getClassChoiceResponses().get(0)
                .getTapeId(), this.tape.getTapeId());
        Assertions.assertFalse(studentChoicesResponse.getChoiceResponses().get(1).getClassChoiceResponses().get(0)
                .isSelected());
    }

    @Test
    public void testDeleteChoiceSelection() throws EntityNotFoundException {
        // Given
        setupClasses(aClass, "test", tape, teacher, subject);
        this.aClass = this.classRepo.findClassByName("test").get();
        setupChoice(secondChoice, 2, aClass);
        secondChoice = this.choiceRepo.findChoiceByChoiceNumberAndStudent_StudentIdAndReleaseYear(2,
                student.getStudentId(), Year.now().getValue()).get();
        List<ChoiceClass> choiceClasses = this.choiceClassRepo.findAllByChoice_ChoiceId(secondChoice.getChoiceId());

        // When
        StudentChoicesResponse studentChoicesResponse = this.assignChoiceService.deleteChoiceSelection(choiceClasses.get(0).getChoiceClassId());

        // Then
        Assertions.assertEquals(studentChoicesResponse.getChoiceResponses().get(0).getChoiceNumber(), 2);
        Assertions.assertEquals(studentChoicesResponse.getChoiceResponses().get(0).getClassChoiceResponses().size(), 1);
        Assertions.assertEquals(studentChoicesResponse.getChoiceResponses().get(0).getClassChoiceResponses().get(0)
                .getClassId(), this.aClass.getClassId());
        Assertions.assertEquals(studentChoicesResponse.getChoiceResponses().get(0).getClassChoiceResponses().get(0)
                .getTapeId(), this.tape.getTapeId());
        Assertions.assertFalse(studentChoicesResponse.getChoiceResponses().get(0).getClassChoiceResponses().get(0)
                .isSelected());
    }

    @Test
    public void testDeleteChoiceSelectionChoiceClassNotFound() {
        // Given
        setupClasses(aClass, "test", tape, teacher, subject);
        this.aClass = this.classRepo.findClassByName("test").get();
        setupChoice(secondChoice, 2, aClass);
        secondChoice = this.choiceRepo.findChoiceByChoiceNumberAndStudent_StudentIdAndReleaseYear(2,
                student.getStudentId(), Year.now().getValue()).get();
        List<ChoiceClass> choiceClasses = this.choiceClassRepo.findAllByChoice_ChoiceId(secondChoice.getChoiceId());

        // When
        EntityNotFoundException entityNotFoundException = Assert.assertThrows(EntityNotFoundException.class, () ->
                this.assignChoiceService.deleteChoiceSelection(choiceClasses.get(0).getChoiceClassId() + 3));

        // Then
        Assertions.assertEquals(entityNotFoundException.getMessage(), ErrorMessage.CHOICE_NOT_FOUND);
    }

    @Test
    public void testAssignAlternativeChoice() throws EntityNotFoundException {
        // Given
        setupClasses(aClass, "test", tape, teacher, subject);
        this.aClass = this.classRepo.findClassByName("test").get();
        setupChoice(alternativeChoice, 3, null);

        AlternateChoiceRequest alternateChoiceRequest = new AlternateChoiceRequest();
        alternateChoiceRequest.setStudentId(this.student.getStudentId());
        alternateChoiceRequest.setClassId(aClass.getClassId());

        // When
        StudentChoicesResponse studentChoicesResponse = this.assignChoiceService.assignAlternateChoice(alternateChoiceRequest);

        // Then
        Assertions.assertEquals(studentChoicesResponse.getChoiceResponses().get(0).getChoiceNumber(), 3);
        Assertions.assertEquals(studentChoicesResponse.getChoiceResponses().get(0).getClassChoiceResponses().size(), 1);
        Assertions.assertEquals(studentChoicesResponse.getChoiceResponses().get(0).getClassChoiceResponses().get(0)
                .getClassId(), this.aClass.getClassId());
        Assertions.assertEquals(studentChoicesResponse.getChoiceResponses().get(0).getClassChoiceResponses().get(0)
                .getTapeId(), this.tape.getTapeId());
        Assertions.assertTrue(studentChoicesResponse.getChoiceResponses().get(0).getClassChoiceResponses().get(0)
                .isSelected());
    }

    @Test
    public void testAssignAlternativeChoiceCreatesChoice() throws EntityNotFoundException {
        // Given
        setupClasses(aClass, "test", tape, teacher, subject);
        this.aClass = this.classRepo.findClassByName("test").get();

        AlternateChoiceRequest alternateChoiceRequest = new AlternateChoiceRequest();
        alternateChoiceRequest.setStudentId(this.student.getStudentId());
        alternateChoiceRequest.setClassId(aClass.getClassId());

        // When
        StudentChoicesResponse studentChoicesResponse = this.assignChoiceService.assignAlternateChoice(alternateChoiceRequest);

        // Then
        Assertions.assertEquals(studentChoicesResponse.getChoiceResponses().get(0).getChoiceNumber(), 3);
        Assertions.assertEquals(studentChoicesResponse.getChoiceResponses().get(0).getClassChoiceResponses().size(), 1);
        Assertions.assertEquals(studentChoicesResponse.getChoiceResponses().get(0).getClassChoiceResponses().get(0)
                .getClassId(), this.aClass.getClassId());
        Assertions.assertEquals(studentChoicesResponse.getChoiceResponses().get(0).getClassChoiceResponses().get(0)
                .getTapeId(), this.tape.getTapeId());
        Assertions.assertTrue(studentChoicesResponse.getChoiceResponses().get(0).getClassChoiceResponses().get(0)
                .isSelected());
    }

    @Test
    public void testAssignAlternativeChoiceSameSubject() throws EntityNotFoundException {
        // Given
        setupClasses(aClass, "test", tape, teacher, subject);
        setupClasses(bClass, "test 2", otherTape, teacher, subject);
        this.aClass = this.classRepo.findClassByName("test").get();
        this.bClass = this.classRepo.findClassByName("test 2").get();
        setupChoice(choice, 1, aClass);

        AlternateChoiceRequest alternateChoiceRequest = new AlternateChoiceRequest();
        alternateChoiceRequest.setStudentId(this.student.getStudentId());
        alternateChoiceRequest.setClassId(bClass.getClassId());

        // When
        StudentChoicesResponse studentChoicesResponse = this.assignChoiceService.assignAlternateChoice(alternateChoiceRequest);

        // Then
        studentChoicesResponse.getChoiceResponses().sort(Comparator.comparing(ChoiceResponse::getChoiceNumber));

        Assertions.assertEquals(studentChoicesResponse.getChoiceResponses().get(0).getChoiceNumber(), 1);
        Assertions.assertEquals(studentChoicesResponse.getChoiceResponses().get(0).getClassChoiceResponses().size(), 1);
        Assertions.assertEquals(studentChoicesResponse.getChoiceResponses().get(0).getClassChoiceResponses().get(0)
                .getClassId(), this.aClass.getClassId());
        Assertions.assertEquals(studentChoicesResponse.getChoiceResponses().get(0).getClassChoiceResponses().get(0)
                .getTapeId(), this.tape.getTapeId());
        Assertions.assertFalse(studentChoicesResponse.getChoiceResponses().get(0).getClassChoiceResponses().get(0)
                .isSelected());
        Assertions.assertEquals(studentChoicesResponse.getChoiceResponses().get(1).getChoiceNumber(), 3);
        Assertions.assertEquals(studentChoicesResponse.getChoiceResponses().get(1).getClassChoiceResponses().size(), 1);
        Assertions.assertEquals(studentChoicesResponse.getChoiceResponses().get(1).getClassChoiceResponses().get(0)
                .getClassId(), this.bClass.getClassId());
        Assertions.assertEquals(studentChoicesResponse.getChoiceResponses().get(1).getClassChoiceResponses().get(0)
                .getTapeId(), this.otherTape.getTapeId());
        Assertions.assertTrue(studentChoicesResponse.getChoiceResponses().get(1).getClassChoiceResponses().get(0)
                .isSelected());
    }

    @Test
    public void testAssignAlternativeChoiceSameTape() throws EntityNotFoundException {
        // Given
        setupClasses(aClass, "test", tape, teacher, subject);
        setupClasses(bClass, "test 2", tape, teacher, otherSubject);
        this.aClass = this.classRepo.findClassByName("test").get();
        this.bClass = this.classRepo.findClassByName("test 2").get();
        setupChoice(choice, 1, aClass);

        AlternateChoiceRequest alternateChoiceRequest = new AlternateChoiceRequest();
        alternateChoiceRequest.setStudentId(this.student.getStudentId());
        alternateChoiceRequest.setClassId(bClass.getClassId());

        // When
        StudentChoicesResponse studentChoicesResponse = this.assignChoiceService.assignAlternateChoice(alternateChoiceRequest);

        // Then
        studentChoicesResponse.getChoiceResponses().sort(Comparator.comparing(ChoiceResponse::getChoiceNumber));

        Assertions.assertEquals(studentChoicesResponse.getChoiceResponses().get(0).getChoiceNumber(), 1);
        Assertions.assertEquals(studentChoicesResponse.getChoiceResponses().get(0).getClassChoiceResponses().size(), 1);
        Assertions.assertEquals(studentChoicesResponse.getChoiceResponses().get(0).getClassChoiceResponses().get(0)
                .getClassId(), this.aClass.getClassId());
        Assertions.assertEquals(studentChoicesResponse.getChoiceResponses().get(0).getClassChoiceResponses().get(0)
                .getTapeId(), this.tape.getTapeId());
        Assertions.assertFalse(studentChoicesResponse.getChoiceResponses().get(0).getClassChoiceResponses().get(0)
                .isSelected());
        Assertions.assertEquals(studentChoicesResponse.getChoiceResponses().get(1).getChoiceNumber(), 3);
        Assertions.assertEquals(studentChoicesResponse.getChoiceResponses().get(1).getClassChoiceResponses().size(), 1);
        Assertions.assertEquals(studentChoicesResponse.getChoiceResponses().get(1).getClassChoiceResponses().get(0)
                .getClassId(), this.bClass.getClassId());
        Assertions.assertEquals(studentChoicesResponse.getChoiceResponses().get(1).getClassChoiceResponses().get(0)
                .getTapeId(), this.tape.getTapeId());
        Assertions.assertTrue(studentChoicesResponse.getChoiceResponses().get(1).getClassChoiceResponses().get(0)
                .isSelected());
    }

    @Test
    public void testAssignAlternativeChoiceClassNotFound() throws EntityNotFoundException {
        // Given
        setupClasses(aClass, "test", tape, teacher, subject);
        this.aClass = this.classRepo.findClassByName("test").get();

        AlternateChoiceRequest alternateChoiceRequest = new AlternateChoiceRequest();
        alternateChoiceRequest.setStudentId(this.student.getStudentId());
        alternateChoiceRequest.setClassId(aClass.getClassId() + 3);

        // When
        EntityNotFoundException entityNotFoundException = Assert.assertThrows(EntityNotFoundException.class, () ->
                this.assignChoiceService.assignAlternateChoice(alternateChoiceRequest));

        // Then
        Assertions.assertEquals(entityNotFoundException.getMessage(), ErrorMessage.CLASS_NOT_FOUND);
    }

    @Test
    public void testAssignAlternativeChoiceStudentNotFound() throws EntityNotFoundException {
        // Given
        setupClasses(aClass, "test", tape, teacher, subject);
        this.aClass = this.classRepo.findClassByName("test").get();

        AlternateChoiceRequest alternateChoiceRequest = new AlternateChoiceRequest();
        alternateChoiceRequest.setStudentId(this.student.getStudentId() + 3);
        alternateChoiceRequest.setClassId(aClass.getClassId());

        // When
        EntityNotFoundException entityNotFoundException = Assert.assertThrows(EntityNotFoundException.class, () ->
                this.assignChoiceService.assignAlternateChoice(alternateChoiceRequest));

        // Then
        Assertions.assertEquals(entityNotFoundException.getMessage(), ErrorMessage.STUDENT_NOT_FOUND);
    }

    @Test
    public void testGetTapes() {
        // Given
        setupClasses(aClass, "test", tape, teacher, subject);
        setupClasses(bClass, "test 2", otherTape, teacher, otherSubject);
        this.aClass = this.classRepo.findClassByName("test").get();
        this.bClass = this.classRepo.findClassByName("test 2").get();

        // When
        List<ChoiceTapeResponse> choiceTapeResponses = this.assignChoiceService.getTapes(11);

        // Then
        Assertions.assertEquals(choiceTapeResponses.size(), 2);

        Assertions.assertEquals(choiceTapeResponses.get(0).getName(), tape.getName());
        Assertions.assertEquals(choiceTapeResponses.get(0).getTapeId(), tape.getTapeId());
        Assertions.assertEquals(choiceTapeResponses.get(0).getChoiceTapeClassResponses().size(), 1);
        Assertions.assertEquals(choiceTapeResponses.get(0).getChoiceTapeClassResponses().get(0).getClassId()
                , aClass.getClassId());
        Assertions.assertEquals(choiceTapeResponses.get(0).getChoiceTapeClassResponses().get(0).getTeacherResponse()
                .getTeacherId(), teacher.getTeacherId());

        Assertions.assertEquals(choiceTapeResponses.get(1).getName(), otherTape.getName());
        Assertions.assertEquals(choiceTapeResponses.get(1).getTapeId(), otherTape.getTapeId());
        Assertions.assertEquals(choiceTapeResponses.get(1).getChoiceTapeClassResponses().size(), 1);
        Assertions.assertEquals(choiceTapeResponses.get(1).getChoiceTapeClassResponses().get(0).getClassId(),
                bClass.getClassId());
        Assertions.assertEquals(choiceTapeResponses.get(1).getChoiceTapeClassResponses().get(0).getTeacherResponse()
                .getTeacherId(), teacher.getTeacherId());
    }

    @Test
    public void testDeleteAlternativeChoiceClass() throws EntityNotFoundException {
        // Given
        setupClasses(aClass, "test", tape, teacher, subject);
        this.aClass = this.classRepo.findClassByName("test").get();
        setupChoice(alternativeChoice, 3, aClass);
        alternativeChoice = this.choiceRepo.findChoiceByChoiceNumberAndStudent_StudentIdAndReleaseYear(3,
                student.getStudentId(), Year.now().getValue()).get();
        List<ChoiceClass> choiceClasses = this.choiceClassRepo.findAllByChoice_ChoiceId(alternativeChoice.getChoiceId());

        // When
        StudentChoicesResponse studentChoicesResponse = this.assignChoiceService.deleteAlternativeChoiceClass(
                choiceClasses.get(0).getChoiceClassId());

        // Then
        Assertions.assertEquals(studentChoicesResponse.getChoiceResponses().get(0).getChoiceNumber(), 3);
        Assertions.assertTrue(studentChoicesResponse.getChoiceResponses().get(0).getClassChoiceResponses().isEmpty());
    }

    @Test
    public void testDeleteAlternativeChoiceClassChoiceClassNotFound() throws EntityNotFoundException {
        // Given
        setupClasses(aClass, "test", tape, teacher, subject);
        this.aClass = this.classRepo.findClassByName("test").get();
        setupChoice(alternativeChoice, 3, aClass);
        alternativeChoice = this.choiceRepo.findChoiceByChoiceNumberAndStudent_StudentIdAndReleaseYear(3,
                student.getStudentId(), Year.now().getValue()).get();
        List<ChoiceClass> choiceClasses = this.choiceClassRepo.findAllByChoice_ChoiceId(alternativeChoice.getChoiceId());

        // When
        EntityNotFoundException entityNotFoundException = Assert.assertThrows(EntityNotFoundException.class, () ->
                this.assignChoiceService.deleteAlternativeChoiceClass(
                        choiceClasses.get(0).getChoiceClassId() + 3));

        // Then
        Assertions.assertEquals(entityNotFoundException.getMessage(), ErrorMessage.CHOICE_NOT_FOUND);
    }
}
