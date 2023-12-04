package com.x7ubi.kurswahl.mapper;

import com.x7ubi.kurswahl.models.Choice;
import com.x7ubi.kurswahl.request.student.AlterStudentChoiceRequest;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ChoiceMapper {
    Choice choiceRequestToChoice(AlterStudentChoiceRequest studentChoiceRequest);
}
