package com.x7ubi.kurswahl.admin.rule.service;

import com.x7ubi.kurswahl.admin.rule.mapper.RuleMapper;
import com.x7ubi.kurswahl.admin.rule.request.RuleCreationRequest;
import com.x7ubi.kurswahl.common.models.Rule;
import com.x7ubi.kurswahl.common.models.RuleSet;
import com.x7ubi.kurswahl.common.models.Subject;
import com.x7ubi.kurswahl.common.repository.RuleRepo;
import com.x7ubi.kurswahl.common.repository.RuleSetRepo;
import com.x7ubi.kurswahl.common.repository.SubjectRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;

@Service
public class RuleCreationService {
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
    public void createRule(RuleCreationRequest ruleCreationRequest) {
        RuleSet ruleSet = getOrCreateRuleSet(ruleCreationRequest.getYear());

        Rule rule = this.ruleMapper.ruleRequestToRule(ruleCreationRequest);
        rule.setRuleSet(ruleSet);
        rule.setSubjects(new HashSet<>());

        this.ruleRepo.saveAndFlush(rule);
        ruleSet.getRules().add(rule);
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

            return ruleSet;
        }
    }
}
