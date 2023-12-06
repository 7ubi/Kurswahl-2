package com.x7ubi.kurswahl.common.mapper;

import com.x7ubi.kurswahl.admin.request.TeacherSignupRequest;
import com.x7ubi.kurswahl.admin.response.user.TeacherResponse;
import com.x7ubi.kurswahl.admin.response.user.TeacherResponses;
import com.x7ubi.kurswahl.common.models.Teacher;
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
    Teacher teacherRequestToTeacher(TeacherSignupRequest teacherSignupRequest);

    @Mapping(source = "surname", target = "user.surname")
    @Mapping(source = "firstname", target = "user.firstname")
    void teacherRequestToTeacher(TeacherSignupRequest teacherSignupRequest, @MappingTarget Teacher teacher);

    default TeacherResponses teachersToTeacherResponses(List<Teacher> teachers) {
        TeacherResponses teacherResponses = new TeacherResponses();
        teacherResponses.setTeacherResponses(teachersToTeacherResponse(teachers));
        return teacherResponses;
    }

    List<TeacherResponse> teachersToTeacherResponse(List<Teacher> teachers);

    @Mapping(source = "user.surname", target = "surname")
    @Mapping(source = "user.firstname", target = "firstname")
    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "user.generatedPassword", target = "generatedPassword")
    @Mapping(source = "user.userId", target = "userId")
    TeacherResponse teacherToTeacherResponse(Teacher teacher);
}
