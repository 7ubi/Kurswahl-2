package com.x7ubi.kurswahl.admin.classes.mapper;

import com.x7ubi.kurswahl.admin.classes.request.SubjectAreaCreationRequest;
import com.x7ubi.kurswahl.admin.classes.response.SubjectAreaResponse;
import com.x7ubi.kurswahl.admin.classes.response.SubjectAreaResponses;
import com.x7ubi.kurswahl.common.models.SubjectArea;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface SubjectAreaMapper {
    SubjectArea subjectAreaRequestToSubject(SubjectAreaCreationRequest subjectCreationRequest);

    void subjectAreaRequestToSubject(SubjectAreaCreationRequest subjectAreaCreationRequest, @MappingTarget SubjectArea subjectArea);

    default SubjectAreaResponses subjectAreasToSubjectAreaResponses(List<SubjectArea> subjectAreas) {
        SubjectAreaResponses subjectAreaResponses = new SubjectAreaResponses();
        subjectAreaResponses.setSubjectAreaResponses(subjectAreasToSubjectAreaResponseList(subjectAreas));
        return subjectAreaResponses;
    }

    List<SubjectAreaResponse> subjectAreasToSubjectAreaResponseList(List<SubjectArea> subjectAreas);

    SubjectAreaResponse subjectAreaToSubjectAreaResponse(SubjectArea subjectArea);
}
