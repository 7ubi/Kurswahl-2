package com.x7ubi.kurswahl.student.choice.mapper;

import com.x7ubi.kurswahl.common.models.Choice;
import com.x7ubi.kurswahl.common.models.Class;
import com.x7ubi.kurswahl.student.choice.request.AlterStudentChoiceRequest;
import com.x7ubi.kurswahl.student.choice.response.ChoiceResponse;
import com.x7ubi.kurswahl.student.choice.response.ClassChoiceResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ChoiceMapper {
    Choice choiceRequestToChoice(AlterStudentChoiceRequest studentChoiceRequest);

    List<ChoiceResponse> choicesToChoiceResponses(List<Choice> choices);

    @Mapping(source = "classes", target = "classChoiceResponses")
    ChoiceResponse choiceToChoiceResponse(Choice choice);

    @Mapping(source = "tape.tapeId", target = "tapeId")
    ClassChoiceResponse classToClassChoiceResponse(Class c);
}
