package com.x7ubi.kurswahl.mapper;

import com.x7ubi.kurswahl.models.StudentClass;
import com.x7ubi.kurswahl.response.admin.classes.StudentClassResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface StudentClassMapper {
    StudentClassResponse studentClassToStudentClassResponse(StudentClass studentClass);

    List<StudentClassResponse> studentClassToStudentClassResponse(List<StudentClass> studentClass);
}
