package com.x7ubi.kurswahl.service.admin.classes;

import com.x7ubi.kurswahl.mapper.TapeMapper;
import com.x7ubi.kurswahl.models.Tape;
import com.x7ubi.kurswahl.repository.TapeRepo;
import com.x7ubi.kurswahl.request.admin.TapeCreationRequest;
import com.x7ubi.kurswahl.response.common.ResultResponse;
import com.x7ubi.kurswahl.service.admin.AdminErrorService;
import org.springframework.stereotype.Service;

import java.time.Year;

@Service
public class TapeCreationService {

    private final AdminErrorService adminErrorService;

    private final TapeMapper tapeMapper;

    private final TapeRepo tapeRepo;

    public TapeCreationService(AdminErrorService adminErrorService, TapeMapper tapeMapper, TapeRepo tapeRepo) {
        this.adminErrorService = adminErrorService;
        this.tapeMapper = tapeMapper;
        this.tapeRepo = tapeRepo;
    }

    public ResultResponse createTape(TapeCreationRequest tapeCreationRequest) {
        ResultResponse response = new ResultResponse();

        response.setErrorMessages(this.adminErrorService.findTapeCreationError(tapeCreationRequest));

        if (!response.getErrorMessages().isEmpty()) {
            return response;
        }

        Tape tape = this.tapeMapper.tapeRequestToTape(tapeCreationRequest);
        tape.setReleaseYear(Year.now().getValue());

        this.tapeRepo.save(tape);

        return response;
    }
}
