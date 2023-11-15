package com.x7ubi.kurswahl.mapper;

import com.x7ubi.kurswahl.models.Teacher;
import com.x7ubi.kurswahl.request.admin.TeacherSignupRequest;
import com.x7ubi.kurswahl.response.admin.user.TeacherResponse;
import com.x7ubi.kurswahl.response.admin.user.TeacherResponses;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface TeacherMapper {

    @Mapping(source = "surname", target = "user.surname")
    @Mapping(source = "firstname", target = "user.firstname")
    Teacher studentRequestToStudent(TeacherSignupRequest teacherSignupRequest);

    @Mapping(source = "surname", target = "user.surname")
    @Mapping(source = "firstname", target = "user.firstname")
    void studentRequestToStudent(TeacherSignupRequest teacherSignupRequest, @MappingTarget Teacher teacher);

    default TeacherResponses studentsToStudentResponses(List<Teacher> teachers) {
        TeacherResponses teacherResponses = new TeacherResponses();
        teacherResponses.setTeacherResponses(teachersToTeacherResponse(teachers));
        return teacherResponses;
    }

    List<TeacherResponse> teachersToTeacherResponse(List<Teacher> teachers);

    @Mapping(source = "user.surname", target = "surname")
    @Mapping(source = "user.firstname", target = "firstname")
    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "user.generatedPassword", target = "generatedPassword")
    TeacherResponse studentToStudentResponse(Teacher teacher);
}
