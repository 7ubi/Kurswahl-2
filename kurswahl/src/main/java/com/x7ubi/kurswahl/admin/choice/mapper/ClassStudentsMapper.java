package com.x7ubi.kurswahl.admin.choice.mapper;

import com.x7ubi.kurswahl.admin.choice.response.ClassStudentsResponse;
import com.x7ubi.kurswahl.admin.choice.response.StudentSurveillanceResponse;
import com.x7ubi.kurswahl.admin.user.mapper.TeacherMapper;
import com.x7ubi.kurswahl.common.models.ChoiceClass;
import com.x7ubi.kurswahl.common.models.Class;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {TeacherMapper.class}
)
public interface ClassStudentsMapper {

    @Mapping(source = "teacher", target = "teacherResponse")
    @Mapping(source = "choiceClasses", target = "studentSurveillanceResponses")
    @Mapping(source = "tape.name", target = "tapeName")
    @Mapping(source = "subject.name", target = "subjectName")
    ClassStudentsResponse classToClassChoiceResponse(Class aclass);

    List<ClassStudentsResponse> classesToClassChoiceResponses(List<Class> classes);


    @Mapping(source = "choice.student.user.surname", target = "surname")
    @Mapping(source = "choice.student.user.firstname", target = "firstname")
    @Mapping(source = "choice.student.user.username", target = "username")
    @Mapping(source = "choice.student.studentClass.studentClassId", target = "studentClassId")
    @Mapping(source = "choice.student.studentClass.name", target = "name")
    @Mapping(source = "choice.student.studentId", target = "studentId")
    @Mapping(source = "choice.student.user.userId", target = "userId")
    StudentSurveillanceResponse choiceToStudentSurveillanceResponse(ChoiceClass choice);
}
