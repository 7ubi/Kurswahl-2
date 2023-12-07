package com.x7ubi.kurswahl.common.mapper;

import com.x7ubi.kurswahl.admin.user.request.StudentSignupRequest;
import com.x7ubi.kurswahl.admin.user.response.StudentResponse;
import com.x7ubi.kurswahl.admin.user.response.StudentResponses;
import com.x7ubi.kurswahl.common.models.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {StudentClassMapper.class}
)
public interface StudentMapper {

    @Mapping(source = "surname", target = "user.surname")
    @Mapping(source = "firstname", target = "user.firstname")
    Student studentRequestToStudent(StudentSignupRequest studentSignupRequest);


    @Mapping(source = "surname", target = "user.surname")
    @Mapping(source = "firstname", target = "user.firstname")
    void studentRequestToStudent(StudentSignupRequest studentSignupRequest, @MappingTarget Student student);

    default StudentResponses studentsToStudentResponses(List<Student> students) {
        StudentResponses studentResponses = new StudentResponses();
        studentResponses.setStudentResponses(studentsToStudentResponseList(students));
        return studentResponses;
    }

    List<StudentResponse> studentsToStudentResponseList(List<Student> students);

    @Mapping(source = "user.surname", target = "surname")
    @Mapping(source = "user.firstname", target = "firstname")
    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "user.generatedPassword", target = "generatedPassword")
    @Mapping(source = "studentClass", target = "studentClassResponse")
    @Mapping(source = "user.userId", target = "userId")
    StudentResponse studentToStudentResponse(Student student);
}
