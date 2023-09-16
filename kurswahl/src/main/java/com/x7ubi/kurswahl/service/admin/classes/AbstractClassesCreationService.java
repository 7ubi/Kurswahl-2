package com.x7ubi.kurswahl.service.admin.classes;

import com.x7ubi.kurswahl.error.ErrorMessage;
import com.x7ubi.kurswahl.repository.SubjectAreaRepo;
import com.x7ubi.kurswahl.request.admin.SubjectAreaCreationRequest;
import com.x7ubi.kurswahl.response.common.MessageResponse;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractClassesCreationService {

    private final Logger logger = LoggerFactory.getLogger(AbstractClassesCreationService.class);

    protected final SubjectAreaRepo subjectAreaRepo;

    protected final ModelMapper modelMapper = new ModelMapper();

    protected AbstractClassesCreationService(SubjectAreaRepo subjectAreaRepo) {
        this.subjectAreaRepo = subjectAreaRepo;
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

}
