package com.x7ubi.kurswahl.student.choice.mapper;

import com.x7ubi.kurswahl.common.models.Choice;
import com.x7ubi.kurswahl.student.choice.request.AlterStudentChoiceRequest;
import com.x7ubi.kurswahl.student.choice.response.ChoiceResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ChoiceMapper {
    Choice choiceRequestToChoice(AlterStudentChoiceRequest studentChoiceRequest);

    @Mapping(source = "classes", target = "classResponses")
    ChoiceResponse choiceToChoiceResponse(Choice choice);
}
