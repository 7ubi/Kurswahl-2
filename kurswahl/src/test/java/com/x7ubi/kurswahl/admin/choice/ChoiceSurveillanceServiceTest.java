package com.x7ubi.kurswahl.admin.choice;

import com.x7ubi.kurswahl.KurswahlServiceTest;
import com.x7ubi.kurswahl.admin.choice.response.ChoiceSurveillanceResponse;
import com.x7ubi.kurswahl.admin.choice.service.ChoiceSurveillanceService;
import com.x7ubi.kurswahl.common.models.Class;
import com.x7ubi.kurswahl.common.models.*;
import com.x7ubi.kurswahl.common.repository.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Year;
import java.util.HashSet;
import java.util.List;

@KurswahlServiceTest
public class ChoiceSurveillanceServiceTest {
    @Autowired
    private ChoiceSurveillanceService choiceSurveillanceService;

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

    private Subject subject;

    private Teacher teacher;

    private Class aClass;

    private Class bClass;

    private Choice choice;

    private Choice secondChoice;

    private StudentClass studentClass;

    private RuleSet ruleSet;

    private Rule rule;

    private Rule otherRule;

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

    private void setupChoice(Choice choice, Integer choiceNumber, Class c, Class bc) {

        choice = new Choice();
        choice.setChoiceNumber(choiceNumber);
        choice.setReleaseYear(Year.now().getValue());
        choice.setChoiceClasses(new HashSet<>());
        choice.setStudent(student);

        this.choiceRepo.save(choice);

        choice = addClassToChoice(choice, choiceNumber, c);
        choice = addClassToChoice(choice, choiceNumber, bc);

        student.getChoices().add(choice);
        this.studentRepo.save(student);
    }

    private Choice addClassToChoice(Choice choice, Integer choiceNumber, Class c) {
        if (c != null) {
            ChoiceClass choiceClass = new ChoiceClass();
            choiceClass.setChoice(choice);
            choiceClass.setaClass(c);
            this.choiceClassRepo.save(choiceClass);

            choice = this.choiceRepo.findChoiceByChoiceNumberAndStudent_StudentIdAndReleaseYear(choiceNumber,
                    student.getStudentId(), Year.now().getValue()).get();

            choice.getChoiceClasses().add(choiceClass);
            this.choiceRepo.save(choice);

            c.getChoiceClasses().add(choiceClass);
            this.classRepo.save(c);
        }
        return choice;
    }

    private void setupTapes() {
        tape = new Tape();
        tape.setName("GK 1");
        tape.setYear(11);
        tape.setReleaseYear(Year.now().getValue());
        tape.setLk(true);
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

    private void setupRuleSet() {
        ruleSet = new RuleSet();
        ruleSet.setYear(11);

        ruleSetRepo.save(ruleSet);
        ruleSet = ruleSetRepo.findRuleSetByYear(11).get();
    }

    private void setupRule() {
        rule = new Rule();
        rule.setRuleSet(ruleSet);
        rule.setName("Rule");

        ruleRepo.save(rule);
        rule = ruleRepo.findRuleByNameAndRuleSet_Year("Rule", 11).get();
        ruleSet.getRules().add(rule);
        ruleSetRepo.save(ruleSet);
        ruleSet = ruleSetRepo.findRuleSetByYear(11).get();
    }

    private void addSubjectToRule() {
        rule.getSubjects().add(subject);
        ruleRepo.save(rule);
    }

    @Test
    public void testGetChoiceSurveillanceForStudents() {
        // Given
        setupClasses(aClass, "test", tape, teacher, subject);
        setupClasses(bClass, "test other", tape, teacher, subject);
        setupRuleSet();
        setupRule();
        addSubjectToRule();
        this.aClass = this.classRepo.findClassByName("test").get();
        this.bClass = this.classRepo.findClassByName("test other").get();
        this.student = this.studentRepo.findStudentByUser_Username("test.student").get();
        setupChoice(choice, 1, aClass, bClass);
        this.aClass = this.classRepo.findClassByName("test").get();
        this.bClass = this.classRepo.findClassByName("test other").get();
        this.student = this.studentRepo.findStudentByUser_Username("test.student").get();
        setupChoice(secondChoice, 2, aClass, bClass);

        // When
        List<ChoiceSurveillanceResponse> responses = this.choiceSurveillanceService.getChoiceSurveillanceForStudents();

        // Then
        Assertions.assertEquals(responses.size(), 1);
        Assertions.assertEquals(responses.get(0).getStudentSurveillanceResponse().getStudentClassId(),
                studentClass.getStudentClassId());
        Assertions.assertEquals(responses.get(0).getStudentSurveillanceResponse().getName(), studentClass.getName());
        Assertions.assertEquals(responses.get(0).getStudentSurveillanceResponse().getUsername(),
                student.getUser().getUsername());
        Assertions.assertTrue(responses.get(0).getChosen());
        Assertions.assertTrue(responses.get(0).getFulfilledRules());
    }

    @Test
    public void testGetChoiceSurveillanceNoRuleSet() {
        // Given
        setupClasses(aClass, "test", tape, teacher, subject);
        setupClasses(bClass, "test other", tape, teacher, subject);
        this.aClass = this.classRepo.findClassByName("test").get();
        this.bClass = this.classRepo.findClassByName("test other").get();
        this.student = this.studentRepo.findStudentByUser_Username("test.student").get();
        setupChoice(choice, 1, aClass, bClass);
        this.aClass = this.classRepo.findClassByName("test").get();
        this.bClass = this.classRepo.findClassByName("test other").get();
        this.student = this.studentRepo.findStudentByUser_Username("test.student").get();
        setupChoice(secondChoice, 2, aClass, bClass);

        // When
        List<ChoiceSurveillanceResponse> responses = this.choiceSurveillanceService.getChoiceSurveillanceForStudents();

        // Then
        Assertions.assertEquals(responses.size(), 1);
        Assertions.assertEquals(responses.get(0).getStudentSurveillanceResponse().getStudentClassId(),
                studentClass.getStudentClassId());
        Assertions.assertEquals(responses.get(0).getStudentSurveillanceResponse().getName(), studentClass.getName());
        Assertions.assertEquals(responses.get(0).getStudentSurveillanceResponse().getUsername(),
                student.getUser().getUsername());
        Assertions.assertTrue(responses.get(0).getChosen());
        Assertions.assertTrue(responses.get(0).getFulfilledRules());
    }

    @Test
    public void testGetChoiceSurveillanceRulesNotFulfilled() {
        // Given
        setupClasses(aClass, "test", tape, teacher, subject);
        setupClasses(bClass, "test other", tape, teacher, subject);
        setupRuleSet();
        setupRule();
        addSubjectToRule();
        this.aClass = this.classRepo.findClassByName("test").get();
        this.bClass = this.classRepo.findClassByName("test other").get();
        this.student = this.studentRepo.findStudentByUser_Username("test.student").get();
        setupChoice(choice, 1, aClass, bClass);
        this.student = this.studentRepo.findStudentByUser_Username("test.student").get();
        setupChoice(secondChoice, 2, null, null);

        // When
        List<ChoiceSurveillanceResponse> responses = this.choiceSurveillanceService.getChoiceSurveillanceForStudents();

        // Then
        Assertions.assertEquals(responses.size(), 1);
        Assertions.assertEquals(responses.get(0).getStudentSurveillanceResponse().getStudentClassId(),
                studentClass.getStudentClassId());
        Assertions.assertEquals(responses.get(0).getStudentSurveillanceResponse().getName(), studentClass.getName());
        Assertions.assertEquals(responses.get(0).getStudentSurveillanceResponse().getUsername(),
                student.getUser().getUsername());
        Assertions.assertTrue(responses.get(0).getChosen());
        Assertions.assertFalse(responses.get(0).getFulfilledRules());
    }

    @Test
    public void testGetChoiceSurveillanceRulesNotFulfilledBothChoices() {
        // Given
        setupClasses(aClass, "test", tape, teacher, subject);
        setupRuleSet();
        setupRule();
        addSubjectToRule();
        this.student = this.studentRepo.findStudentByUser_Username("test.student").get();
        setupChoice(choice, 1, null, null);
        this.student = this.studentRepo.findStudentByUser_Username("test.student").get();
        setupChoice(choice, 2, null, null);

        // When
        List<ChoiceSurveillanceResponse> responses = this.choiceSurveillanceService.getChoiceSurveillanceForStudents();

        // Then
        Assertions.assertEquals(responses.size(), 1);
        Assertions.assertEquals(responses.get(0).getStudentSurveillanceResponse().getStudentClassId(),
                studentClass.getStudentClassId());
        Assertions.assertEquals(responses.get(0).getStudentSurveillanceResponse().getName(), studentClass.getName());
        Assertions.assertEquals(responses.get(0).getStudentSurveillanceResponse().getUsername(),
                student.getUser().getUsername());
        Assertions.assertTrue(responses.get(0).getChosen());
        Assertions.assertFalse(responses.get(0).getFulfilledRules());
    }

    @Test
    public void testGetChoiceSurveillanceNotChosen() {
        // Given
        setupClasses(aClass, "test", tape, teacher, subject);
        setupClasses(bClass, "test other", tape, teacher, subject);
        setupRuleSet();
        setupRule();
        addSubjectToRule();
        this.aClass = this.classRepo.findClassByName("test").get();
        this.bClass = this.classRepo.findClassByName("test other").get();
        this.student = this.studentRepo.findStudentByUser_Username("test.student").get();
        setupChoice(choice, 1, aClass, bClass);

        // When
        List<ChoiceSurveillanceResponse> responses = this.choiceSurveillanceService.getChoiceSurveillanceForStudents();

        // Then
        Assertions.assertEquals(responses.size(), 1);
        Assertions.assertEquals(responses.get(0).getStudentSurveillanceResponse().getStudentClassId(),
                studentClass.getStudentClassId());
        Assertions.assertEquals(responses.get(0).getStudentSurveillanceResponse().getName(), studentClass.getName());
        Assertions.assertEquals(responses.get(0).getStudentSurveillanceResponse().getUsername(),
                student.getUser().getUsername());
        Assertions.assertFalse(responses.get(0).getChosen());
        Assertions.assertFalse(responses.get(0).getFulfilledRules());
    }

    @Test
    public void testGetChoiceSurveillanceNotChosenBothChoices() {
        // Given
        setupClasses(aClass, "test", tape, teacher, subject);
        setupRuleSet();
        setupRule();
        addSubjectToRule();

        // When
        List<ChoiceSurveillanceResponse> responses = this.choiceSurveillanceService.getChoiceSurveillanceForStudents();

        // Then
        Assertions.assertEquals(responses.size(), 1);
        Assertions.assertEquals(responses.get(0).getStudentSurveillanceResponse().getStudentClassId(),
                studentClass.getStudentClassId());
        Assertions.assertEquals(responses.get(0).getStudentSurveillanceResponse().getName(), studentClass.getName());
        Assertions.assertEquals(responses.get(0).getStudentSurveillanceResponse().getUsername(),
                student.getUser().getUsername());
        Assertions.assertFalse(responses.get(0).getChosen());
        Assertions.assertFalse(responses.get(0).getFulfilledRules());
    }
}
