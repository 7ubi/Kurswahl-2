package com.x7ubi.kurswahl.admin.classes.mapper;

import com.x7ubi.kurswahl.admin.classes.request.RuleCreationRequest;
import com.x7ubi.kurswahl.admin.classes.response.RuleResponse;
import com.x7ubi.kurswahl.admin.classes.response.SubjectResponse;
import com.x7ubi.kurswahl.common.models.Rule;
import com.x7ubi.kurswahl.common.models.SubjectRule;
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

    @Mapping(source = "subjectRules", target = "subjectResponses")
    @Mapping(source = "ruleSet.year", target = "year")
    RuleResponse ruleToRuleResponse(Rule rule);

    List<RuleResponse> rulesToRuleResponses(List<Rule> rules);

    @Mapping(source = "subject.subjectId", target = "subjectId")
    @Mapping(source = "subject.name", target = "name")
    @Mapping(source = "subject.subjectArea", target = "subjectAreaResponse")
    SubjectResponse subjectRulesToSubjectResponse(SubjectRule subjectRule);
}
