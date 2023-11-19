package com.x7ubi.kurswahl.mapper;

import com.x7ubi.kurswahl.models.SubjectArea;
import com.x7ubi.kurswahl.response.admin.classes.SubjectAreaResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface SubjectAreaMapper {
    SubjectAreaResponse subjectAreaToSubjectAreaResponse(SubjectArea subjectArea);
}
