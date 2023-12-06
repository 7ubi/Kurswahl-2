package com.x7ubi.kurswahl.common.mapper;

import com.x7ubi.kurswahl.admin.request.StudentClassCreationRequest;
import com.x7ubi.kurswahl.admin.response.classes.StudentClassResponse;
import com.x7ubi.kurswahl.admin.response.classes.StudentClassResponses;
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

    default StudentClassResponses studentClassesToStudentClassResponses(List<StudentClass> studentClasses) {

        StudentClassResponses studentClassResponses = new StudentClassResponses();
        studentClassResponses.setStudentClassResponses(studentClassListToStudentClassResponseList(studentClasses));
        return studentClassResponses;
    }

    StudentClassResponse studentClassToStudentClassResponse(StudentClass studentClass);

    List<StudentClassResponse> studentClassListToStudentClassResponseList(List<StudentClass> studentClass);
}
