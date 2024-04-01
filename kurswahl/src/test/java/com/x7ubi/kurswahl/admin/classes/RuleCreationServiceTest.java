package com.x7ubi.kurswahl.admin.classes;

import com.x7ubi.kurswahl.KurswahlServiceTest;
import com.x7ubi.kurswahl.admin.classes.request.RuleCreationRequest;
import com.x7ubi.kurswahl.admin.classes.response.RuleResponse;
import com.x7ubi.kurswahl.admin.classes.service.RuleCreationService;
import com.x7ubi.kurswahl.common.error.ErrorMessage;
import com.x7ubi.kurswahl.common.exception.EntityCreationException;
import com.x7ubi.kurswahl.common.exception.EntityNotFoundException;
import com.x7ubi.kurswahl.common.models.*;
import com.x7ubi.kurswahl.common.repository.*;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@KurswahlServiceTest
public class RuleCreationServiceTest {
    @Autowired
    private RuleCreationService ruleCreationService;

    @Autowired
    private RuleSetRepo ruleSetRepo;

    @Autowired
    private RuleRepo ruleRepo;

    @Autowired
    private SubjectRepo subjectRepo;

    @Autowired
    private SubjectAreaRepo subjectAreaRepo;

    private Subject subject;

    private Subject otherSubject;

    private RuleSet ruleSet;

    private RuleSet otherRuleSet;

    private Rule rule;

    private Rule otherRule;

    @Autowired
    private SubjetRuleRepo subjetRuleRepo;

    @BeforeEach
    public void setupTest() {
        SubjectArea subjectArea = new SubjectArea();
        subjectArea.setName("Subject Area");

        subject = new Subject();
        subject.setName("Subject");
        subject.setSubjectArea(subjectArea);

        subjectArea = subjectAreaRepo.save(subjectArea);
        subject = subjectRepo.save(subject);

        otherSubject = new Subject();
        otherSubject.setName("Subject Other");
        otherSubject.setSubjectArea(subjectArea);
        otherSubject = subjectRepo.save(otherSubject);
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

    private void addSubjectToRule() {
        SubjectRule subjectRule = new SubjectRule();
        subjectRule.setSubject(subject);
        subjectRule.setRule(rule);

        subjectRule = this.subjetRuleRepo.save(subjectRule);

        rule.getSubjectRules().add(subjectRule);
        ruleRepo.save(rule);
    }

    @Test
    public void testCreateRuleCreatesRuleSet() throws EntityCreationException {
        // Given
        subject = subjectRepo.findSubjectByName(subject.getName()).get();

        RuleCreationRequest ruleCreationRequest = new RuleCreationRequest();
        ruleCreationRequest.setName("Test");
        ruleCreationRequest.setYear(11);
        ruleCreationRequest.setSubjectIds(List.of(subject.getSubjectId()));

        // When
        ruleCreationService.createRule(ruleCreationRequest);

        // Then
        rule = ruleRepo.findRuleByNameAndRuleSet_Year(ruleCreationRequest.getName(), 11).get();
        Assertions.assertEquals(rule.getName(), ruleCreationRequest.getName());
        Assertions.assertEquals(rule.getRuleSet().getYear(), ruleCreationRequest.getYear());
        Assertions.assertEquals(rule.getSubjectRules().size(), 1);
        Assertions.assertEquals(rule.getSubjectRules().stream().findFirst().get().getSubject().getSubjectId(), subject.getSubjectId());

        ruleSet = ruleSetRepo.findRuleSetByYear(11).get();
        Assertions.assertEquals(ruleSet.getRules().size(), 1);
        Assertions.assertEquals(ruleSet.getRules().stream().findFirst().get().getRuleId(), rule.getRuleId());

        subject = subjectRepo.findSubjectByName(subject.getName()).get();
        Assertions.assertEquals(subject.getSubjectRules().size(), 1);
        Assertions.assertEquals(subject.getSubjectRules().stream().findFirst().get().getRule().getRuleId(), rule.getRuleId());
    }

    @Test
    public void testCreateRule() throws EntityCreationException {
        // Given
        setupRuleSet();
        subject = subjectRepo.findSubjectByName(subject.getName()).get();

        RuleCreationRequest ruleCreationRequest = new RuleCreationRequest();
        ruleCreationRequest.setName("Test");
        ruleCreationRequest.setYear(11);
        ruleCreationRequest.setSubjectIds(List.of(subject.getSubjectId()));

        // When
        ruleCreationService.createRule(ruleCreationRequest);

        // Then
        rule = ruleRepo.findRuleByNameAndRuleSet_Year(ruleCreationRequest.getName(), 11).get();
        Assertions.assertEquals(rule.getName(), ruleCreationRequest.getName());
        Assertions.assertEquals(rule.getRuleSet().getYear(), ruleCreationRequest.getYear());
        Assertions.assertEquals(rule.getSubjectRules().size(), 1);
        Assertions.assertEquals(rule.getSubjectRules().stream().findFirst().get().getSubject().getSubjectId(), subject.getSubjectId());

        ruleSet = ruleSetRepo.findRuleSetByYear(11).get();
        Assertions.assertEquals(ruleSet.getRules().size(), 1);
        Assertions.assertEquals(ruleSet.getRules().stream().findFirst().get().getRuleId(), rule.getRuleId());

        subject = subjectRepo.findSubjectByName(subject.getName()).get();
        Assertions.assertEquals(subject.getSubjectRules().size(), 1);
        Assertions.assertEquals(subject.getSubjectRules().stream().findFirst().get().getRule().getRuleId(), rule.getRuleId());
    }

    @Test
    public void testCreateRuleNameExists() {
        // Given
        setupRuleSet();
        setupRule();
        subject = subjectRepo.findSubjectByName(subject.getName()).get();

        RuleCreationRequest ruleCreationRequest = new RuleCreationRequest();
        ruleCreationRequest.setName("Rule");
        ruleCreationRequest.setYear(11);
        ruleCreationRequest.setSubjectIds(List.of(subject.getSubjectId()));

        // When
        EntityCreationException exception = Assert.assertThrows(EntityCreationException.class, () ->
                ruleCreationService.createRule(ruleCreationRequest));

        // Then
        Assertions.assertEquals(exception.getMessage(), ErrorMessage.RULE_ALREADY_EXISTS);
    }

    @Test
    public void testCreateRuleWrongSubjectId() throws EntityCreationException {
        // Given
        setupRuleSet();
        subject = subjectRepo.findSubjectByName(subject.getName()).get();

        RuleCreationRequest ruleCreationRequest = new RuleCreationRequest();
        ruleCreationRequest.setName("Test");
        ruleCreationRequest.setYear(11);
        ruleCreationRequest.setSubjectIds(List.of(subject.getSubjectId(), subject.getSubjectId() + 3));

        // When
        ruleCreationService.createRule(ruleCreationRequest);

        // Then
        rule = ruleRepo.findRuleByNameAndRuleSet_Year(ruleCreationRequest.getName(), 11).get();
        Assertions.assertEquals(rule.getName(), ruleCreationRequest.getName());
        Assertions.assertEquals(rule.getRuleSet().getYear(), ruleCreationRequest.getYear());
        Assertions.assertEquals(rule.getSubjectRules().size(), 1);
        Assertions.assertEquals(rule.getSubjectRules().stream().findFirst().get().getSubject().getSubjectId(), subject.getSubjectId());

        ruleSet = ruleSetRepo.findRuleSetByYear(11).get();
        Assertions.assertEquals(ruleSet.getRules().size(), 1);
        Assertions.assertEquals(ruleSet.getRules().stream().findFirst().get().getRuleId(), rule.getRuleId());

        subject = subjectRepo.findSubjectByName(subject.getName()).get();
        Assertions.assertEquals(subject.getSubjectRules().size(), 1);
        Assertions.assertEquals(subject.getSubjectRules().stream().findFirst().get().getRule().getRuleId(), rule.getRuleId());
    }

    @Test
    public void testEditRule() throws EntityCreationException, EntityNotFoundException {
        // Given
        setupRuleSet();
        setupRule();
        addSubjectToRule();

        RuleCreationRequest ruleCreationRequest = new RuleCreationRequest();
        ruleCreationRequest.setName("Rule");
        ruleCreationRequest.setYear(12);
        ruleCreationRequest.setSubjectIds(List.of(otherSubject.getSubjectId()));

        // When
        ruleCreationService.editRule(rule.getRuleId(), ruleCreationRequest);

        // Then
        rule = ruleRepo.findRuleByNameAndRuleSet_Year(ruleCreationRequest.getName(), 12).get();
        Assertions.assertEquals(rule.getName(), ruleCreationRequest.getName());
        Assertions.assertEquals(rule.getRuleSet().getYear(), ruleCreationRequest.getYear());
        Assertions.assertEquals(rule.getSubjectRules().size(), 1);
        Assertions.assertEquals(rule.getSubjectRules().stream().findFirst().get().getSubject().getSubjectId(), otherSubject.getSubjectId());

        ruleSet = ruleSetRepo.findRuleSetByYear(12).get();
        Assertions.assertEquals(ruleSet.getRules().size(), 1);
        Assertions.assertEquals(ruleSet.getRules().stream().findFirst().get().getRuleId(), rule.getRuleId());

        ruleSet = ruleSetRepo.findRuleSetByYear(11).get();
        Assertions.assertTrue(ruleSet.getRules().isEmpty());

        subject = subjectRepo.findSubjectByName(subject.getName()).get();
        Assertions.assertTrue(subject.getSubjectRules().isEmpty());

        otherSubject = subjectRepo.findSubjectByName(otherSubject.getName()).get();
        Assertions.assertEquals(otherSubject.getSubjectRules().size(), 1);
        Assertions.assertEquals(otherSubject.getSubjectRules().stream().findFirst().get().getRule().getRuleId(), rule.getRuleId());
    }

    @Test
    public void testEditRuleWrongSubjectId() throws EntityCreationException, EntityNotFoundException {
        // Given
        setupRuleSet();
        subject = subjectRepo.findSubjectByName(subject.getName()).get();
        otherSubject = subjectRepo.findSubjectByName(otherSubject.getName()).get();
        setupRule();

        RuleCreationRequest ruleCreationRequest = new RuleCreationRequest();
        ruleCreationRequest.setName("Test");
        ruleCreationRequest.setYear(12);
        ruleCreationRequest.setSubjectIds(List.of(otherSubject.getSubjectId() + 3));

        // When
        ruleCreationService.editRule(rule.getRuleId(), ruleCreationRequest);

        // Then
        rule = ruleRepo.findRuleByNameAndRuleSet_Year(ruleCreationRequest.getName(), 12).get();
        Assertions.assertEquals(rule.getName(), ruleCreationRequest.getName());
        Assertions.assertEquals(rule.getRuleSet().getYear(), ruleCreationRequest.getYear());
        Assertions.assertTrue(rule.getSubjectRules().isEmpty());

        ruleSet = ruleSetRepo.findRuleSetByYear(12).get();
        Assertions.assertEquals(ruleSet.getRules().size(), 1);
        Assertions.assertEquals(ruleSet.getRules().stream().findFirst().get().getRuleId(), rule.getRuleId());

        ruleSet = ruleSetRepo.findRuleSetByYear(11).get();
        Assertions.assertTrue(ruleSet.getRules().isEmpty());

        subject = subjectRepo.findSubjectByName(subject.getName()).get();
        Assertions.assertTrue(subject.getSubjectRules().isEmpty());

        otherSubject = subjectRepo.findSubjectByName(otherSubject.getName()).get();
        Assertions.assertTrue(otherSubject.getSubjectRules().isEmpty());
    }

    @Test
    public void testEditRuleWrongRuleId() {
        // Given
        setupRuleSet();
        subject = subjectRepo.findSubjectByName(subject.getName()).get();
        otherSubject = subjectRepo.findSubjectByName(otherSubject.getName()).get();
        setupRule();

        RuleCreationRequest ruleCreationRequest = new RuleCreationRequest();
        ruleCreationRequest.setName("Test");
        ruleCreationRequest.setYear(12);
        ruleCreationRequest.setSubjectIds(List.of(otherSubject.getSubjectId()));

        // When
        EntityNotFoundException exception = Assert.assertThrows(EntityNotFoundException.class, () ->
                this.ruleCreationService.editRule(rule.getRuleId() + 3, ruleCreationRequest));

        // Then
        Assertions.assertEquals(exception.getMessage(), ErrorMessage.RULE_NOT_FOUND);
    }

    @Test
    public void testEditRuleNameExists() {
        // Given
        setupRuleSet();
        subject = subjectRepo.findSubjectByName(subject.getName()).get();
        otherSubject = subjectRepo.findSubjectByName(otherSubject.getName()).get();
        setupRule();
        setupOtherRule();

        RuleCreationRequest ruleCreationRequest = new RuleCreationRequest();
        ruleCreationRequest.setName(otherRule.getName());
        ruleCreationRequest.setYear(12);
        ruleCreationRequest.setSubjectIds(List.of(otherSubject.getSubjectId()));

        // When
        EntityCreationException exception = Assert.assertThrows(EntityCreationException.class, () ->
                this.ruleCreationService.editRule(rule.getRuleId(), ruleCreationRequest));

        // Then
        Assertions.assertEquals(exception.getMessage(), ErrorMessage.RULE_ALREADY_EXISTS);
    }

    @Test
    public void testGetRule() throws EntityNotFoundException {
        // Given
        setupRuleSet();
        setupRule();
        addSubjectToRule();

        // When
        RuleResponse response = this.ruleCreationService.getRule(rule.getRuleId());

        // Then
        Assertions.assertEquals(response.getRuleId(), rule.getRuleId());
        Assertions.assertEquals(response.getName(), rule.getName());
        Assertions.assertEquals(response.getSubjectResponses().size(), 1);
        Assertions.assertEquals(response.getSubjectResponses().get(0).getSubjectId(), subject.getSubjectId());
    }

    @Test
    public void testGetRuleRuleNotFound() {
        // Given
        setupRuleSet();
        setupRule();
        addSubjectToRule();

        // When
        EntityNotFoundException exception = Assert.assertThrows(EntityNotFoundException.class, () ->
                this.ruleCreationService.getRule(rule.getRuleId() + 3));

        // Then
        Assertions.assertEquals(exception.getMessage(), ErrorMessage.RULE_NOT_FOUND);
    }

    @Test
    public void testGetRules() {
        // Given
        setupRuleSet();
        setupRule();
        addSubjectToRule();

        // When
        List<RuleResponse> responses = this.ruleCreationService.getRules(11);

        // Then
        Assertions.assertEquals(responses.size(), 1);
        Assertions.assertEquals(responses.get(0).getRuleId(), rule.getRuleId());
        Assertions.assertEquals(responses.get(0).getName(), rule.getName());
        Assertions.assertEquals(responses.get(0).getSubjectResponses().size(), 1);
        Assertions.assertEquals(responses.get(0).getSubjectResponses().get(0).getSubjectId(), subject.getSubjectId());
    }

    @Test
    public void testGetRulesNoRules() {
        // When
        List<RuleResponse> responses = this.ruleCreationService.getRules(11);

        // Then
        Assertions.assertTrue(responses.isEmpty());
    }

    @Test
    public void testDeleteRule() throws EntityNotFoundException {
        // Given
        setupRuleSet();
        setupRule();
        addSubjectToRule();

        // When
        List<RuleResponse> responses = this.ruleCreationService.deleteRule(rule.getRuleId());

        // Then
        Assertions.assertTrue(responses.isEmpty());

        Assertions.assertFalse(ruleRepo.existsRuleByNameAndRuleSet_Year(rule.getName(), 11));

        ruleSet = ruleSetRepo.findRuleSetByYear(11).get();
        Assertions.assertTrue(ruleSet.getRules().isEmpty());

        subject = subjectRepo.findSubjectByName(subject.getName()).get();
        Assertions.assertTrue(subject.getSubjectRules().isEmpty());
    }

    @Test
    public void testDeleteRuleRuleNotFound() {
        // Given
        setupRuleSet();
        setupRule();
        addSubjectToRule();

        // When
        EntityNotFoundException exception = Assert.assertThrows(EntityNotFoundException.class, () ->
                this.ruleCreationService.deleteRule(rule.getRuleId() + 3));

        // Then
        Assertions.assertEquals(exception.getMessage(), ErrorMessage.RULE_NOT_FOUND);

        Assertions.assertTrue(ruleRepo.existsRuleByNameAndRuleSet_Year(rule.getName(), 11));
    }

    @Test
    public void testDeleteRules() throws EntityNotFoundException {
        // Given
        setupRuleSet();
        setupRule();
        addSubjectToRule();

        // When
        List<RuleResponse> responses = this.ruleCreationService.deleteRules(List.of(rule.getRuleId()));

        // Then
        Assertions.assertTrue(responses.isEmpty());

        Assertions.assertFalse(ruleRepo.existsRuleByNameAndRuleSet_Year(rule.getName(), 11));

        ruleSet = ruleSetRepo.findRuleSetByYear(11).get();
        Assertions.assertTrue(ruleSet.getRules().isEmpty());

        subject = subjectRepo.findSubjectByName(subject.getName()).get();
        Assertions.assertTrue(subject.getSubjectRules().isEmpty());
    }

    @Test
    public void testDeleteRulesEmptyList() throws EntityNotFoundException {
        // Given
        setupRuleSet();
        setupRule();
        addSubjectToRule();

        // When
        List<RuleResponse> responses = this.ruleCreationService.deleteRules(new ArrayList<>());

        // Then
        Assertions.assertTrue(responses.isEmpty());

        Assertions.assertTrue(ruleRepo.existsRuleByNameAndRuleSet_Year(rule.getName(), 11));
    }

    @Test
    public void testDeleteRulesRuleNotFound() {
        // Given
        setupRuleSet();
        setupRule();
        addSubjectToRule();

        // When
        EntityNotFoundException exception = Assert.assertThrows(EntityNotFoundException.class, () ->
                this.ruleCreationService.deleteRules(List.of(rule.getRuleId() + 3)));

        // Then
        Assertions.assertEquals(exception.getMessage(), ErrorMessage.RULE_NOT_FOUND);

        Assertions.assertTrue(ruleRepo.existsRuleByNameAndRuleSet_Year(rule.getName(), 11));
    }
}
