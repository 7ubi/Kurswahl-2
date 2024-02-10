package com.x7ubi.kurswahl.common.rule.service;

import com.x7ubi.kurswahl.common.models.ChoiceClass;
import com.x7ubi.kurswahl.common.models.Rule;
import com.x7ubi.kurswahl.common.models.RuleSet;
import com.x7ubi.kurswahl.common.models.Subject;
import com.x7ubi.kurswahl.common.repository.RuleSetRepo;
import com.x7ubi.kurswahl.common.rule.response.RuleResponse;
import com.x7ubi.kurswahl.student.choice.mapper.ChoiceRuleMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RuleService {

    private final Logger logger = LoggerFactory.getLogger(RuleService.class);

    private final RuleSetRepo ruleSetRepo;

    private final ChoiceRuleMapper choiceRuleMapper;


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

    public List<RuleResponse> getRulesByChoiceClasses(Integer year, Set<ChoiceClass> choiceClasses) {
        Optional<RuleSet> ruleSet = this.ruleSetRepo.findRuleSetByYear(year);
        if (ruleSet.isEmpty()) {
            return new ArrayList<>();
        }
        List<Rule> rules = new ArrayList<>();

        for (Rule rule : ruleSet.get().getRules()) {
            boolean ruleFulfilled = false;
            for (Subject subject : rule.getSubjects()) {
                if (choiceClasses.stream().anyMatch(choiceClass -> Objects.equals(choiceClass.getaClass().getSubject()
                        .getSubjectId(), subject.getSubjectId()))) {
                    ruleFulfilled = true;
                    break;
                }
            }

            if (!ruleFulfilled) {
                rules.add(rule);
            }
        }

        return this.choiceRuleMapper.rulesToRuleResponses(rules);
    }
}
