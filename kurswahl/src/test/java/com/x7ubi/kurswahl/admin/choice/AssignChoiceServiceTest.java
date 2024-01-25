package com.x7ubi.kurswahl.admin.choice;

import com.x7ubi.kurswahl.KurswahlServiceTest;
import com.x7ubi.kurswahl.admin.choice.response.ChoiceResponse;
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

    private Student student;

    private Tape tape;

    private Subject subject;

    private Teacher teacher;

    private com.x7ubi.kurswahl.common.models.Class aClass;

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
        choice.setClasses(new HashSet<>());
        choice.getClasses().add(c);
        choice.setStudent(student);

        this.choiceRepo.save(choice);

        if (c != null) {
            c.getChoices().add(choice);
            this.classRepo.save(c);
        }

        student.getChoices().add(choice);
        this.studentRepo.save(student);
    }

    private void setupSecondChoice(com.x7ubi.kurswahl.common.models.Class c) {
        secondChoice = new Choice();
        secondChoice.setChoiceNumber(2);
        secondChoice.setReleaseYear(Year.now().getValue());
        secondChoice.setClasses(new HashSet<>());
        secondChoice.getClasses().add(c);
        secondChoice.setStudent(student);

        this.choiceRepo.save(secondChoice);

        secondChoice = this.choiceRepo.findChoiceByChoiceNumberAndStudent_StudentIdAndReleaseYear(2,
                student.getStudentId(), Year.now().getValue()).get();

        if (c != null) {
            c.getChoices().add(secondChoice);
            this.classRepo.save(c);
        }

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

        tapeRepo.save(tape);
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
        setupChoice(aClass);

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
        setupChoice(aClass);
        this.aClass = this.classRepo.findClassByName("test").get();
        this.student = this.studentRepo.findStudentByUser_Username("test.student").get();
        setupSecondChoice(aClass);

        // When
        StudentChoicesResponse studentChoicesResponse = this.assignChoiceService.getStundetChoices(student.getStudentId());

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
    public void testGetStundetChoicesStudentNotFound() throws EntityNotFoundException {
        // Given
        setupClasses(aClass, "test", tape, teacher, subject);
        this.aClass = this.classRepo.findClassByName("test").get();
        setupChoice(aClass);
        this.aClass = this.classRepo.findClassByName("test").get();
        this.student = this.studentRepo.findStudentByUser_Username("test.student").get();
        setupSecondChoice(aClass);

        // When
        EntityNotFoundException entityNotFoundException = Assert.assertThrows(EntityNotFoundException.class, () ->
                this.assignChoiceService.getStundetChoices(student.getStudentId() + 3));

        // Then
        Assertions.assertEquals(entityNotFoundException.getMessage(), ErrorMessage.STUDENT_NOT_FOUND);
    }
}
