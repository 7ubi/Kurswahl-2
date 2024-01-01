package com.x7ubi.kurswahl.admin.rule.mapper;

import com.x7ubi.kurswahl.admin.classes.mapper.SubjectMapper;
import com.x7ubi.kurswahl.admin.rule.request.RuleCreationRequest;
import com.x7ubi.kurswahl.admin.rule.response.RuleResponse;
import com.x7ubi.kurswahl.common.models.Rule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {SubjectMapper.class}
)
public interface RuleMapper {
    Rule ruleRequestToRule(RuleCreationRequest ruleCreationRequest);

    void ruleRequestToRule(RuleCreationRequest ruleCreationRequest, @MappingTarget Rule rule);

    @Mapping(source = "subjects", target = "subjectResponses")
    RuleResponse ruleToRuleResponse(Rule rule);

    List<RuleResponse> rulesToRuleResponses(List<Rule> rules);
}
