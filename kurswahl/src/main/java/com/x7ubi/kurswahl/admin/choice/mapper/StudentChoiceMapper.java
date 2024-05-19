package com.x7ubi.kurswahl.admin.choice.mapper;

import com.x7ubi.kurswahl.admin.choice.response.ChoiceResponse;
import com.x7ubi.kurswahl.admin.choice.response.ClassChoiceResponse;
import com.x7ubi.kurswahl.admin.choice.response.StudentChoicesResponse;
import com.x7ubi.kurswahl.admin.choice.response.StudentRuleResponse;
import com.x7ubi.kurswahl.admin.user.mapper.TeacherMapper;
import com.x7ubi.kurswahl.common.models.Choice;
import com.x7ubi.kurswahl.common.models.ChoiceClass;
import com.x7ubi.kurswahl.common.models.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.Set;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {TeacherMapper.class}
)
public interface StudentChoiceMapper {

    @Mapping(source = "user.surname", target = "surname")
    @Mapping(source = "user.firstname", target = "firstname")
    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "studentClass.studentClassId", target = "studentClassId")
    @Mapping(source = "studentClass.name", target = "name")
    @Mapping(source = "studentClass.year", target = "year")
    @Mapping(source = "user.userId", target = "userId")
    @Mapping(source = "studentId", target = "studentId")
    @Mapping(source = "choices", target = "choiceResponses")
    StudentChoicesResponse studentToStudentChoicesResponse(Student student);

    @Mapping(source = "choiceClasses", target = "classChoiceResponses")
    ChoiceResponse choiceToChoiceResponse(Choice choice);

    List<ClassChoiceResponse> choiceClassSetToClassChoiceResponseList(Set<ChoiceClass> source);

    @Mapping(source = "aClass.tape.tapeId", target = "tapeId")
    @Mapping(source = "aClass.teacher", target = "teacherResponse")
    @Mapping(source = "aClass.classId", target = "classId")
    @Mapping(source = "aClass.name", target = "name")
    ClassChoiceResponse classToClassChoiceResponse(ChoiceClass choiceClass);

    @Mapping(source = "user.surname", target = "surname")
    @Mapping(source = "user.firstname", target = "firstname")
    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "studentClass.studentClassId", target = "studentClassId")
    @Mapping(source = "studentClass.name", target = "name")
    @Mapping(source = "studentClass.year", target = "year")
    @Mapping(source = "user.userId", target = "userId")
    @Mapping(source = "studentId", target = "studentId")
    StudentRuleResponse studentToStudentRuleResponse(Student student);
}
