package com.x7ubi.kurswahl.admin.classes.service;

import com.x7ubi.kurswahl.admin.classes.request.TapeCreationRequest;
import com.x7ubi.kurswahl.admin.classes.response.TapeResponse;
import com.x7ubi.kurswahl.admin.classes.response.TapeResponses;
import com.x7ubi.kurswahl.common.error.ErrorMessage;
import com.x7ubi.kurswahl.common.exception.EntityCreationException;
import com.x7ubi.kurswahl.common.exception.EntityNotFoundException;
import com.x7ubi.kurswahl.common.mapper.TapeMapper;
import com.x7ubi.kurswahl.common.models.Class;
import com.x7ubi.kurswahl.common.models.Tape;
import com.x7ubi.kurswahl.common.repository.ClassRepo;
import com.x7ubi.kurswahl.common.repository.TapeRepo;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TapeCreationService {

    private final Logger logger = LoggerFactory.getLogger(TapeCreationService.class);

    private final TapeMapper tapeMapper;

    private final TapeRepo tapeRepo;

    private final ClassRepo classRepo;

    private final ClassCreationService classCreationService;

    public TapeCreationService(TapeMapper tapeMapper, TapeRepo tapeRepo, ClassRepo classRepo,
                               ClassCreationService classCreationService) {
        this.tapeMapper = tapeMapper;
        this.tapeRepo = tapeRepo;
        this.classRepo = classRepo;
        this.classCreationService = classCreationService;
    }

    @Transactional
    public void createTape(TapeCreationRequest tapeCreationRequest) throws EntityCreationException {
        this.findTapeCreationError(tapeCreationRequest);

        Tape tape = this.tapeMapper.tapeRequestToTape(tapeCreationRequest);
        tape.setReleaseYear(Year.now().getValue());

        this.tapeRepo.save(tape);

        logger.info(String.format("Created tape %s", tape.getName()));
    }

    @Transactional
    public void editTape(Long tapeId, TapeCreationRequest tapeCreationRequest)
            throws EntityCreationException, EntityNotFoundException {
        Optional<Tape> tapeOptional = this.tapeRepo.findTapeByTapeId(tapeId);
        if (tapeOptional.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.TAPE_NOT_FOUND);
        }
        Tape tape = tapeOptional.get();

        if (!Objects.equals(tape.getName(), tapeCreationRequest.getName())) {
            this.findTapeCreationError(tapeCreationRequest);
        }

        this.tapeMapper.tapeRequestToTape(tapeCreationRequest, tape);

        this.tapeRepo.save(tape);

        logger.info(String.format("Edited tape %s", tape.getName()));
    }

    public TapeResponse getTape(Long tapeId) throws EntityNotFoundException {
        Optional<Tape> tapeOptional = this.tapeRepo.findTapeByTapeId(tapeId);
        if (tapeOptional.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.TAPE_NOT_FOUND);
        }
        Tape tape = tapeOptional.get();

        logger.info(String.format("Got tape %s", tape.getName()));

        return this.tapeMapper.tapeToTapeResponse(tape);
    }

    public TapeResponses getAllTapes(Integer year) {
        List<Tape> tapes = this.tapeRepo.findAllByYearAndReleaseYear(year, Year.now().getValue()).get();

        return this.tapeMapper.tapesToTapeResponses(tapes);
    }

    @Transactional
    public void deleteTape(Long tapeId) throws EntityNotFoundException {
        Optional<Tape> tapeOptional = this.tapeRepo.findTapeByTapeId(tapeId);
        if (tapeOptional.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.TAPE_NOT_FOUND);
        }
        Tape tape = tapeOptional.get();

        List<Class> classes = new ArrayList<>(tape.getaClass());
        tape.getaClass().clear();
        this.tapeRepo.save(tape);
        for (Class aclass : classes) {
            classCreationService.deleteClass(aclass.getClassId());
        }
        this.classRepo.deleteAll(tape.getaClass());

        this.tapeRepo.delete(tape);
        logger.info(String.format("Deleted tape %s", tape.getName()));
    }

    public void findTapeCreationError(TapeCreationRequest tapeCreationRequest) throws EntityCreationException {
        if (tapeRepo.existsTapeByNameAndYearAndReleaseYear(tapeCreationRequest.getName(),
                tapeCreationRequest.getYear(), Year.now().getValue())) {
            throw new EntityCreationException(ErrorMessage.TAPE_ALREADY_EXISTS);
        }
    }
}
