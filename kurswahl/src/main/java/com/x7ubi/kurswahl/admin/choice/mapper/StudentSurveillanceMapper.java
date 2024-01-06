package com.x7ubi.kurswahl.admin.choice.mapper;

import com.x7ubi.kurswahl.admin.choice.response.StudentSurveillanceResponse;
import com.x7ubi.kurswahl.common.models.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface StudentSurveillanceMapper {

    @Mapping(source = "user.surname", target = "surname")
    @Mapping(source = "user.firstname", target = "firstname")
    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "studentClass.studentClassId", target = "studentClassId")
    @Mapping(source = "studentClass.name", target = "name")
    @Mapping(source = "user.userId", target = "userId")
    StudentSurveillanceResponse studentTostudentSurveillanceResponse(Student student);
}
