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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

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
            Optional<Subject> subjectOptional = this.subjectRepo.findSubjectBySubjectId(subjectId);
            if (subjectOptional.isEmpty()) {
                continue;
            }
            Subject subject = subjectOptional.get();
            subject.getRules().add(rule);
            rule.getSubjects().add(subject);
            this.subjectRepo.saveAndFlush(subject);
        }
        logger.info(String.format("Created Rule %s", rule.getName()));

        this.ruleRepo.saveAndFlush(rule);
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
        logger.info(String.format("Got Rules for year: %s", year));

        return this.ruleMapper.rulesToRuleResponses(ruleSet.getRules().stream().toList());
    }

    public RuleResponse getRule(Long ruleId) throws EntityNotFoundException {
        Optional<Rule> ruleOptional = this.ruleRepo.findRuleByRuleId(ruleId);
        if (ruleOptional.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.RULE_NOT_FOUND);
        }
        Rule rule = ruleOptional.get();
        return this.ruleMapper.ruleToRuleResponse(rule);
    }
}
