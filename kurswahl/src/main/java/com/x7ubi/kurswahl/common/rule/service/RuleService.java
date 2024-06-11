package com.x7ubi.kurswahl.common.rule.service;

import com.x7ubi.kurswahl.common.models.ChoiceClass;
import com.x7ubi.kurswahl.common.models.Rule;
import com.x7ubi.kurswahl.common.models.RuleSet;
import com.x7ubi.kurswahl.common.models.SubjectRule;
import com.x7ubi.kurswahl.common.repository.RuleSetRepo;
import com.x7ubi.kurswahl.common.rule.response.RuleResponse;
import com.x7ubi.kurswahl.student.choice.mapper.ChoiceRuleMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class RuleService {

    private final Logger logger = LoggerFactory.getLogger(RuleService.class);

    private final RuleSetRepo ruleSetRepo;

    private final ChoiceRuleMapper choiceRuleMapper;

    public final static int NUMBER_OF_LKS = 2;

    public final static String LK_NOT_FULFILLED_RULE = String.format("Es müssen %s Leistungskurse gewählt werden", NUMBER_OF_LKS);


    public RuleService(RuleSetRepo ruleSetRepo, ChoiceRuleMapper choiceRuleMapper) {
        this.ruleSetRepo = ruleSetRepo;
        this.choiceRuleMapper = choiceRuleMapper;
    }

    public List<RuleResponse> getAllRules(Integer year) {
        Optional<RuleSet> ruleSet = this.ruleSetRepo.findRuleSetByYear(year);
        if (ruleSet.isEmpty()) {
            return new ArrayList<>();
        }

        return this.choiceRuleMapper.rulesToRuleResponses(ruleSet.get().getRules().stream().toList());
    }

    @Transactional(readOnly = true)
    public List<RuleResponse> getRulesByChoiceClasses(Integer year, Set<ChoiceClass> choiceClasses) {
        Optional<RuleSet> ruleSet = this.ruleSetRepo.findRuleSetByYear(year);
        if (ruleSet.isEmpty()) {
            return new ArrayList<>();
        }
        List<Rule> rules = new ArrayList<>();
        Set<ChoiceClass> usedChoiceClasses = new HashSet<>();

        List<Rule> ruleSetRules = new ArrayList<>(ruleSet.get().getRules().stream().toList());
        ruleSetRules.sort(Comparator.comparing(Rule::getName));

        for (Rule rule : ruleSetRules) {
            boolean ruleFulfilled = false;
            for (SubjectRule subjectRule : rule.getSubjectRules()) {
                Optional<ChoiceClass> matchingChoiceClass = choiceClasses.stream()
                        .filter(choiceClass -> !usedChoiceClasses.contains(choiceClass))
                        .filter(choiceClass -> Objects.equals(choiceClass.getaClass().getSubject().getSubjectId(), subjectRule.getSubject().getSubjectId()))
                        .findFirst();

                if (matchingChoiceClass.isPresent()) {
                    ruleFulfilled = true;
                    usedChoiceClasses.add(matchingChoiceClass.get());
                    break;
                }
            }

            if (!ruleFulfilled) {
                rules.add(rule);
            }
        }
        List<RuleResponse> ruleResponses = this.choiceRuleMapper.rulesToRuleResponses(rules);

        if (!getNumberOfLKsFulfilled(choiceClasses)) {
            RuleResponse lkRuleNotFulfilled = new RuleResponse();
            lkRuleNotFulfilled.setName(LK_NOT_FULFILLED_RULE);
            lkRuleNotFulfilled.setSubjectResponses(new ArrayList<>());
            ruleResponses.add(lkRuleNotFulfilled);
        }

        return ruleResponses;
    }


    @Transactional(readOnly = true)
    public Boolean getRulesFulfilled(RuleSet ruleSet, Set<ChoiceClass> choiceClasses) {
        for (Rule rule : ruleSet.getRules()) {
            boolean fulfilled = false;
            for (SubjectRule subjectRule : rule.getSubjectRules()) {
                if (choiceClasses.stream().anyMatch(c -> Objects.equals(c.getaClass().getSubject().getSubjectId(),
                        subjectRule.getSubject().getSubjectId()))) {
                    fulfilled = true;
                    break;
                }
            }

            if (!fulfilled) {
                return false;
            }
        }
        return getNumberOfLKsFulfilled(choiceClasses);
    }

    @Transactional(readOnly = true)
    public Boolean getNumberOfLKsFulfilled(Set<ChoiceClass> choiceClasses) {
        AtomicInteger numberOfLks = new AtomicInteger();

        for (ChoiceClass choiceClass : choiceClasses) {
            if (choiceClass.getaClass().getTape().getLk()) {
                numberOfLks.getAndIncrement();

                if (numberOfLks.get() == NUMBER_OF_LKS) {
                    return true;
                }
            }
        }

        return false;
    }

}
