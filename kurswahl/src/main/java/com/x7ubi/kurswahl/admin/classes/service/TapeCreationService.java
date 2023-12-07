package com.x7ubi.kurswahl.admin.classes.service;

import com.x7ubi.kurswahl.admin.classes.request.TapeCreationRequest;
import com.x7ubi.kurswahl.admin.classes.response.TapeResponses;
import com.x7ubi.kurswahl.admin.classes.response.TapeResultResponse;
import com.x7ubi.kurswahl.admin.user.service.AdminErrorService;
import com.x7ubi.kurswahl.common.mapper.TapeMapper;
import com.x7ubi.kurswahl.common.models.Class;
import com.x7ubi.kurswahl.common.models.Tape;
import com.x7ubi.kurswahl.common.repository.ClassRepo;
import com.x7ubi.kurswahl.common.repository.TapeRepo;
import com.x7ubi.kurswahl.common.response.ResultResponse;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class TapeCreationService {

    private final Logger logger = LoggerFactory.getLogger(TapeCreationService.class);

    private final AdminErrorService adminErrorService;

    private final TapeMapper tapeMapper;

    private final TapeRepo tapeRepo;

    private final ClassRepo classRepo;

    private final ClassCreationService classCreationService;

    public TapeCreationService(AdminErrorService adminErrorService, TapeMapper tapeMapper, TapeRepo tapeRepo,
                               ClassRepo classRepo, ClassCreationService classCreationService) {
        this.adminErrorService = adminErrorService;
        this.tapeMapper = tapeMapper;
        this.tapeRepo = tapeRepo;
        this.classRepo = classRepo;
        this.classCreationService = classCreationService;
    }

    @Transactional
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

    @Transactional
    public ResultResponse editTape(Long tapeId, TapeCreationRequest tapeCreationRequest) {
        ResultResponse response = new ResultResponse();

        response.setErrorMessages(this.adminErrorService.getTapeNotFound(tapeId));

        if (!response.getErrorMessages().isEmpty()) {
            return response;
        }

        Tape tape = this.tapeRepo.findTapeByTapeId(tapeId).get();

        if (!Objects.equals(tape.getName(), tapeCreationRequest.getName())) {
            response.setErrorMessages(this.adminErrorService.findTapeCreationError(tapeCreationRequest));
        }

        if (!response.getErrorMessages().isEmpty()) {
            return response;
        }

        this.tapeMapper.tapeRequestToTape(tapeCreationRequest, tape);

        this.tapeRepo.save(tape);

        logger.info(String.format("Edited tape %s", tape.getName()));

        return response;
    }

    public TapeResultResponse getTape(Long tapeId) {
        TapeResultResponse response = new TapeResultResponse();

        response.setErrorMessages(this.adminErrorService.getTapeNotFound(tapeId));

        if (!response.getErrorMessages().isEmpty()) {
            return response;
        }

        Tape tape = this.tapeRepo.findTapeByTapeId(tapeId).get();
        response.setTapeResponse(this.tapeMapper.tapeToTapeResponse(tape));
        logger.info(String.format("Got tape %s", tape.getName()));

        return response;
    }

    public TapeResponses getAllTapes(Integer year) {
        List<Tape> tapes = this.tapeRepo.findAllByYearAndReleaseYear(year, Year.now().getValue()).get();

        return this.tapeMapper.tapesToTapeResponses(tapes);
    }

    @Transactional
    public ResultResponse deleteTape(Long tapeId) {
        ResultResponse response = new ResultResponse();

        response.setErrorMessages(this.adminErrorService.getTapeNotFound(tapeId));

        if (!response.getErrorMessages().isEmpty()) {
            return response;
        }

        Tape tape = this.tapeRepo.findTapeByTapeId(tapeId).get();
        List<Class> classes = new ArrayList<>(tape.getaClass());
        tape.getaClass().clear();
        this.tapeRepo.save(tape);
        for (Class aclass : classes) {
            classCreationService.deleteClass(aclass.getClassId());
        }
        this.classRepo.deleteAll(tape.getaClass());

        this.tapeRepo.delete(tape);
        logger.info(String.format("Deleted tape %s", tape.getName()));

        return response;
    }
}
