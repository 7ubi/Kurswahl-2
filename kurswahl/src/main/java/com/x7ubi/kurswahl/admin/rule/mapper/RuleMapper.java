package com.x7ubi.kurswahl.admin.rule.mapper;

import com.x7ubi.kurswahl.admin.rule.request.RuleCreationRequest;
import com.x7ubi.kurswahl.common.models.Rule;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface RuleMapper {
    Rule ruleRequestToRule(RuleCreationRequest ruleCreationRequest);
}
