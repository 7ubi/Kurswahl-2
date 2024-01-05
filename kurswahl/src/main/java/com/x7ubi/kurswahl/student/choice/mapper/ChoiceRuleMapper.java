package com.x7ubi.kurswahl.student.choice.mapper;

import com.x7ubi.kurswahl.common.models.Rule;
import com.x7ubi.kurswahl.student.choice.response.RuleResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ChoiceRuleMapper {

    @Mapping(source = "subjects", target = "subjectResponses")
    RuleResponse ruleToRuleResponse(Rule rule);

    List<RuleResponse> rulesToRuleResponses(List<Rule> rules);
}
