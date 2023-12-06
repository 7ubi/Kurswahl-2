package com.x7ubi.kurswahl.common.mapper;

import com.x7ubi.kurswahl.admin.request.ClassCreationRequest;
import com.x7ubi.kurswahl.admin.response.classes.ClassResponse;
import com.x7ubi.kurswahl.admin.response.classes.ClassResponses;
import com.x7ubi.kurswahl.common.models.Class;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {TeacherMapper.class, TapeMapper.class, SubjectMapper.class}
)
public interface ClassMapper {
    Class classRequestToClass(ClassCreationRequest classCreationRequest);

    void classRequestToClass(ClassCreationRequest classCreationRequest, @MappingTarget Class aclass);

    default ClassResponses classesToClassResponses(List<Class> classes) {
        ClassResponses classResponses = new ClassResponses();
        classResponses.setClassResponses(classesToClassResponseList(classes));
        return classResponses;
    }

    List<ClassResponse> classesToClassResponseList(List<Class> classes);

    @Mapping(source = "teacher", target = "teacherResponse")
    @Mapping(source = "tape", target = "tapeResponse")
    @Mapping(source = "subject", target = "subjectResponse")
    ClassResponse classToClassResponse(Class aclass);
}
