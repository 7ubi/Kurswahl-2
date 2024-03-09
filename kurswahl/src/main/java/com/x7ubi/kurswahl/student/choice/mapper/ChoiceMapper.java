package com.x7ubi.kurswahl.student.choice.mapper;

import com.x7ubi.kurswahl.common.models.Choice;
import com.x7ubi.kurswahl.common.models.ChoiceClass;
import com.x7ubi.kurswahl.student.choice.request.AlterStudentChoiceRequest;
import com.x7ubi.kurswahl.student.choice.response.ChoiceResponse;
import com.x7ubi.kurswahl.student.choice.response.ClassChoiceResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.Set;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ChoiceMapper {
    Choice choiceRequestToChoice(AlterStudentChoiceRequest studentChoiceRequest);

    List<ChoiceResponse> choicesToChoiceResponses(List<Choice> choices);

    @Mapping(source = "choiceClasses", target = "classChoiceResponses")
    ChoiceResponse choiceToChoiceResponse(Choice choice);

    List<ClassChoiceResponse> choiceClassSetToClassChoiceResponseList(Set<ChoiceClass> set);

    @Mapping(source = "aClass.tape.tapeId", target = "tapeId")
    @Mapping(source = "aClass.name", target = "name")
    @Mapping(source = "aClass.classId", target = "classId")
    ClassChoiceResponse classToClassChoiceResponse(ChoiceClass c);
}
