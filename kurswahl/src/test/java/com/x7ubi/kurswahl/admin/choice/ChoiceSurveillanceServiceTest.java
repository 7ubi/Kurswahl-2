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

    private RuleSet otherRuleSet;

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

    private void setupSecondChoice(Class c) {
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

    private void setupOtherRule() {
        otherRuleSet = new RuleSet();
        otherRuleSet.setYear(12);

        ruleSetRepo.save(otherRuleSet);
        otherRuleSet = ruleSetRepo.findRuleSetByYear(12).get();

        otherRule = new Rule();
        otherRule.setRuleSet(otherRuleSet);
        otherRule.setName("Other Rule");

        ruleRepo.save(otherRule);
        otherRule = ruleRepo.findRuleByNameAndRuleSet_Year("Other Rule", 12).get();
        ruleSet.getRules().add(otherRule);
        ruleSetRepo.save(otherRuleSet);
        otherRuleSet = ruleSetRepo.findRuleSetByYear(12).get();
    }

    @Test
    public void testGetChoiceSurveillanceForStudents() {
        // Given
        setupClasses(aClass, "test", tape, teacher, subject);
        setupRuleSet();
        setupRule();
        addSubjectToRule();
        this.aClass = this.classRepo.findClassByName("test").get();
        this.student = this.studentRepo.findStudentByUser_Username("test.student").get();
        setupChoice(aClass);
        this.aClass = this.classRepo.findClassByName("test").get();
        this.student = this.studentRepo.findStudentByUser_Username("test.student").get();
        setupSecondChoice(aClass);

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
        this.aClass = this.classRepo.findClassByName("test").get();
        this.student = this.studentRepo.findStudentByUser_Username("test.student").get();
        setupChoice(aClass);
        this.aClass = this.classRepo.findClassByName("test").get();
        this.student = this.studentRepo.findStudentByUser_Username("test.student").get();
        setupSecondChoice(aClass);

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
        setupRuleSet();
        setupRule();
        addSubjectToRule();
        this.aClass = this.classRepo.findClassByName("test").get();
        this.student = this.studentRepo.findStudentByUser_Username("test.student").get();
        setupChoice(aClass);
        this.student = this.studentRepo.findStudentByUser_Username("test.student").get();
        setupSecondChoice(null);

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
        setupChoice(null);
        this.student = this.studentRepo.findStudentByUser_Username("test.student").get();
        setupSecondChoice(null);

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
        setupRuleSet();
        setupRule();
        addSubjectToRule();
        this.aClass = this.classRepo.findClassByName("test").get();
        this.student = this.studentRepo.findStudentByUser_Username("test.student").get();
        setupChoice(aClass);

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
