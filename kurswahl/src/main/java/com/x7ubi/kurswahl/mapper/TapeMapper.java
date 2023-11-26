package com.x7ubi.kurswahl.mapper;

import com.x7ubi.kurswahl.models.Tape;
import com.x7ubi.kurswahl.request.admin.TapeCreationRequest;
import com.x7ubi.kurswahl.response.admin.classes.TapeResponse;
import com.x7ubi.kurswahl.response.admin.classes.TapeResponses;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface TapeMapper {
    Tape tapeRequestToTape(TapeCreationRequest tapeCreationRequest);

    void tapeRequestToTape(TapeCreationRequest tapeCreationRequest, @MappingTarget Tape tape);

    default TapeResponses tapesToTapeResponses(List<Tape> tapes) {
        TapeResponses tapeResponses = new TapeResponses();
        tapeResponses.setTapeResponses(tapesToTapeResponseList(tapes));
        return tapeResponses;
    }

    List<TapeResponse> tapesToTapeResponseList(List<Tape> tapes);

    TapeResponse tapeToTapeResponse(Tape tape);
}
