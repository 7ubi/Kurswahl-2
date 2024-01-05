package com.x7ubi.kurswahl.student.choice;

import com.x7ubi.kurswahl.KurswahlServiceTest;
import com.x7ubi.kurswahl.common.models.Class;
import com.x7ubi.kurswahl.common.models.*;
import com.x7ubi.kurswahl.common.repository.*;
import com.x7ubi.kurswahl.student.choice.response.RuleResponse;
import com.x7ubi.kurswahl.student.choice.service.StudentRuleService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Year;
import java.util.HashSet;
import java.util.List;

@KurswahlServiceTest
public class StudentRuleServiceTest {
    @Autowired
    private StudentRuleService studentRuleService;

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

    private Subject subjectOther;

    private Teacher teacher;

    private Class aClass;

    private Choice choice;

    private StudentClass studentClass;

    private RuleSet ruleSet;

    private Rule rule;

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

        c.getChoices().add(choice);
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

        subjectOther = new Subject();
        subjectOther.setName("test other");
        subjectOther.setSubjectArea(subjectArea);
        this.subjectRepo.save(subjectOther);
        subjectArea.getSubjects().add(subjectOther);
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

    private void setupClasses(Class c, Tape tape, Teacher teacher, Subject subject) {
        c = new Class();
        c.setName("test");
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
    public void testGetAllRules() {
        // Given
        setupRuleSet();
        setupRule();
        addSubjectToRule();

        // When
        List<RuleResponse> ruleResponses = this.studentRuleService.getAllRules(11);

        // Then
        Assertions.assertEquals(ruleResponses.size(), 1);
        Assertions.assertEquals(ruleResponses.get(0).getName(), rule.getName());
        Assertions.assertEquals(ruleResponses.get(0).getSubjectResponses().size(), 1);
        Assertions.assertEquals(ruleResponses.get(0).getSubjectResponses().get(0).getName(), subject.getName());
    }

    @Test
    public void testGetAllRulesNoRuleSet() {
        // When
        List<RuleResponse> ruleResponses = this.studentRuleService.getAllRules(11);

        // Then
        Assertions.assertTrue(ruleResponses.isEmpty());
    }

    @Test
    public void testGetRulesByChoice() {
        // Given
        setupRuleSet();
        setupRule();
        addSubjectToRule();
        setupClasses(aClass, this.tape, this.teacher, this.subjectOther);
        aClass = this.classRepo.findClassByName("test").get();
        setupChoice(this.aClass);

        // When
        List<RuleResponse> ruleResponses = this.studentRuleService.getRulesByChoice(11, choice);

        // Then
        Assertions.assertEquals(ruleResponses.size(), 1);
        Assertions.assertEquals(ruleResponses.get(0).getName(), rule.getName());
        Assertions.assertEquals(ruleResponses.get(0).getSubjectResponses().size(), 1);
        Assertions.assertEquals(ruleResponses.get(0).getSubjectResponses().get(0).getName(), subject.getName());
    }

    @Test
    public void testGetRulesByChoiceRuleFulfilled() {
        // Given
        setupRuleSet();
        setupRule();
        addSubjectToRule();
        setupClasses(aClass, this.tape, this.teacher, this.subject);
        aClass = this.classRepo.findClassByName("test").get();
        setupChoice(this.aClass);

        // When
        List<RuleResponse> ruleResponses = this.studentRuleService.getRulesByChoice(11, choice);

        // Then
        Assertions.assertTrue(ruleResponses.isEmpty());
    }

    @Test
    public void testGetRulesByChoiceNoRuleSet() {
        // Given
        setupClasses(aClass, this.tape, this.teacher, this.subject);
        aClass = this.classRepo.findClassByName("test").get();
        setupChoice(this.aClass);

        // When
        List<RuleResponse> ruleResponses = this.studentRuleService.getRulesByChoice(11, choice);

        // Then
        Assertions.assertTrue(ruleResponses.isEmpty());
    }
}
