package com.x7ubi.kurswahl.admin.choice.mapper;


import com.x7ubi.kurswahl.admin.choice.response.ChoiceTapeClassResponse;
import com.x7ubi.kurswahl.admin.choice.response.ChoiceTapeResponse;
import com.x7ubi.kurswahl.admin.user.mapper.TeacherMapper;
import com.x7ubi.kurswahl.common.models.Class;
import com.x7ubi.kurswahl.common.models.Tape;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {TeacherMapper.class}
)
public interface ChoiceTapeMapper {
    List<ChoiceTapeResponse> tapesToChoiceTapeResponses(List<Tape> tapes);

    @Mapping(source = "aClass", target = "choiceTapeClassResponses")
    ChoiceTapeResponse tapeToChoiceTapeResponse(Tape tape);

    @Mapping(source = "teacher", target = "teacherResponse")
    ChoiceTapeClassResponse classToChoiceTapeClassResponse(Class aClass);
}
