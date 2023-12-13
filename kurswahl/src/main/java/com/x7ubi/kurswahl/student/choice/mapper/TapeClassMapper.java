package com.x7ubi.kurswahl.student.choice.mapper;

import com.x7ubi.kurswahl.common.models.Tape;
import com.x7ubi.kurswahl.student.choice.response.TapeResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface TapeClassMapper {

    List<TapeResponse> tapesToTapeResponses(List<Tape> tape);

    @Mapping(source = "aClass", target = "classResponses")
    @Mapping(source = "lessons", target = "lessonResponses")
    TapeResponse tapeToTapeResponse(Tape tape);
}
