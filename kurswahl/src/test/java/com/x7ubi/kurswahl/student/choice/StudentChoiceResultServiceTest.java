package com.x7ubi.kurswahl.student.choice;

import com.x7ubi.kurswahl.KurswahlServiceTest;
import com.x7ubi.kurswahl.common.error.ErrorMessage;
import com.x7ubi.kurswahl.common.exception.DisabledException;
import com.x7ubi.kurswahl.common.exception.EntityNotFoundException;
import com.x7ubi.kurswahl.common.models.Class;
import com.x7ubi.kurswahl.common.models.*;
import com.x7ubi.kurswahl.common.repository.*;
import com.x7ubi.kurswahl.student.choice.response.ChoiceResponse;
import com.x7ubi.kurswahl.student.choice.service.StudentChoiceResultService;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Year;
import java.util.HashSet;

@KurswahlServiceTest
public class StudentChoiceResultServiceTest {

    @Autowired
    private StudentChoiceResultService studentChoiceResultService;

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

    private Subject subject;

    private Teacher teacher;

    private com.x7ubi.kurswahl.common.models.Class aClass;

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
        studentClass.setStudents(new HashSet<>());

        studentClass = this.studentClassRepo.save(studentClass);

        teacher.setStudentClasses(new HashSet<>());
        teacher.getStudentClasses().add(studentClass);
        this.teacherRepo.save(teacher);
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

    private void setupChoice(com.x7ubi.kurswahl.common.models.Class c, boolean selected) {

        Choice choice = new Choice();
        choice.setChoiceNumber(1);
        choice.setReleaseYear(Year.now().getValue());
        choice.setChoiceClasses(new HashSet<>());
        choice.setStudent(student);

        choice = this.choiceRepo.save(choice);

        ChoiceClass choiceClass = new ChoiceClass();
        choiceClass.setChoice(choice);
        choiceClass.setaClass(c);
        choiceClass.setSelected(selected);
        this.choiceClassRepo.save(choiceClass);

        choice.getChoiceClasses().add(choiceClass);
        this.choiceRepo.save(choice);

        c.getChoiceClasses().add(choiceClass);
        this.classRepo.save(c);

        student.getChoices().add(choice);
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
    }

    public void setupSubjects() {
        SubjectArea subjectArea = new SubjectArea();
        subjectArea.setName("Subject Area");
        subjectArea.setSubjects(new HashSet<>());

        subjectArea = this.subjectAreaRepo.save(subjectArea);

        subject = new Subject();
        subject.setName("test");
        subject.setSubjectArea(subjectArea);
        subject = this.subjectRepo.save(subject);
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
        teacher.setClasses(new HashSet<>());

        teacher = this.teacherRepo.save(teacher);
    }

    private void setupClasses() {
        aClass = new Class();
        aClass.setName("test");
        aClass.setTape(tape);
        aClass.setSubject(subject);
        aClass.setTeacher(teacher);
        aClass.setChoiceClasses(new HashSet<>());
        aClass = this.classRepo.save(aClass);

        teacher.getClasses().add(aClass);
        this.teacherRepo.save(teacher);

        subject.setClasses(new HashSet<>());
        subject.getClasses().add(aClass);
        this.subjectRepo.save(subject);

        tape.getaClass().add(aClass);
        tapeRepo.save(tape);
    }

    @Test
    public void testGetChoiceResult() throws EntityNotFoundException, DisabledException {
        // Given
        setupClasses();
        setupChoice(aClass, true);

        // When
        ChoiceResponse response = this.studentChoiceResultService.getStudentChoiceResult(student.getUser().getUsername());

        // Then
        Assertions.assertEquals(response.getClassChoiceResponses().size(), 1);
        Assertions.assertEquals(response.getClassChoiceResponses().get(0).getClassId(), aClass.getClassId());
        Assertions.assertEquals(response.getClassChoiceResponses().get(0).getTapeId(), aClass.getTape().getTapeId());
    }

    @Test
    public void testGetChoiceResultNotSelected() throws EntityNotFoundException, DisabledException {
        // Given
        setupClasses();
        setupChoice(aClass, false);

        // When
        ChoiceResponse response = this.studentChoiceResultService.getStudentChoiceResult(student.getUser().getUsername());

        // Then
        Assertions.assertTrue(response.getClassChoiceResponses().isEmpty());
    }

    @Test
    public void testGetChoiceResultStudentNotFound() {
        // Given
        setupClasses();
        setupChoice(aClass, true);

        // When
        EntityNotFoundException entityNotFoundException = Assert.assertThrows(EntityNotFoundException.class, () ->
                this.studentChoiceResultService.getStudentChoiceResult("wrong"));

        // Then
        Assertions.assertEquals(entityNotFoundException.getMessage(), ErrorMessage.STUDENT_NOT_FOUND);
    }
}
