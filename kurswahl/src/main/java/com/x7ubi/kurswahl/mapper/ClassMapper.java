package com.x7ubi.kurswahl.mapper;

import com.x7ubi.kurswahl.models.Class;
import com.x7ubi.kurswahl.request.admin.ClassCreationRequest;
import com.x7ubi.kurswahl.response.admin.classes.ClassResponse;
import com.x7ubi.kurswahl.response.admin.classes.ClassResponses;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {TeacherMapper.class, TapeMapper.class, SubjectMapper.class}
)
public interface ClassMapper {
    Class classRequestToClass(ClassCreationRequest classCreationRequest);

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
