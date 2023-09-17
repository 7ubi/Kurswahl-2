package com.x7ubi.kurswahl.service.admin.classes;

import com.x7ubi.kurswahl.error.ErrorMessage;
import com.x7ubi.kurswahl.repository.SubjectAreaRepo;
import com.x7ubi.kurswahl.repository.SubjectRepo;
import com.x7ubi.kurswahl.request.admin.SubjectAreaCreationRequest;
import com.x7ubi.kurswahl.request.admin.SubjectCreationRequest;
import com.x7ubi.kurswahl.response.common.MessageResponse;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractClassesCreationService {

    private final Logger logger = LoggerFactory.getLogger(AbstractClassesCreationService.class);

    protected final SubjectAreaRepo subjectAreaRepo;

    protected final SubjectRepo subjectRepo;

    protected final ModelMapper modelMapper = new ModelMapper();

    protected AbstractClassesCreationService(SubjectAreaRepo subjectAreaRepo, SubjectRepo subjectRepo) {
        this.subjectAreaRepo = subjectAreaRepo;
        this.subjectRepo = subjectRepo;
    }

    protected List<MessageResponse> findSubjectAreaCreationError(
            SubjectAreaCreationRequest subjectAreaCreationRequest) {
        List<MessageResponse> errors = new ArrayList<>();

        if(this.subjectAreaRepo.existsSubjectAreaByName(subjectAreaCreationRequest.getName())) {
            logger.error(ErrorMessage.Administration.SUBJECT_AREA_ALREADY_EXISTS);
            errors.add(new MessageResponse(ErrorMessage.Administration.SUBJECT_AREA_ALREADY_EXISTS));
        }

        return errors;
    }

    protected List<MessageResponse> getSubjectAreaNotFound(Long subjectAreaId) {
        List<MessageResponse> errors = new ArrayList<>();

        if(!this.subjectAreaRepo.existsSubjectAreaBySubjectAreaId(subjectAreaId)) {
            logger.error(ErrorMessage.Administration.SUBJECT_AREA_NOT_FOUND);
            errors.add(new MessageResponse(ErrorMessage.Administration.SUBJECT_AREA_NOT_FOUND));
        }

        return errors;
    }

    protected List<MessageResponse> findSubjectCreationError(SubjectCreationRequest subjectCreationRequest) {
        List<MessageResponse> errors = new ArrayList<>();

        if(this.subjectRepo.existsSubjectByName(subjectCreationRequest.getName())) {
            logger.error(ErrorMessage.Administration.SUBJECT_ALREADY_EXISTS);
            errors.add(new MessageResponse(ErrorMessage.Administration.SUBJECT_ALREADY_EXISTS));
        }

        return errors;
    }

    protected List<MessageResponse> getSubjectNotFound(Long subjectId) {
        List<MessageResponse> errors = new ArrayList<>();

        if(!this.subjectRepo.existsSubjectAreaBySubjectId(subjectId)) {
            logger.error(ErrorMessage.Administration.SUBJECT_NOT_FOUND);
            errors.add(new MessageResponse(ErrorMessage.Administration.SUBJECT_NOT_FOUND));
        }

        return errors;
    }
}
