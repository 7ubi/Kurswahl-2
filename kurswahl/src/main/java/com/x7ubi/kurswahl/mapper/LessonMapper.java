package com.x7ubi.kurswahl.mapper;

import com.x7ubi.kurswahl.models.Lesson;
import com.x7ubi.kurswahl.request.admin.LessonCreationRequest;
import com.x7ubi.kurswahl.response.admin.classes.LessonResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface LessonMapper {

    Lesson lessonRequestToLesson(LessonCreationRequest lessonCreationRequest);

    LessonResponse lessonToLessonResponse(Lesson lesson);

    List<LessonResponse> lessonSetToLessonResponseList(List<Lesson> lessons);
}
