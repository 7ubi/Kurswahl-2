package com.x7ubi.kurswahl.admin.classes.mapper;

import com.x7ubi.kurswahl.admin.classes.request.StudentClassCreationRequest;
import com.x7ubi.kurswahl.admin.classes.response.StudentClassResponse;
import com.x7ubi.kurswahl.admin.user.mapper.TeacherMapper;
import com.x7ubi.kurswahl.common.models.StudentClass;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {TeacherMapper.class}
)
public interface StudentClassMapper {

    StudentClass studentClassRequestToStudentClass(StudentClassCreationRequest studentClassCreationRequest);

    void studentClassRequestToStudentClass(StudentClassCreationRequest studentClassCreationRequest, @MappingTarget StudentClass studentClass);

    StudentClassResponse studentClassToStudentClassResponse(StudentClass studentClass);

    List<StudentClassResponse> studentClassListToStudentClassResponseList(List<StudentClass> studentClass);
}
