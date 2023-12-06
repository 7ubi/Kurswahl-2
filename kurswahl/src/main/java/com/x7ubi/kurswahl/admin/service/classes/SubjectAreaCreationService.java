package com.x7ubi.kurswahl.admin.service.classes;

import com.x7ubi.kurswahl.admin.request.SubjectAreaCreationRequest;
import com.x7ubi.kurswahl.admin.response.classes.SubjectAreaResponse;
import com.x7ubi.kurswahl.admin.response.classes.SubjectAreaResponses;
import com.x7ubi.kurswahl.admin.service.AdminErrorService;
import com.x7ubi.kurswahl.common.exception.CreationException;
import com.x7ubi.kurswahl.common.exception.ObjectNotFoundException;
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

@Service
public class SubjectAreaCreationService {
    private final Logger logger = LoggerFactory.getLogger(SubjectAreaCreationService.class);

    private final AdminErrorService adminErrorService;

    private final SubjectAreaRepo subjectAreaRepo;

    private final SubjectRepo subjectRepo;

    private final SubjectAreaMapper subjectAreaMapper;

    public SubjectAreaCreationService(AdminErrorService adminErrorService, SubjectAreaRepo subjectAreaRepo,
                                      SubjectRepo subjectRepo, SubjectAreaMapper subjectAreaMapper) {
        this.adminErrorService = adminErrorService;
        this.subjectAreaRepo = subjectAreaRepo;
        this.subjectRepo = subjectRepo;
        this.subjectAreaMapper = subjectAreaMapper;
    }

    public void createSubjectArea(SubjectAreaCreationRequest subjectAreaCreationRequest)
            throws CreationException {
        this.adminErrorService.findSubjectAreaCreationError(subjectAreaCreationRequest);

        SubjectArea subjectArea = this.subjectAreaMapper.subjectAreaRequestToSubject(subjectAreaCreationRequest);
        this.subjectAreaRepo.save(subjectArea);

        logger.info(String.format("Subject Area %s was created", subjectArea.getName()));
    }

    public void editSubjectArea(Long subjectAreaId, SubjectAreaCreationRequest subjectAreaCreationRequest)
            throws CreationException, ObjectNotFoundException {
        this.adminErrorService.getSubjectAreaNotFound(subjectAreaId);

        SubjectArea subjectArea = this.subjectAreaRepo.findSubjectAreaBySubjectAreaId(subjectAreaId).get();
        if (!Objects.equals(subjectArea.getName(), subjectAreaCreationRequest.getName())) {
            this.adminErrorService.findSubjectAreaCreationError(subjectAreaCreationRequest);
        }

        this.subjectAreaMapper.subjectAreaRequestToSubject(subjectAreaCreationRequest, subjectArea);
        this.subjectAreaRepo.save(subjectArea);

        logger.info(String.format("Subject Area %s was edited", subjectArea.getName()));
    }

    public SubjectAreaResponses getAllSubjectAreas() {

        List<SubjectArea> subjectAreas = this.subjectAreaRepo.findAll();

        return this.subjectAreaMapper.subjectAreasToSubjectAreaResponses(subjectAreas);
    }

    public void deleteSubjectArea(Long subjectAreaId) throws ObjectNotFoundException {
        this.adminErrorService.getSubjectAreaNotFound(subjectAreaId);

        SubjectArea subjectArea = this.subjectAreaRepo.findSubjectAreaBySubjectAreaId(subjectAreaId).get();

        List<Subject> subjects = new ArrayList<>(subjectArea.getSubjects());

        subjectArea.getSubjects().clear();
        this.subjectAreaRepo.save(subjectArea);
        this.subjectRepo.deleteAll(subjects);
        this.subjectAreaRepo.delete(subjectArea);

        logger.info(String.format("Deleted subject area %s", subjectArea.getName()));
    }

    public SubjectAreaResponse getSubjectArea(Long subjectAreaId) throws ObjectNotFoundException {
        this.adminErrorService.getSubjectAreaNotFound(subjectAreaId);

        SubjectArea subjectArea = this.subjectAreaRepo.findSubjectAreaBySubjectAreaId(subjectAreaId).get();

        logger.info(String.format("Found Subject Area %s", subjectArea.getName()));

        return this.subjectAreaMapper.subjectAreaToSubjectAreaResponse(subjectArea);
    }
}
