package com.x7ubi.kurswahl.teacher.classes.mapper;

import com.x7ubi.kurswahl.common.models.ChoiceClass;
import com.x7ubi.kurswahl.common.models.Class;
import com.x7ubi.kurswahl.teacher.classes.response.TeacherClassResponse;
import com.x7ubi.kurswahl.teacher.classes.response.TeacherClassStudentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface TeacherClassesMapper {

    List<TeacherClassResponse> mapClassesToClassResponses(List<Class> classes);

    @Mapping(source = "choiceClasses", target = "studentResponses")
    TeacherClassResponse mapClassToClassResponse(Class classEntity);

    @Mapping(source = "choice.student.user.firstname", target = "firstname")
    @Mapping(source = "choice.student.user.surname", target = "surname")
    @Mapping(source = "choice.student.studentClass.name", target = "studentClassName")
    TeacherClassStudentResponse mapChoiceClassToStudentResponse(ChoiceClass choiceClass);
}
