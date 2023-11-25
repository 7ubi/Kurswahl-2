package com.x7ubi.kurswahl.service.admin.classes;

import com.x7ubi.kurswahl.mapper.TapeMapper;
import com.x7ubi.kurswahl.models.Tape;
import com.x7ubi.kurswahl.repository.TapeRepo;
import com.x7ubi.kurswahl.request.admin.TapeCreationRequest;
import com.x7ubi.kurswahl.response.admin.classes.TapeResponses;
import com.x7ubi.kurswahl.response.common.ResultResponse;
import com.x7ubi.kurswahl.service.admin.AdminErrorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.List;

@Service
public class TapeCreationService {

    private final Logger logger = LoggerFactory.getLogger(TapeCreationService.class);

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

        logger.info(String.format("Created tape %s", tape.getName()));

        return response;
    }

    public TapeResponses getAllTapes() {
        List<Tape> tapes = this.tapeRepo.findAll();

        return this.tapeMapper.tapesToTapeResponses(tapes);
    }

    public ResultResponse deleteTape(Long tapeId) {
        ResultResponse response = new ResultResponse();

        response.setErrorMessages(this.adminErrorService.getTapeNotFound(tapeId));

        if (!response.getErrorMessages().isEmpty()) {
            return response;
        }

        Tape tape = this.tapeRepo.findTapeByTapeId(tapeId).get();
        this.tapeRepo.delete(tape);
        logger.info(String.format("Deleted tape %s", tape.getName()));

        return response;
    }
}
