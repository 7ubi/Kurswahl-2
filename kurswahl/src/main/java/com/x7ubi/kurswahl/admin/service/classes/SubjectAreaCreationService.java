package com.x7ubi.kurswahl.admin.service.classes;

import com.x7ubi.kurswahl.admin.request.SubjectAreaCreationRequest;
import com.x7ubi.kurswahl.admin.response.classes.SubjectAreaResponse;
import com.x7ubi.kurswahl.admin.response.classes.SubjectAreaResponses;
import com.x7ubi.kurswahl.common.error.ErrorMessage;
import com.x7ubi.kurswahl.common.exception.EntityCreationException;
import com.x7ubi.kurswahl.common.exception.EntityNotFoundException;
import com.x7ubi.kurswahl.common.mapper.SubjectAreaMapper;
import com.x7ubi.kurswahl.common.models.Subject;
import com.x7ubi.kurswahl.common.models.SubjectArea;
import com.x7ubi.kurswahl.common.repository.SubjectAreaRepo;
import com.x7ubi.kurswahl.common.repository.SubjectRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class SubjectAreaCreationService {
    private final Logger logger = LoggerFactory.getLogger(SubjectAreaCreationService.class);

    private final SubjectAreaRepo subjectAreaRepo;

    private final SubjectRepo subjectRepo;

    private final SubjectAreaMapper subjectAreaMapper;

    public SubjectAreaCreationService(SubjectAreaRepo subjectAreaRepo, SubjectRepo subjectRepo,
                                      SubjectAreaMapper subjectAreaMapper) {
        this.subjectAreaRepo = subjectAreaRepo;
        this.subjectRepo = subjectRepo;
        this.subjectAreaMapper = subjectAreaMapper;
    }

    public void createSubjectArea(SubjectAreaCreationRequest subjectAreaCreationRequest)
            throws EntityCreationException {
        this.findSubjectAreaCreationError(subjectAreaCreationRequest);

        SubjectArea subjectArea = this.subjectAreaMapper.subjectAreaRequestToSubject(subjectAreaCreationRequest);
        this.subjectAreaRepo.save(subjectArea);

        logger.info(String.format("Subject Area %s was created", subjectArea.getName()));
    }

    public void editSubjectArea(Long subjectAreaId, SubjectAreaCreationRequest subjectAreaCreationRequest)
            throws EntityCreationException, EntityNotFoundException {
        Optional<SubjectArea> subjectAreaOptional = this.subjectAreaRepo.findSubjectAreaBySubjectAreaId(subjectAreaId);

        if (subjectAreaOptional.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.Administration.SUBJECT_AREA_NOT_FOUND);
        }

        SubjectArea subjectArea = subjectAreaOptional.get();

        if (!Objects.equals(subjectArea.getName(), subjectAreaCreationRequest.getName())) {
            this.findSubjectAreaCreationError(subjectAreaCreationRequest);
        }

        this.subjectAreaMapper.subjectAreaRequestToSubject(subjectAreaCreationRequest, subjectArea);
        this.subjectAreaRepo.save(subjectArea);

        logger.info(String.format("Subject Area %s was edited", subjectArea.getName()));
    }

    public SubjectAreaResponses getAllSubjectAreas() {

        List<SubjectArea> subjectAreas = this.subjectAreaRepo.findAll();

        return this.subjectAreaMapper.subjectAreasToSubjectAreaResponses(subjectAreas);
    }

    public void deleteSubjectArea(Long subjectAreaId) throws EntityNotFoundException {
        Optional<SubjectArea> subjectAreaOptional = this.subjectAreaRepo.findSubjectAreaBySubjectAreaId(subjectAreaId);

        if (subjectAreaOptional.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.Administration.SUBJECT_AREA_NOT_FOUND);
        }

        SubjectArea subjectArea = subjectAreaOptional.get();

        List<Subject> subjects = new ArrayList<>(subjectArea.getSubjects());

        subjectArea.getSubjects().clear();
        this.subjectAreaRepo.save(subjectArea);
        this.subjectRepo.deleteAll(subjects);
        this.subjectAreaRepo.delete(subjectArea);

        logger.info(String.format("Deleted subject area %s", subjectArea.getName()));
    }

    public SubjectAreaResponse getSubjectArea(Long subjectAreaId) throws EntityNotFoundException {
        Optional<SubjectArea> subjectAreaOptional = this.subjectAreaRepo.findSubjectAreaBySubjectAreaId(subjectAreaId);

        if (subjectAreaOptional.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.Administration.SUBJECT_AREA_NOT_FOUND);
        }

        SubjectArea subjectArea = subjectAreaOptional.get();

        logger.info(String.format("Found Subject Area %s", subjectArea.getName()));

        return this.subjectAreaMapper.subjectAreaToSubjectAreaResponse(subjectArea);
    }

    private void findSubjectAreaCreationError(SubjectAreaCreationRequest subjectAreaCreationRequest)
            throws EntityCreationException {

        if (this.subjectAreaRepo.existsSubjectAreaByName(subjectAreaCreationRequest.getName())) {
            throw new EntityCreationException(ErrorMessage.Administration.SUBJECT_AREA_ALREADY_EXISTS);
        }
    }
}