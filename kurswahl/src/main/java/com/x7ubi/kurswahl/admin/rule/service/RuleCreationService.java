package com.x7ubi.kurswahl.admin.rule.service;

import com.x7ubi.kurswahl.admin.rule.mapper.RuleMapper;
import com.x7ubi.kurswahl.admin.rule.request.RuleCreationRequest;
import com.x7ubi.kurswahl.admin.rule.response.RuleResponse;
import com.x7ubi.kurswahl.common.error.ErrorMessage;
import com.x7ubi.kurswahl.common.exception.EntityCreationException;
import com.x7ubi.kurswahl.common.exception.EntityNotFoundException;
import com.x7ubi.kurswahl.common.models.Rule;
import com.x7ubi.kurswahl.common.models.RuleSet;
import com.x7ubi.kurswahl.common.models.Subject;
import com.x7ubi.kurswahl.common.repository.RuleRepo;
import com.x7ubi.kurswahl.common.repository.RuleSetRepo;
import com.x7ubi.kurswahl.common.repository.SubjectRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class RuleCreationService {
    private final Logger logger = LoggerFactory.getLogger(RuleCreationService.class);

    private final RuleSetRepo ruleSetRepo;

    private final RuleRepo ruleRepo;

    private final SubjectRepo subjectRepo;

    private final RuleMapper ruleMapper;

    public RuleCreationService(RuleSetRepo ruleSetRepo, RuleRepo ruleRepo, SubjectRepo subjectRepo, RuleMapper ruleMapper) {
        this.ruleSetRepo = ruleSetRepo;
        this.ruleRepo = ruleRepo;
        this.subjectRepo = subjectRepo;
        this.ruleMapper = ruleMapper;
    }

    @Transactional
    public void createRule(RuleCreationRequest ruleCreationRequest) throws EntityCreationException {
        RuleSet ruleSet = getOrCreateRuleSet(ruleCreationRequest.getYear());

        if (ruleRepo.existsRuleByNameAndRuleSet_Year(ruleCreationRequest.getName(), ruleCreationRequest.getYear())) {
            throw new EntityCreationException(ErrorMessage.RULE_ALREADY_EXISTS);
        }

        Rule rule = this.ruleMapper.ruleRequestToRule(ruleCreationRequest);
        rule.setRuleSet(ruleSet);
        rule.setSubjects(new HashSet<>());

        ruleSet.getRules().add(rule);
        this.ruleRepo.saveAndFlush(rule);
        this.ruleSetRepo.saveAndFlush(ruleSet);

        for (Long subjectId : ruleCreationRequest.getSubjectIds()) {
            addSubjectToRule(subjectId, rule);
        }
        logger.info(String.format("Created Rule %s", rule.getName()));

        this.ruleRepo.saveAndFlush(rule);
    }

    @Transactional
    protected void addSubjectToRule(Long subjectId, Rule rule) {
        Optional<Subject> subjectOptional = this.subjectRepo.findSubjectBySubjectId(subjectId);
        if (subjectOptional.isEmpty()) {
            return;
        }
        Subject subject = subjectOptional.get();
        subject.getRules().add(rule);
        rule.getSubjects().add(subject);
        this.subjectRepo.saveAndFlush(subject);
    }

    @Transactional
    protected RuleSet getOrCreateRuleSet(Integer year) {
        Optional<RuleSet> ruleSetOptional = this.ruleSetRepo.findRuleSetByYear(year);
        if (ruleSetOptional.isPresent()) {
            return ruleSetOptional.get();
        } else {
            RuleSet ruleSet = new RuleSet();
            ruleSet.setRules(new HashSet<>());
            ruleSet.setYear(year);

            this.ruleSetRepo.saveAndFlush(ruleSet);
            logger.info(String.format("Created Rule Set for year: %s", year));
            return this.ruleSetRepo.findRuleSetByYear(year).get();
        }
    }

    public List<RuleResponse> getRules(Integer year) {
        Optional<RuleSet> ruleSetOptional = this.ruleSetRepo.findRuleSetByYear(year);
        if (ruleSetOptional.isEmpty()) {
            return new ArrayList<>();
        }

        RuleSet ruleSet = ruleSetOptional.get();
        logger.info(String.format("Got Rules for year %s", year));

        return this.ruleMapper.rulesToRuleResponses(ruleSet.getRules().stream().toList());
    }

    public RuleResponse getRule(Long ruleId) throws EntityNotFoundException {
        Rule rule = getRuleById(ruleId);
        return this.ruleMapper.ruleToRuleResponse(rule);
    }

    private Rule getRuleById(Long ruleId) throws EntityNotFoundException {
        Optional<Rule> ruleOptional = this.ruleRepo.findRuleByRuleId(ruleId);
        if (ruleOptional.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.RULE_NOT_FOUND);
        }
        return ruleOptional.get();
    }

    @Transactional
    public void editRule(Long ruleId, RuleCreationRequest ruleCreationRequest)
            throws EntityNotFoundException, EntityCreationException {
        Rule rule = getRuleById(ruleId);

        if (!Objects.equals(rule.getName(), ruleCreationRequest.getName())) {
            if (ruleRepo.existsRuleByNameAndRuleSet_Year(ruleCreationRequest.getName(), ruleCreationRequest.getYear())) {
                throw new EntityCreationException(ErrorMessage.RULE_ALREADY_EXISTS);
            }
            this.ruleMapper.ruleRequestToRule(ruleCreationRequest, rule);
        }

        if (!Objects.equals(rule.getRuleSet().getYear(), ruleCreationRequest.getYear())) {
            rule.getRuleSet().getRules().remove(rule);
            ruleSetRepo.save(rule.getRuleSet());

            RuleSet ruleSet = getOrCreateRuleSet(ruleCreationRequest.getYear());
            ruleSet.getRules().add(rule);
            ruleSetRepo.save(ruleSet);

            rule.setRuleSet(ruleSet);
        }

        for (Subject subject : rule.getSubjects()) {
            if (ruleCreationRequest.getSubjectIds().stream().noneMatch(subjectId ->
                    Objects.equals(subject.getSubjectId(), subjectId))) {
                removeSubjectFromRule(subject, rule);
            }
        }

        for (Long subjectId : ruleCreationRequest.getSubjectIds()) {
            if (rule.getSubjects().stream().noneMatch(subject -> Objects.equals(subject.getSubjectId(), subjectId))) {
                addSubjectToRule(subjectId, rule);
            }
        }

        ruleRepo.save(rule);
    }

    @Transactional
    protected void removeSubjectFromRule(Subject subject, Rule rule) {
        subject.getRules().remove(rule);
        subjectRepo.save(subject);
        rule.getSubjects().remove(subject);
    }

    @Transactional
    public List<RuleResponse> deleteRule(Long ruleId) throws EntityNotFoundException {
        Integer year = deleteRuleHelper(ruleId);
        return getRules(year);
    }

    @Transactional
    protected Integer deleteRuleHelper(Long ruleId) throws EntityNotFoundException {
        Rule rule = getRuleById(ruleId);

        for (Subject subject : rule.getSubjects()) {
            removeSubjectFromRule(subject, rule);
        }

        rule.getRuleSet().getRules().remove(rule);
        ruleSetRepo.save(rule.getRuleSet());

        ruleRepo.delete(rule);

        return rule.getRuleSet().getYear();
    }

    @Transactional
    public List<RuleResponse> deleteRules(List<Long> ruleIds) throws EntityNotFoundException {
        Integer year = null;
        for (Long ruleId : ruleIds) {
            year = deleteRuleHelper(ruleId);
        }

        return getRules(year);
    }
}
