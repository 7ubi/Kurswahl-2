package com.x7ubi.kurswahl.admin.choice.mapper;

import com.x7ubi.kurswahl.admin.choice.response.ChoiceResponse;
import com.x7ubi.kurswahl.admin.choice.response.ClassChoiceResponse;
import com.x7ubi.kurswahl.admin.user.mapper.TeacherMapper;
import com.x7ubi.kurswahl.common.models.Choice;
import com.x7ubi.kurswahl.common.models.Class;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {StudentSurveillanceMapper.class, TeacherMapper.class}
)
public interface ClassChoiceMapper {

    @Mapping(source = "teacher", target = "teacherResponse")
    @Mapping(source = "choices", target = "choiceResponses")
    ClassChoiceResponse classToClassChoiceResponse(Class aclass);

    List<ClassChoiceResponse> classesToClassChoiceResponses(List<Class> classes);

    @Mapping(source = "student", target = "studentSurveillanceResponse")
    ChoiceResponse choiceToChoiceResponse(Choice choice);
}
