package com.x7ubi.kurswahl.mapper;

import com.x7ubi.kurswahl.models.StudentClass;
import com.x7ubi.kurswahl.request.admin.StudentClassCreationRequest;
import com.x7ubi.kurswahl.response.admin.classes.StudentClassResponse;
import com.x7ubi.kurswahl.response.admin.classes.StudentClassResponses;
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
