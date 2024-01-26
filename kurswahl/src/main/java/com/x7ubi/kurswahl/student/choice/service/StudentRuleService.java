package com.x7ubi.kurswahl.student.choice.service;

import com.x7ubi.kurswahl.common.models.Choice;
import com.x7ubi.kurswahl.common.models.Rule;
import com.x7ubi.kurswahl.common.models.RuleSet;
import com.x7ubi.kurswahl.common.models.Subject;
import com.x7ubi.kurswahl.common.repository.RuleSetRepo;
import com.x7ubi.kurswahl.student.choice.mapper.ChoiceRuleMapper;
import com.x7ubi.kurswahl.student.choice.response.RuleResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class StudentRuleService {

    private final Logger logger = LoggerFactory.getLogger(StudentRuleService.class);

    private final RuleSetRepo ruleSetRepo;

    private final ChoiceRuleMapper choiceRuleMapper;


    public StudentRuleService(RuleSetRepo ruleSetRepo, ChoiceRuleMapper choiceRuleMapper) {
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

    public List<RuleResponse> getRulesByChoice(Integer year, Choice choice) {
        Optional<RuleSet> ruleSet = this.ruleSetRepo.findRuleSetByYear(year);
        if (ruleSet.isEmpty()) {
            return new ArrayList<>();
        }

        List<Rule> rules = new ArrayList<>();

        for (Rule rule : ruleSet.get().getRules()) {
            boolean ruleFulfilled = false;
            for (Subject subject : rule.getSubjects()) {
                if (choice.getChoiceClasses().stream().anyMatch(c -> Objects.equals(c.getaClass().getSubject().getSubjectId(),
                        subject.getSubjectId()))) {
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
