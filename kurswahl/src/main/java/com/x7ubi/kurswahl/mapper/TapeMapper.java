package com.x7ubi.kurswahl.mapper;

import com.x7ubi.kurswahl.models.Tape;
import com.x7ubi.kurswahl.request.admin.TapeCreationRequest;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface TapeMapper {
    Tape tapeRequestToTape(TapeCreationRequest tapeCreationRequest);
}
