package com.x7ubi.kurswahl.admin.classes.mapper;

import com.x7ubi.kurswahl.admin.classes.request.LessonCreationRequest;
import com.x7ubi.kurswahl.admin.classes.response.LessonResponse;
import com.x7ubi.kurswahl.common.models.Lesson;
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
