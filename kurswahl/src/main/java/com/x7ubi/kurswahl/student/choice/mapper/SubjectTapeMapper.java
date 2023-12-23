package com.x7ubi.kurswahl.student.choice.mapper;

import com.x7ubi.kurswahl.common.models.Class;
import com.x7ubi.kurswahl.common.models.Subject;
import com.x7ubi.kurswahl.student.choice.response.SubjectTapeResponse;
import com.x7ubi.kurswahl.student.choice.response.TapeResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface SubjectTapeMapper {

    List<SubjectTapeResponse> subjectsToSubjectTapeResponses(List<Subject> subjects);

    @Mapping(source = "classes", target = "tapeResponses")
    SubjectTapeResponse subjectToSubjectTapeResponse(Subject subject);

    @Mapping(source = "tape.name", target = "name")
    TapeResponse classToTapeResponse(Class aclass);
}
