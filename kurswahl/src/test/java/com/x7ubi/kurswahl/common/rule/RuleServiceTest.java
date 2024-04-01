package com.x7ubi.kurswahl.common.rule;

import com.x7ubi.kurswahl.KurswahlServiceTest;
import com.x7ubi.kurswahl.common.models.Class;
import com.x7ubi.kurswahl.common.models.*;
import com.x7ubi.kurswahl.common.repository.*;
import com.x7ubi.kurswahl.common.rule.response.RuleResponse;
import com.x7ubi.kurswahl.common.rule.service.RuleService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Year;
import java.util.HashSet;
import java.util.List;

@KurswahlServiceTest
public class RuleServiceTest {
    @Autowired
    private RuleService ruleService;

    @Autowired
    private RuleSetRepo ruleSetRepo;

    @Autowired
    private RuleRepo ruleRepo;

    @Autowired
    private SubjectRepo subjectRepo;

    @Autowired
    private SubjectAreaRepo subjectAreaRepo;

    @Autowired
    private SubjetRuleRepo subjetRuleRepo;

    private Student student;

    private Tape tape;

    private Tape otherTape;

    private Subject subject;

    private Subject subjectOther;

    private Teacher teacher;

    private Class aClass;

    private Class bClass;

    private Choice choice;

    private RuleSet ruleSet;

    private Rule rule;

    @BeforeEach
    public void setupTest() {
        setupTapes();
        setupSubjects();
        setupStudent();
    }

    private void setupStudent() {
        User user = new User();
        user.setUsername("test.student");
        user.setFirstname("Test");
        user.setSurname("User");
        user.setPassword("Password");
        student = new Student();
        student.setUser(user);
        student.setChoices(new HashSet<>());
    }

    private void setupChoice() {

        choice = new Choice();
        choice.setChoiceNumber(1);
        choice.setReleaseYear(Year.now().getValue());
        choice.setChoiceClasses(new HashSet<>());
        choice.setStudent(student);
    }

    private void addClassToChoice(Class c) {
        ChoiceClass choiceClass = new ChoiceClass();
        choiceClass.setChoice(choice);
        choiceClass.setaClass(c);

        choice.getChoiceClasses().add(choiceClass);

        c.getChoiceClasses().add(choiceClass);

        student.getChoices().add(choice);
    }

    private void setupTapes() {
        tape = new Tape();
        tape.setName("LK 1");
        tape.setYear(11);
        tape.setReleaseYear(Year.now().getValue());
        tape.setLk(true);
        tape.setaClass(new HashSet<>());

        otherTape = new Tape();
        otherTape.setName("LK 2");
        otherTape.setYear(11);
        otherTape.setReleaseYear(Year.now().getValue());
        otherTape.setLk(true);
        otherTape.setaClass(new HashSet<>());
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
        subject = this.subjectRepo.findSubjectByName("test").get();
        subjectArea.getSubjects().add(subject);

        subjectOther = new Subject();
        subjectOther.setName("test other");
        subjectOther.setSubjectArea(subjectArea);
        this.subjectRepo.save(subjectOther);
        subjectOther = this.subjectRepo.findSubjectByName("test other").get();
        subjectArea.getSubjects().add(subjectOther);
        this.subjectAreaRepo.save(subjectArea);
    }

    private Class setupClass(String name, Tape tape, Subject subject) {
        Class c = new Class();
        c.setName(name);
        c.setTape(tape);
        c.setSubject(subject);
        c.setTeacher(teacher);
        c.setChoiceClasses(new HashSet<>());

        subject.setClasses(new HashSet<>());
        subject.getClasses().add(c);
        tape.getaClass().add(c);

        return c;
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
        SubjectRule subjectRule = new SubjectRule();
        subjectRule.setSubject(subject);
        subjectRule.setRule(rule);

        subjectRule = this.subjetRuleRepo.save(subjectRule);

        rule.getSubjectRules().add(subjectRule);
        ruleRepo.save(rule);
    }

    @Test
    public void testGetAllRules() {
        // Given
        setupRuleSet();
        setupRule();
        addSubjectToRule();

        // When
        List<RuleResponse> ruleResponses = this.ruleService.getAllRules(11);

        // Then
        Assertions.assertEquals(ruleResponses.size(), 1);
        Assertions.assertEquals(ruleResponses.get(0).getName(), rule.getName());
        Assertions.assertEquals(ruleResponses.get(0).getSubjectResponses().size(), 1);
        Assertions.assertEquals(ruleResponses.get(0).getSubjectResponses().get(0).getName(), subject.getName());
    }

    @Test
    public void testGetAllRulesNoRuleSet() {
        // When
        List<RuleResponse> ruleResponses = this.ruleService.getAllRules(11);

        // Then
        Assertions.assertTrue(ruleResponses.isEmpty());
    }

    @Test
    public void testGetRulesByChoice() {
        // Given
        setupRuleSet();
        setupRule();
        addSubjectToRule();
        this.aClass = setupClass("test", this.tape, this.subjectOther);
        this.bClass = setupClass("test other", this.tape, this.subjectOther);
        setupChoice();
        addClassToChoice(this.aClass);
        addClassToChoice(this.bClass);

        // When
        List<RuleResponse> ruleResponses = this.ruleService.getRulesByChoiceClasses(11, choice.getChoiceClasses());

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
        this.aClass = setupClass("test", this.tape, this.subject);
        this.bClass = setupClass("test other", this.tape, this.subject);
        setupChoice();
        addClassToChoice(this.aClass);
        addClassToChoice(this.bClass);

        // When
        List<RuleResponse> ruleResponses = this.ruleService.getRulesByChoiceClasses(11, choice.getChoiceClasses());

        // Then
        Assertions.assertTrue(ruleResponses.isEmpty());
    }

    @Test
    public void testGetRulesByChoiceNoRuleSet() {
        // Given
        this.aClass = setupClass("test", this.tape, this.subjectOther);
        this.bClass = setupClass("test other", this.tape, this.subjectOther);
        setupChoice();
        addClassToChoice(this.aClass);
        addClassToChoice(this.bClass);

        // When
        List<RuleResponse> ruleResponses = this.ruleService.getRulesByChoiceClasses(11, choice.getChoiceClasses());

        // Then
        Assertions.assertTrue(ruleResponses.isEmpty());
    }

    @Test
    public void testGetRulesByChoiceRuleNumLKsNotFulfilled() {
        // Given
        setupRuleSet();
        setupRule();
        addSubjectToRule();
        this.aClass = setupClass("test", this.tape, this.subject);
        setupChoice();
        addClassToChoice(this.aClass);

        // When
        List<RuleResponse> ruleResponses = this.ruleService.getRulesByChoiceClasses(11, choice.getChoiceClasses());

        // Then
        Assertions.assertEquals(ruleResponses.size(), 1);
        Assertions.assertEquals(ruleResponses.get(0).getName(), RuleService.LK_NOT_FULFILLED_RULE);
        Assertions.assertTrue(ruleResponses.get(0).getSubjectResponses().isEmpty());
    }
}
