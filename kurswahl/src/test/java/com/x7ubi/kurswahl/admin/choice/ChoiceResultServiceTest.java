package com.x7ubi.kurswahl.admin.choice;

import com.x7ubi.kurswahl.KurswahlServiceTest;
import com.x7ubi.kurswahl.admin.choice.response.ChoiceResultResponse;
import com.x7ubi.kurswahl.admin.choice.response.ClassStudentsResponse;
import com.x7ubi.kurswahl.admin.choice.service.ChoiceResultService;
import com.x7ubi.kurswahl.common.models.Class;
import com.x7ubi.kurswahl.common.models.*;
import com.x7ubi.kurswahl.common.repository.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Year;
import java.util.Comparator;
import java.util.HashSet;

@KurswahlServiceTest
public class ChoiceResultServiceTest {
    @Autowired
    private ChoiceResultService choiceResultService;

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

    private StudentClass studentClass;

    private RuleSet ruleSet;

    private Rule rule;

    private Rule otherRule;

    @Autowired
    private SubjetRuleRepo subjetRuleRepo;

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

    private void setupChoice(com.x7ubi.kurswahl.common.models.Class c, Class bc, boolean selected, Integer choiceNumber) {

        Choice choice = new Choice();
        choice.setChoiceNumber(choiceNumber);
        choice.setReleaseYear(Year.now().getValue());
        choice.setChoiceClasses(new HashSet<>());
        choice.setStudent(student);

        choice = this.choiceRepo.save(choice);

        choice = getChoice(c, selected, choiceNumber, choice);
        choice = getChoice(bc, selected, choiceNumber, choice);

        student.getChoices().add(choice);
        student = this.studentRepo.save(student);
    }

    private Choice getChoice(Class c, boolean selected, Integer choiceNumber, Choice choice) {
        if (c != null) {
            ChoiceClass choiceClass = new ChoiceClass();
            choiceClass.setChoice(choice);
            choiceClass.setaClass(c);
            choiceClass.setSelected(selected);
            this.choiceClassRepo.save(choiceClass);

            choice = this.choiceRepo.findChoiceByChoiceNumberAndStudent_StudentIdAndReleaseYear(choiceNumber,
                    student.getStudentId(), Year.now().getValue()).get();

            choice.getChoiceClasses().add(choiceClass);
            this.choiceRepo.save(choice);

            c.getChoiceClasses().add(choiceClass);
            c = this.classRepo.save(c);
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

        tape = tapeRepo.save(tape);
    }

    public void setupSubjects() {
        SubjectArea subjectArea = new SubjectArea();
        subjectArea.setName("Subject Area");

        this.subjectAreaRepo.save(subjectArea);
        subjectArea = this.subjectAreaRepo.findSubjectAreaByName(subjectArea.getName()).get();

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

        this.teacherRepo.save(teacher);
        teacher = this.teacherRepo.findTeacherByUser_Username(teacher.getUser().getUsername()).get();
    }

    private Class setupClasses(Class c, String name, Tape tape, Teacher teacher, Subject subject) {
        c = new Class();
        c.setName(name);
        c.setTape(tape);
        c.setSubject(subject);
        c.setTeacher(teacher);
        c.setChoiceClasses(new HashSet<>());
        c = this.classRepo.save(c);

        teacher.getClasses().add(c);
        teacher = this.teacherRepo.save(teacher);

        subject = subjectRepo.findSubjectByName(subject.getName()).get();
        subject.setClasses(new HashSet<>());
        subject.getClasses().add(c);
        subject = this.subjectRepo.save(subject);

        tape.getaClass().add(c);
        tape = tapeRepo.save(tape);

        return c;
    }

    private void setupRuleSet() {
        ruleSet = new RuleSet();
        ruleSet.setYear(11);

        ruleSetRepo.save(ruleSet);
        ruleSet = ruleSetRepo.findRuleSetByYear(11).get();
        setupRule();
        addSubjectToRule();
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
    public void testGetResults() {
        // Given
        Integer year = 11;
        setupRuleSet();
        aClass = setupClasses(aClass, "test", tape, teacher, subject);
        bClass = setupClasses(bClass, "test other", tape, teacher, subject);
        setupChoice(aClass, bClass, true, 1);

        // When
        ChoiceResultResponse response = this.choiceResultService.getResults(year);

        // Then
        Assertions.assertEquals(response.getClassStudentsResponses().size(), 2);
        response.getClassStudentsResponses().sort(Comparator.comparing(ClassStudentsResponse::getName));
        Assertions.assertEquals(response.getClassStudentsResponses().get(0).getName(), "test");
        Assertions.assertEquals(response.getClassStudentsResponses().get(0).getStudentSurveillanceResponses().size(), 1);
        Assertions.assertEquals(response.getClassStudentsResponses().get(0).getStudentSurveillanceResponses().get(0)
                .getStudentId(), student.getStudentId());

        Assertions.assertTrue(response.getStudentsNotFulfilledRules().isEmpty());
    }

    @Test
    public void testGetResultsNotAllRulesFulfilled() {
        // Given
        Integer year = 11;
        setupRuleSet();
        aClass = setupClasses(aClass, "test", tape, teacher, subject);
        bClass = setupClasses(bClass, "test other", tape, teacher, subject);
        setupChoice(aClass, bClass, false, 1);

        // When
        ChoiceResultResponse response = this.choiceResultService.getResults(year);

        // Then
        Assertions.assertEquals(response.getClassStudentsResponses().size(), 2);
        response.getClassStudentsResponses().sort(Comparator.comparing(ClassStudentsResponse::getName));
        Assertions.assertEquals(response.getClassStudentsResponses().get(0).getName(), "test");
        Assertions.assertTrue(response.getClassStudentsResponses().get(0).getStudentSurveillanceResponses().isEmpty());

        Assertions.assertEquals(response.getStudentsNotFulfilledRules().size(), 1);
        Assertions.assertEquals(response.getStudentsNotFulfilledRules().get(0).getStudentId(), student.getStudentId());

        Assertions.assertEquals(response.getStudentsNotChosen().size(), 1);
        Assertions.assertEquals(response.getStudentsNotChosen().get(0).getStudentId(), student.getStudentId());
    }

    @Test
    public void testGetResultsEmptyRuleSet() {
        // Given
        Integer year = 11;
        aClass = setupClasses(aClass, "test", tape, teacher, subject);
        bClass = setupClasses(bClass, "test other", tape, teacher, subject);
        setupChoice(aClass, bClass, true, 1);

        // When
        ChoiceResultResponse response = this.choiceResultService.getResults(year);

        // Then
        Assertions.assertEquals(response.getClassStudentsResponses().size(), 2);
        response.getClassStudentsResponses().sort(Comparator.comparing(ClassStudentsResponse::getName));
        Assertions.assertEquals(response.getClassStudentsResponses().get(0).getName(), "test");
        Assertions.assertEquals(response.getClassStudentsResponses().get(0).getStudentSurveillanceResponses().size(), 1);
        Assertions.assertEquals(response.getClassStudentsResponses().get(0).getStudentSurveillanceResponses().get(0)
                .getStudentId(), student.getStudentId());

        Assertions.assertTrue(response.getStudentsNotFulfilledRules().isEmpty());

        Assertions.assertEquals(response.getStudentsNotChosen().size(), 1);
        Assertions.assertEquals(response.getStudentsNotChosen().get(0).getStudentId(), student.getStudentId());
    }

    @Test
    public void testGetResultsChosen() {
        // Given
        Integer year = 11;
        aClass = setupClasses(aClass, "test", tape, teacher, subject);
        bClass = setupClasses(bClass, "test other", tape, teacher, subject);
        setupChoice(aClass, bClass, true, 1);
        setupChoice(aClass, bClass, false, 2);

        // When
        ChoiceResultResponse response = this.choiceResultService.getResults(year);

        // Then
        Assertions.assertEquals(response.getClassStudentsResponses().size(), 2);
        response.getClassStudentsResponses().sort(Comparator.comparing(ClassStudentsResponse::getName));
        Assertions.assertEquals(response.getClassStudentsResponses().get(0).getName(), "test");
        Assertions.assertEquals(response.getClassStudentsResponses().get(0).getStudentSurveillanceResponses().size(), 1);
        Assertions.assertEquals(response.getClassStudentsResponses().get(0).getStudentSurveillanceResponses().get(0)
                .getStudentId(), student.getStudentId());

        Assertions.assertTrue(response.getStudentsNotFulfilledRules().isEmpty());

        Assertions.assertTrue(response.getStudentsNotChosen().isEmpty());
    }
}
