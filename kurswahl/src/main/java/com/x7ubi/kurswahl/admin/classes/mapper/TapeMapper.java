package com.x7ubi.kurswahl.admin.classes.mapper;

import com.x7ubi.kurswahl.admin.classes.request.TapeCreationRequest;
import com.x7ubi.kurswahl.admin.classes.response.TapeResponse;
import com.x7ubi.kurswahl.common.models.Tape;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {LessonMapper.class}
)
public interface TapeMapper {
    Tape tapeRequestToTape(TapeCreationRequest tapeCreationRequest);

    void tapeRequestToTape(TapeCreationRequest tapeCreationRequest, @MappingTarget Tape tape);

    List<TapeResponse> tapesToTapeResponseList(List<Tape> tapes);

    @Mapping(source = "lessons", target = "lessonResponses")
    TapeResponse tapeToTapeResponse(Tape tape);
}
