package com.x7ubi.kurswahl.mapper;

import com.x7ubi.kurswahl.models.SubjectArea;
import com.x7ubi.kurswahl.request.admin.SubjectAreaCreationRequest;
import com.x7ubi.kurswahl.response.admin.classes.SubjectAreaResponse;
import com.x7ubi.kurswahl.response.admin.classes.SubjectAreaResponses;
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
