package com.x7ubi.kurswahl.student.choice.mapper;

import com.x7ubi.kurswahl.common.models.Rule;
import com.x7ubi.kurswahl.common.models.SubjectRule;
import com.x7ubi.kurswahl.common.rule.response.RuleResponse;
import com.x7ubi.kurswahl.common.rule.response.SubjectResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ChoiceRuleMapper {

    @Mapping(source = "subjectRules", target = "subjectResponses")
    RuleResponse ruleToRuleResponse(Rule rule);

    @Mapping(source = "subject.name", target = "name")
    SubjectResponse subjectRulesToSubjectResponse(SubjectRule subjectRule);

    List<RuleResponse> rulesToRuleResponses(List<Rule> rules);
}
