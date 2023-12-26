package com.x7ubi.kurswahl.admin.classes.service;

import com.x7ubi.kurswahl.admin.classes.mapper.SubjectMapper;
import com.x7ubi.kurswahl.admin.classes.request.SubjectCreationRequest;
import com.x7ubi.kurswahl.admin.classes.response.SubjectResponse;
import com.x7ubi.kurswahl.common.error.ErrorMessage;
import com.x7ubi.kurswahl.common.exception.EntityCreationException;
import com.x7ubi.kurswahl.common.exception.EntityNotFoundException;
import com.x7ubi.kurswahl.common.models.Class;
import com.x7ubi.kurswahl.common.models.Subject;
import com.x7ubi.kurswahl.common.models.SubjectArea;
import com.x7ubi.kurswahl.common.repository.SubjectAreaRepo;
import com.x7ubi.kurswahl.common.repository.SubjectRepo;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class SubjectCreationService {

    private final Logger logger = LoggerFactory.getLogger(SubjectCreationService.class);
    private final SubjectAreaRepo subjectAreaRepo;

    private final SubjectRepo subjectRepo;

    private final SubjectMapper subjectMapper;

    private final ClassCreationService classCreationService;

    protected SubjectCreationService(SubjectAreaRepo subjectAreaRepo, SubjectRepo subjectRepo,
                                     SubjectMapper subjectMapper, ClassCreationService classCreationService) {
        this.subjectAreaRepo = subjectAreaRepo;
        this.subjectRepo = subjectRepo;
        this.subjectMapper = subjectMapper;
        this.classCreationService = classCreationService;
    }

    @Transactional
    public void createSubject(SubjectCreationRequest subjectCreationRequest) throws EntityCreationException,
            EntityNotFoundException {

        this.findSubjectCreationError(subjectCreationRequest);

        SubjectArea subjectArea = getSubjectArea(subjectCreationRequest);

        Subject subject = this.subjectMapper.subjectRequestToSubject(subjectCreationRequest);
        subject.setSubjectArea(subjectArea);
        this.subjectRepo.save(subject);
        subjectArea.getSubjects().add(subject);
        this.subjectAreaRepo.save(subjectArea);

        logger.info(String.format("Subject %s was created", subjectArea.getName()));
    }

    public void editSubject(Long subjectId, SubjectCreationRequest subjectCreationRequest)
            throws EntityNotFoundException, EntityCreationException {
        SubjectArea subjectArea = getSubjectArea(subjectCreationRequest);

        Subject subject = getSubjectFromId(subjectId);

        if (!Objects.equals(subject.getName(), subjectCreationRequest.getName())) {
            this.findSubjectCreationError(subjectCreationRequest);
        }

        this.subjectMapper.subjectRequestToSubject(subjectCreationRequest, subject);

        if (!Objects.equals(subject.getSubjectArea().getSubjectAreaId(), subjectCreationRequest.getSubjectAreaId())) {
            subject.getSubjectArea().getSubjects().remove(subject);
            this.subjectAreaRepo.save(subject.getSubjectArea());

            subjectArea.getSubjects().add(subject);
            subject.setSubjectArea(subjectArea);
            this.subjectAreaRepo.save(subjectArea);
        }

        this.subjectRepo.save(subject);
        logger.info(String.format("Edited subject %s", subject.getName()));
    }

    public List<SubjectResponse> getAllSubjects() {
        List<Subject> subjects = this.subjectRepo.findAll();

        return this.subjectMapper.subjectsToSubjectResponseList(subjects);
    }

    public List<SubjectResponse> deleteSubject(Long subjectId) throws EntityNotFoundException {
        deleteSubjectHelper(subjectId);

        return getAllSubjects();
    }

    private void deleteSubjectHelper(Long subjectId) throws EntityNotFoundException {
        Subject subject = getSubjectFromId(subjectId);

        subject.getSubjectArea().getSubjects().remove(subject);
        this.subjectAreaRepo.save(subject.getSubjectArea());
        List<Class> classes = new ArrayList<>(subject.getClasses());
        subject.getClasses().clear();
        this.subjectRepo.save(subject);
        for (Class aclass : classes) {
            classCreationService.deleteClass(aclass.getClassId());
        }
        this.subjectRepo.delete(subject);

        logger.info(String.format("Deleted subject %s", subject.getName()));
    }

    public SubjectResponse getSubject(Long subjectId) throws EntityNotFoundException {
        Subject subject = getSubjectFromId(subjectId);

        return this.subjectMapper.subjectToSubjectResponse(subject);
    }

    private Subject getSubjectFromId(Long subjectId) throws EntityNotFoundException {
        Optional<Subject> subjectOptional = this.subjectRepo.findSubjectBySubjectId(subjectId);
        if (subjectOptional.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.SUBJECT_NOT_FOUND);
        }
        return subjectOptional.get();
    }

    private void findSubjectCreationError(SubjectCreationRequest subjectCreationRequest) throws EntityCreationException {
        if (this.subjectRepo.existsSubjectByName(subjectCreationRequest.getName())) {
            throw new EntityCreationException(ErrorMessage.SUBJECT_ALREADY_EXISTS);
        }
    }

    public List<SubjectResponse> deleteSubjects(List<Long> subjectIds) throws EntityNotFoundException {
        for (Long subjectId : subjectIds) {
            deleteSubjectHelper(subjectId);
        }

        return getAllSubjects();
    }

    private SubjectArea getSubjectArea(SubjectCreationRequest subjectCreationRequest) throws EntityNotFoundException {
        Optional<SubjectArea> subjectAreaOptional = this.subjectAreaRepo.findSubjectAreaBySubjectAreaId(
                subjectCreationRequest.getSubjectAreaId());
        if (subjectAreaOptional.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.SUBJECT_AREA_NOT_FOUND);
        }
        return subjectAreaOptional.get();
    }
}
