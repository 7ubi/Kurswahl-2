package com.x7ubi.kurswahl.student.choice;

import com.x7ubi.kurswahl.KurswahlServiceTest;
import com.x7ubi.kurswahl.common.error.ErrorMessage;
import com.x7ubi.kurswahl.common.exception.EntityNotFoundException;
import com.x7ubi.kurswahl.common.exception.UnauthorizedException;
import com.x7ubi.kurswahl.common.models.Class;
import com.x7ubi.kurswahl.common.models.*;
import com.x7ubi.kurswahl.common.repository.*;
import com.x7ubi.kurswahl.student.choice.request.AlterStudentChoiceRequest;
import com.x7ubi.kurswahl.student.choice.request.DeleteClassFromChoiceRequest;
import com.x7ubi.kurswahl.student.choice.response.ChoiceResponse;
import com.x7ubi.kurswahl.student.choice.response.SubjectTapeResponse;
import com.x7ubi.kurswahl.student.choice.response.TapeClassResponse;
import com.x7ubi.kurswahl.student.choice.service.StudentChoiceService;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Year;
import java.util.HashSet;
import java.util.List;

@KurswahlServiceTest
public class StudentChoiceServiceTest {
    @Autowired
    private StudentChoiceService studentChoiceService;

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
    private ChoiceClassRepo choiceClassRepo;

    private Student student;

    private Tape tape;

    private Tape otherTape;

    private Subject subject;

    private Subject subjectOther;

    private Teacher teacher;

    private Class aClass;

    private Class bClass;

    private Choice choice;

    private Choice secondChoice;

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

    private void setupChoice(Class c) {

        choice = new Choice();
        choice.setChoiceNumber(1);
        choice.setReleaseYear(Year.now().getValue());
        choice.setChoiceClasses(new HashSet<>());
        choice.setStudent(student);

        this.choiceRepo.save(choice);

        ChoiceClass choiceClass = new ChoiceClass();
        choiceClass.setChoice(choice);
        choiceClass.setaClass(c);
        this.choiceClassRepo.save(choiceClass);

        choice = this.choiceRepo.findChoiceByChoiceNumberAndStudent_StudentIdAndReleaseYear(1,
                student.getStudentId(), Year.now().getValue()).get();

        choice.getChoiceClasses().add(choiceClass);
        this.choiceRepo.save(choice);

        c.getChoiceClasses().add(choiceClass);
        this.classRepo.save(c);

        student.getChoices().add(choice);
        this.studentRepo.save(student);
    }

    private void setupSecondChoice(Class c) {

        secondChoice = new Choice();
        secondChoice.setChoiceNumber(2);
        secondChoice.setReleaseYear(Year.now().getValue());
        secondChoice.setChoiceClasses(new HashSet<>());
        secondChoice.setStudent(student);

        this.choiceRepo.save(secondChoice);

        ChoiceClass choiceClass = new ChoiceClass();
        choiceClass.setChoice(secondChoice);
        choiceClass.setaClass(c);
        this.choiceClassRepo.save(choiceClass);

        secondChoice = this.choiceRepo.findChoiceByChoiceNumberAndStudent_StudentIdAndReleaseYear(2,
                student.getStudentId(), Year.now().getValue()).get();

        secondChoice.getChoiceClasses().add(choiceClass);
        this.choiceRepo.save(secondChoice);

        c.getChoiceClasses().add(choiceClass);
        this.classRepo.save(c);

        student.getChoices().add(secondChoice);
        this.studentRepo.save(student);
    }

    private void setupTapes() {
        tape = new Tape();
        tape.setName("GK 1");
        tape.setYear(11);
        tape.setReleaseYear(Year.now().getValue());
        tape.setLk(false);
        tape.setaClass(new HashSet<>());

        tape = tapeRepo.save(tape);

        otherTape = new Tape();
        otherTape.setName("LK 1");
        otherTape.setYear(11);
        otherTape.setReleaseYear(Year.now().getValue());
        otherTape.setLk(true);
        otherTape.setaClass(new HashSet<>());

        otherTape = tapeRepo.save(otherTape);
    }

    public void setupSubjects() {
        SubjectArea subjectArea = new SubjectArea();
        subjectArea.setName("Subject Area");

        subjectArea = this.subjectAreaRepo.save(subjectArea);
        subjectArea = this.subjectAreaRepo.findSubjectAreaByName(subjectArea.getName()).get();

        subject = new Subject();
        subject.setName("test");
        subject.setSubjectArea(subjectArea);
        subject = this.subjectRepo.save(subject);
        subjectArea.getSubjects().add(subject);

        subjectOther = new Subject();
        subjectOther.setName("test other");
        subjectOther.setSubjectArea(subjectArea);
        subjectOther = this.subjectRepo.save(subjectOther);
        subjectArea.getSubjects().add(subjectOther);
        subjectArea = this.subjectAreaRepo.save(subjectArea);
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
        teacher.setClasses(new HashSet<>());
        teacher.setStudentClasses(new HashSet<>());

        teacher = this.teacherRepo.save(teacher);
    }

    private void setupClasses(Class c, String name, Tape tape, Teacher teacher, Subject subject) {
        c = new Class();
        c.setName(name);
        c.setTape(tape);
        c.setSubject(subject);
        c.setTeacher(teacher);
        c = this.classRepo.save(c);

        teacher.getClasses().add(c);
        this.teacherRepo.save(teacher);

        subject.setClasses(new HashSet<>());
        subject.getClasses().add(c);
        this.subjectRepo.save(subject);

        tape.getaClass().add(c);
        tapeRepo.save(tape);
    }

    @Test
    public void testAlterChoice() throws EntityNotFoundException, UnauthorizedException {
        // Given
        choice = new Choice();
        choice.setChoiceNumber(1);
        choice.setReleaseYear(Year.now().getValue());
        choice.setStudent(student);
        this.choiceRepo.save(choice);

        student.getChoices().add(choice);
        studentRepo.save(student);

        setupClasses(aClass, "test", this.tape, this.teacher, this.subject);
        aClass = this.classRepo.findClassByName("test").get();
        AlterStudentChoiceRequest alterStudentChoiceRequest = new AlterStudentChoiceRequest();
        alterStudentChoiceRequest.setChoiceNumber(1);
        alterStudentChoiceRequest.setClassId(aClass.getClassId());

        // When
        ChoiceResponse choiceResponse = this.studentChoiceService.alterChoice(student.getUser().getUsername(),
                alterStudentChoiceRequest);

        // Then
        Choice created_choice = this.choiceRepo.findChoiceByChoiceNumberAndStudent_StudentIdAndReleaseYear(
                1, student.getStudentId(), Year.now().getValue()).get();


        Assertions.assertEquals(choiceResponse.getChoiceId(), created_choice.getChoiceId());

        Assertions.assertEquals(created_choice.getChoiceNumber(), 1);
        Assertions.assertEquals(created_choice.getChoiceClasses().size(), 1);
        Assertions.assertEquals(created_choice.getChoiceClasses().stream().toList().get(0).getaClass().getClassId(), aClass.getClassId());

        aClass = this.classRepo.findClassByClassId(aClass.getClassId()).get();
        Assertions.assertEquals(aClass.getChoiceClasses().size(), 1);
        Assertions.assertEquals(aClass.getChoiceClasses().stream().toList().get(0).getChoice().getChoiceId(), created_choice.getChoiceId());
    }

    @Test
    public void testAlterChoiceSelectChoiceClassesFirstChoice() throws EntityNotFoundException, UnauthorizedException {
        // Given
        setupClasses(aClass, "test", this.tape, this.teacher, this.subject);
        aClass = this.classRepo.findClassByName("test").get();
        setupChoice(aClass);

        AlterStudentChoiceRequest alterStudentChoiceRequest = new AlterStudentChoiceRequest();
        alterStudentChoiceRequest.setChoiceNumber(2);
        alterStudentChoiceRequest.setClassId(aClass.getClassId());

        // When
        ChoiceResponse choiceResponse = this.studentChoiceService.alterChoice(student.getUser().getUsername(),
                alterStudentChoiceRequest);

        // Then
        Choice firstChoice = this.choiceRepo.findChoiceByChoiceNumberAndStudent_StudentIdAndReleaseYear(
                1, student.getStudentId(), Year.now().getValue()).get();

        Assertions.assertEquals(firstChoice.getChoiceNumber(), 1);
        Assertions.assertEquals(firstChoice.getChoiceClasses().size(), 1);
        Assertions.assertEquals(firstChoice.getChoiceClasses().stream().toList().get(0).getaClass().getClassId(), aClass.getClassId());
        Assertions.assertTrue(firstChoice.getChoiceClasses().stream().toList().get(0).isSelected());
    }

    @Test
    public void testAlterChoiceNewChoice() throws EntityNotFoundException, UnauthorizedException {
        // Given
        setupClasses(aClass, "test", this.tape, this.teacher, this.subject);
        aClass = this.classRepo.findClassByName("test").get();
        AlterStudentChoiceRequest alterStudentChoiceRequest = new AlterStudentChoiceRequest();
        alterStudentChoiceRequest.setChoiceNumber(1);
        alterStudentChoiceRequest.setClassId(aClass.getClassId());

        // When
        ChoiceResponse choiceResponse = this.studentChoiceService.alterChoice(student.getUser().getUsername(),
                alterStudentChoiceRequest);

        // Then
        Choice created_choice = this.choiceRepo.findChoiceByChoiceNumberAndStudent_StudentIdAndReleaseYear(
                1, student.getStudentId(), Year.now().getValue()).get();

        Assertions.assertEquals(choiceResponse.getChoiceId(), created_choice.getChoiceId());


        Assertions.assertEquals(created_choice.getChoiceNumber(), 1);
        Assertions.assertEquals(created_choice.getChoiceClasses().size(), 1);
        Assertions.assertEquals(created_choice.getChoiceClasses().stream().toList().get(0).getaClass().getClassId(), aClass.getClassId());

        aClass = this.classRepo.findClassByClassId(aClass.getClassId()).get();
        Assertions.assertEquals(aClass.getChoiceClasses().size(), 1);
        Assertions.assertEquals(aClass.getChoiceClasses().stream().toList().get(0).getChoice().getChoiceId(), created_choice.getChoiceId());
    }

    @Test
    public void testAlterChoiceSameTape() throws EntityNotFoundException, UnauthorizedException {
        // Given
        setupClasses(aClass, "test", this.tape, this.teacher, this.subject);
        setupClasses(this.bClass, "test 2", this.tape, this.teacher, this.subjectOther);
        aClass = this.classRepo.findClassByName("test").get();
        this.bClass = this.classRepo.findClassByName("test 2").get();
        setupChoice(this.bClass);
        AlterStudentChoiceRequest alterStudentChoiceRequest = new AlterStudentChoiceRequest();
        alterStudentChoiceRequest.setChoiceNumber(1);
        alterStudentChoiceRequest.setClassId(aClass.getClassId());

        // When
        this.studentChoiceService.alterChoice(student.getUser().getUsername(), alterStudentChoiceRequest);

        // Then
        Choice created_choice = this.choiceRepo.findChoiceByChoiceNumberAndStudent_StudentIdAndReleaseYear(
                1, student.getStudentId(), Year.now().getValue()).get();

        Assertions.assertEquals(created_choice.getChoiceNumber(), 1);
        Assertions.assertEquals(created_choice.getChoiceClasses().size(), 1);
        Assertions.assertEquals(created_choice.getChoiceClasses().stream().toList().get(0).getaClass().getClassId(), aClass.getClassId());

        aClass = this.classRepo.findClassByClassId(aClass.getClassId()).get();
        Assertions.assertEquals(aClass.getChoiceClasses().size(), 1);
        Assertions.assertEquals(aClass.getChoiceClasses().stream().toList().get(0).getChoice().getChoiceId(), created_choice.getChoiceId());

        this.bClass = this.classRepo.findClassByClassId(this.bClass.getClassId()).get();
        Assertions.assertTrue(this.bClass.getChoiceClasses().isEmpty());
    }

    @Test
    public void testAlterChoiceSameSubject() throws EntityNotFoundException, UnauthorizedException {
        // Given
        setupClasses(aClass, "test", this.tape, this.teacher, this.subject);
        setupClasses(this.bClass, "test 2", this.otherTape, this.teacher, this.subject);
        aClass = this.classRepo.findClassByName("test").get();
        this.bClass = this.classRepo.findClassByName("test 2").get();
        setupChoice(this.bClass);
        AlterStudentChoiceRequest alterStudentChoiceRequest = new AlterStudentChoiceRequest();
        alterStudentChoiceRequest.setChoiceNumber(1);
        alterStudentChoiceRequest.setClassId(aClass.getClassId());

        // When
        this.studentChoiceService.alterChoice(student.getUser().getUsername(), alterStudentChoiceRequest);

        // Then
        Choice created_choice = this.choiceRepo.findChoiceByChoiceNumberAndStudent_StudentIdAndReleaseYear(
                1, student.getStudentId(), Year.now().getValue()).get();

        Assertions.assertEquals(created_choice.getChoiceNumber(), 1);
        Assertions.assertEquals(created_choice.getChoiceClasses().size(), 1);
        Assertions.assertEquals(created_choice.getChoiceClasses().stream().toList().get(0).getaClass().getClassId(), aClass.getClassId());

        aClass = this.classRepo.findClassByClassId(aClass.getClassId()).get();
        Assertions.assertEquals(aClass.getChoiceClasses().size(), 1);
        Assertions.assertEquals(aClass.getChoiceClasses().stream().toList().get(0).getChoice().getChoiceId(), created_choice.getChoiceId());

        this.bClass = this.classRepo.findClassByClassId(this.bClass.getClassId()).get();
        Assertions.assertTrue(this.bClass.getChoiceClasses().isEmpty());
    }

    @Test
    public void testAlterChoiceStudentNotFound() {
        // Given
        setupClasses(aClass, "test", this.tape, this.teacher, this.subject);
        aClass = this.classRepo.findClassByName("test").get();
        AlterStudentChoiceRequest alterStudentChoiceRequest = new AlterStudentChoiceRequest();
        alterStudentChoiceRequest.setChoiceNumber(1);
        alterStudentChoiceRequest.setClassId(aClass.getClassId());

        // When
        EntityNotFoundException entityNotFoundException = Assert.assertThrows(EntityNotFoundException.class, () ->
                this.studentChoiceService.alterChoice("wrong", alterStudentChoiceRequest));

        // Then
        Assertions.assertEquals(entityNotFoundException.getMessage(), ErrorMessage.STUDENT_NOT_FOUND);

        Assertions.assertTrue(this.choiceRepo.findChoiceByChoiceNumberAndStudent_StudentIdAndReleaseYear(
                1, student.getStudentId(), Year.now().getValue()).isEmpty());
    }

    @Test
    public void testAlterChoiceClassNotFound() {
        // Given
        setupClasses(aClass, "test", this.tape, this.teacher, this.subject);
        aClass = this.classRepo.findClassByName("test").get();
        AlterStudentChoiceRequest alterStudentChoiceRequest = new AlterStudentChoiceRequest();
        alterStudentChoiceRequest.setChoiceNumber(1);
        alterStudentChoiceRequest.setClassId(aClass.getClassId() + 3);

        // When
        EntityNotFoundException entityNotFoundException = Assert.assertThrows(EntityNotFoundException.class, () ->
                this.studentChoiceService.alterChoice(student.getUser().getUsername(), alterStudentChoiceRequest));

        // Then
        Assertions.assertEquals(entityNotFoundException.getMessage(), ErrorMessage.CLASS_NOT_FOUND);

        Assertions.assertTrue(this.choiceRepo.findChoiceByChoiceNumberAndStudent_StudentIdAndReleaseYear(
                1, student.getStudentId(), Year.now().getValue()).isEmpty());
    }

    @Test
    public void testAlterChoiceInvalidChoiceNumber() {
        // Given
        setupClasses(aClass, "test", this.tape, this.teacher, this.subject);
        aClass = this.classRepo.findClassByName("test").get();
        AlterStudentChoiceRequest alterStudentChoiceRequest = new AlterStudentChoiceRequest();
        alterStudentChoiceRequest.setChoiceNumber(3);
        alterStudentChoiceRequest.setClassId(aClass.getClassId());

        // When
        UnauthorizedException unauthorizedException = Assert.assertThrows(UnauthorizedException.class, () ->
                this.studentChoiceService.alterChoice(student.getUser().getUsername(), alterStudentChoiceRequest));

        // Then
        Assertions.assertEquals(unauthorizedException.getMessage(), ErrorMessage.INVALID_CHOICE_NUMBER);

        Assertions.assertTrue(this.choiceRepo.findChoiceByChoiceNumberAndStudent_StudentIdAndReleaseYear(
                1, student.getStudentId(), Year.now().getValue()).isEmpty());
    }

    @Test
    public void testGetForChoiceTapes() throws EntityNotFoundException {
        // Given
        setupClasses(aClass, "test", this.tape, this.teacher, this.subject);
        aClass = this.classRepo.findClassByName("test").get();

        // When
        List<TapeClassResponse> tapeClassResponses = this.studentChoiceService.getTapesForChoice(student.getUser().getUsername());

        // Then
        Assertions.assertEquals(tapeClassResponses.size(), 2);

        Assertions.assertEquals(tapeClassResponses.get(0).getTapeId(), tape.getTapeId());
        Assertions.assertEquals(tapeClassResponses.get(0).getClassResponses().size(), 1);
        Assertions.assertEquals(tapeClassResponses.get(0).getClassResponses().get(0).getClassId(), aClass.getClassId());

        Assertions.assertEquals(tapeClassResponses.get(1).getTapeId(), otherTape.getTapeId());
        Assertions.assertTrue(tapeClassResponses.get(1).getClassResponses().isEmpty());
    }

    @Test
    public void testGetTapesForChoiceStudentNotFound() {
        // Given
        setupClasses(aClass, "test", this.tape, this.teacher, this.subject);
        aClass = this.classRepo.findClassByName("test").get();

        // When
        EntityNotFoundException entityNotFoundException = Assert.assertThrows(EntityNotFoundException.class, () ->
                this.studentChoiceService.getTapesForChoice("wrong"));

        // Then
        Assertions.assertEquals(entityNotFoundException.getMessage(), ErrorMessage.STUDENT_NOT_FOUND);
    }

    @Test
    public void testGetTapesOfSubjects() throws EntityNotFoundException {
        // Given
        setupClasses(aClass, "test", this.tape, this.teacher, this.subject);
        aClass = this.classRepo.findClassByName("test").get();

        // When
        List<SubjectTapeResponse> subjectTapeResponses = this.studentChoiceService.getTapesOfSubjects(
                student.getUser().getUsername());

        // Then
        Assertions.assertEquals(subjectTapeResponses.size(), 2);

        Assertions.assertEquals(subjectTapeResponses.get(0).getName(), subject.getName());
        Assertions.assertEquals(subjectTapeResponses.get(0).getTapeResponses().size(), 1);
        Assertions.assertEquals(subjectTapeResponses.get(0).getTapeResponses().get(0).getName(), tape.getName());

        Assertions.assertEquals(subjectTapeResponses.get(1).getName(), subjectOther.getName());
        Assertions.assertTrue(subjectTapeResponses.get(1).getTapeResponses().isEmpty());
    }

    @Test
    public void testGetTapesOfSubjectsStudentNotFound() {
        // Given
        setupClasses(aClass, "test", this.tape, this.teacher, this.subject);
        aClass = this.classRepo.findClassByName("test").get();

        // When
        EntityNotFoundException entityNotFoundException = Assert.assertThrows(EntityNotFoundException.class, () ->
                this.studentChoiceService.getTapesOfSubjects("wrong"));

        // Then
        Assertions.assertEquals(entityNotFoundException.getMessage(), ErrorMessage.STUDENT_NOT_FOUND);
    }

    @Test
    public void testGetChoice() throws EntityNotFoundException {
        // Given
        setupClasses(aClass, "test", this.tape, this.teacher, this.subject);
        aClass = this.classRepo.findClassByName("test").get();
        setupChoice(aClass);

        // When
        ChoiceResponse response = this.studentChoiceService.getChoice(student.getUser().getUsername(), 1);

        // Then
        Assertions.assertEquals(response.getChoiceNumber(), 1);
        Assertions.assertEquals(response.getClassChoiceResponses().size(), 1);
        Assertions.assertEquals(response.getClassChoiceResponses().get(0).getClassId(), aClass.getClassId());
        Assertions.assertEquals(response.getClassChoiceResponses().get(0).getTapeId(), aClass.getTape().getTapeId());
    }

    @Test
    public void testGetChoiceStudentNotFound() {
        // Given
        setupClasses(aClass, "test", this.tape, this.teacher, this.subject);
        aClass = this.classRepo.findClassByName("test").get();
        setupChoice(aClass);

        // When
        EntityNotFoundException entityNotFoundException = Assert.assertThrows(EntityNotFoundException.class, () ->
                this.studentChoiceService.getChoice("wrong", 1));

        // Then
        Assertions.assertEquals(entityNotFoundException.getMessage(), ErrorMessage.STUDENT_NOT_FOUND);
    }

    @Test
    public void testGetChoices() throws EntityNotFoundException {
        // Given
        setupClasses(aClass, "test", this.tape, this.teacher, this.subject);
        aClass = this.classRepo.findClassByName("test").get();
        setupChoice(aClass);
        aClass = this.classRepo.findClassByName("test").get();
        student = this.studentRepo.findStudentByStudentId(student.getStudentId()).get();
        setupSecondChoice(aClass);

        // When
        List<ChoiceResponse> responses = this.studentChoiceService.getChoices(student.getUser().getUsername());

        // Then
        Assertions.assertEquals(responses.size(), 2);

        Assertions.assertEquals(responses.get(0).getChoiceNumber(), 1);
        Assertions.assertEquals(responses.get(0).getClassChoiceResponses().size(), 1);
        Assertions.assertEquals(responses.get(0).getClassChoiceResponses().get(0).getClassId(), aClass.getClassId());

        Assertions.assertEquals(responses.get(1).getChoiceNumber(), 2);
        Assertions.assertEquals(responses.get(1).getClassChoiceResponses().size(), 1);
        Assertions.assertEquals(responses.get(1).getClassChoiceResponses().get(0).getClassId(), aClass.getClassId());
    }

    @Test
    public void testGetChoicesStudentNotFound() {
        // Given
        setupClasses(aClass, "test", this.tape, this.teacher, this.subject);
        aClass = this.classRepo.findClassByName("test").get();
        setupChoice(aClass);
        aClass = this.classRepo.findClassByName("test").get();
        student = this.studentRepo.findStudentByStudentId(student.getStudentId()).get();
        setupSecondChoice(aClass);

        // When
        EntityNotFoundException entityNotFoundException = Assert.assertThrows(EntityNotFoundException.class, () ->
                this.studentChoiceService.getChoices("wrong"));

        // Then
        Assertions.assertEquals(entityNotFoundException.getMessage(), ErrorMessage.STUDENT_NOT_FOUND);
    }

    @Test
    public void testGetChoicesNoChoices() {
        // When
        EntityNotFoundException entityNotFoundException = Assert.assertThrows(EntityNotFoundException.class, () ->
                this.studentChoiceService.getChoices(student.getUser().getUsername()));

        // Then
        Assertions.assertEquals(entityNotFoundException.getMessage(), ErrorMessage.NOT_ENOUGH_CHOICES);
    }

    @Test
    public void testGetChoicesOneChoice() {
        // Given
        setupClasses(aClass, "test", this.tape, this.teacher, this.subject);
        aClass = this.classRepo.findClassByName("test").get();
        setupChoice(aClass);

        // When
        EntityNotFoundException entityNotFoundException = Assert.assertThrows(EntityNotFoundException.class, () ->
                this.studentChoiceService.getChoices(student.getUser().getUsername()));

        // Then
        Assertions.assertEquals(entityNotFoundException.getMessage(), ErrorMessage.NOT_ENOUGH_CHOICES);
    }

    @Test
    public void testDeleteClassFromChoice() throws EntityNotFoundException {
        // Given
        setupClasses(aClass, "test", this.tape, this.teacher, this.subject);
        aClass = this.classRepo.findClassByName("test").get();
        setupChoice(aClass);
        choice = this.choiceRepo.findChoiceByChoiceNumberAndStudent_StudentIdAndReleaseYear(1,
                student.getStudentId(), Year.now().getValue()).get();

        DeleteClassFromChoiceRequest request = new DeleteClassFromChoiceRequest();
        request.setChoiceId(choice.getChoiceId());
        request.setClassId(aClass.getClassId());

        // When
        ChoiceResponse response = this.studentChoiceService.deleteClassFromChoice(request);

        // Then
        Assertions.assertEquals(response.getChoiceId(), choice.getChoiceId());

        choice = this.choiceRepo.findChoiceByChoiceId(choice.getChoiceId()).get();
        Assertions.assertTrue(choice.getChoiceClasses().isEmpty());

        aClass = this.classRepo.findClassByClassId(aClass.getClassId()).get();
        Assertions.assertTrue(aClass.getChoiceClasses().isEmpty());
    }

    @Test
    public void testDeleteClassFromChoiceChoiceNotFound() {
        // Given
        setupClasses(aClass, "test", this.tape, this.teacher, this.subject);
        aClass = this.classRepo.findClassByName("test").get();
        setupChoice(aClass);
        choice = this.choiceRepo.findChoiceByChoiceNumberAndStudent_StudentIdAndReleaseYear(1,
                student.getStudentId(), Year.now().getValue()).get();

        DeleteClassFromChoiceRequest request = new DeleteClassFromChoiceRequest();
        request.setChoiceId(choice.getChoiceId() + 3);
        request.setClassId(aClass.getClassId());

        // When
        EntityNotFoundException entityNotFoundException = Assert.assertThrows(EntityNotFoundException.class, () ->
                this.studentChoiceService.deleteClassFromChoice(request));

        // Then
        Assertions.assertEquals(entityNotFoundException.getMessage(), ErrorMessage.CHOICE_NOT_FOUND);

        choice = this.choiceRepo.findChoiceByChoiceId(choice.getChoiceId()).get();
        Assertions.assertEquals(choice.getChoiceClasses().size(), 1);
        Assertions.assertEquals(choice.getChoiceClasses().stream().findFirst().get().getaClass().getClassId(), aClass.getClassId());

        aClass = this.classRepo.findClassByClassId(aClass.getClassId()).get();
        Assertions.assertEquals(aClass.getChoiceClasses().stream().findFirst().get().getChoice().getChoiceId(), choice.getChoiceId());
    }

    @Test
    public void testDeleteClassFromChoiceClassNotFound() {
        // Given
        setupClasses(aClass, "test", this.tape, this.teacher, this.subject);
        aClass = this.classRepo.findClassByName("test").get();
        setupChoice(aClass);
        choice = this.choiceRepo.findChoiceByChoiceNumberAndStudent_StudentIdAndReleaseYear(1,
                student.getStudentId(), Year.now().getValue()).get();

        DeleteClassFromChoiceRequest request = new DeleteClassFromChoiceRequest();
        request.setChoiceId(choice.getChoiceId());
        request.setClassId(aClass.getClassId() + 3);

        // When
        EntityNotFoundException entityNotFoundException = Assert.assertThrows(EntityNotFoundException.class, () ->
                this.studentChoiceService.deleteClassFromChoice(request));

        // Then
        Assertions.assertEquals(entityNotFoundException.getMessage(), ErrorMessage.CLASS_NOT_IN_CHOICE);

        choice = this.choiceRepo.findChoiceByChoiceId(choice.getChoiceId()).get();
        Assertions.assertEquals(choice.getChoiceClasses().size(), 1);
        Assertions.assertEquals(choice.getChoiceClasses().stream().findFirst().get().getaClass().getClassId(), aClass.getClassId());

        aClass = this.classRepo.findClassByClassId(aClass.getClassId()).get();
        Assertions.assertEquals(aClass.getChoiceClasses().stream().findFirst().get().getChoice().getChoiceId(), choice.getChoiceId());
    }
}
