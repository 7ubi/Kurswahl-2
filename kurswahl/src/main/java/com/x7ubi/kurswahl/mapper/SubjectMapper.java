package com.x7ubi.kurswahl.mapper;

import com.x7ubi.kurswahl.models.Subject;
import com.x7ubi.kurswahl.request.admin.SubjectCreationRequest;
import com.x7ubi.kurswahl.response.admin.classes.SubjectResponse;
import com.x7ubi.kurswahl.response.admin.classes.SubjectResponses;
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
