package com.x7ubi.kurswahl.admin.classes.mapper;

import com.x7ubi.kurswahl.admin.classes.request.SubjectCreationRequest;
import com.x7ubi.kurswahl.admin.classes.response.SubjectResponse;
import com.x7ubi.kurswahl.admin.classes.response.SubjectResponses;
import com.x7ubi.kurswahl.common.models.Subject;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {SubjectAreaMapper.class}
)
public interface SubjectMapper {
    Subject subjectRequestToSubject(SubjectCreationRequest subjectCreationRequest);

    void subjectRequestToSubject(SubjectCreationRequest subjectCreationRequest, @MappingTarget Subject subject);

    default SubjectResponses subjectsToSubjectResponses(List<Subject> subjects) {
        SubjectResponses subjectResponses = new SubjectResponses();
        subjectResponses.setSubjectResponses(subjectsToSubjectResponseList(subjects));
        return subjectResponses;
    }

    List<SubjectResponse> subjectsToSubjectResponseList(List<Subject> subjects);

    @Mapping(source = "subjectArea", target = "subjectAreaResponse")
    SubjectResponse subjectToSubjectResponse(Subject subject);
}
