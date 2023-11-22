package com.x7ubi.kurswahl.service.admin.classes;

import com.x7ubi.kurswahl.mapper.SubjectAreaMapper;
import com.x7ubi.kurswahl.models.Subject;
import com.x7ubi.kurswahl.models.SubjectArea;
import com.x7ubi.kurswahl.repository.SubjectAreaRepo;
import com.x7ubi.kurswahl.repository.SubjectRepo;
import com.x7ubi.kurswahl.request.admin.SubjectAreaCreationRequest;
import com.x7ubi.kurswahl.response.admin.classes.SubjectAreaResponses;
import com.x7ubi.kurswahl.response.admin.classes.SubjectAreaResultResponse;
import com.x7ubi.kurswahl.response.common.ResultResponse;
import com.x7ubi.kurswahl.service.admin.AdminErrorService;
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

    public ResultResponse createSubjectArea(SubjectAreaCreationRequest subjectAreaCreationRequest) {
        ResultResponse resultResponse = new ResultResponse();

        resultResponse.setErrorMessages(this.adminErrorService.findSubjectAreaCreationError(subjectAreaCreationRequest));

        if(!resultResponse.getErrorMessages().isEmpty()) {
            return resultResponse;
        }

        SubjectArea subjectArea = this.subjectAreaMapper.subjectAreaRequestToSubject(subjectAreaCreationRequest);
        this.subjectAreaRepo.save(subjectArea);

        logger.info(String.format("Subject Area %s was created", subjectArea.getName()));

        return resultResponse;
    }

    public ResultResponse editSubjectArea(Long subjectAreaId, SubjectAreaCreationRequest subjectAreaCreationRequest) {
        ResultResponse resultResponse = new ResultResponse();

        resultResponse.setErrorMessages(this.adminErrorService.getSubjectAreaNotFound(subjectAreaId));

        if (!resultResponse.getErrorMessages().isEmpty()) {
            return resultResponse;
        }

        SubjectArea subjectArea = this.subjectAreaRepo.findSubjectAreaBySubjectAreaId(subjectAreaId).get();
        if (!Objects.equals(subjectArea.getName(), subjectAreaCreationRequest.getName())) {
            resultResponse.setErrorMessages(this.adminErrorService.findSubjectAreaCreationError(subjectAreaCreationRequest));
        }

        if (!resultResponse.getErrorMessages().isEmpty()) {
            return resultResponse;
        }

        this.subjectAreaMapper.subjectAreaRequestToSubject(subjectAreaCreationRequest, subjectArea);
        this.subjectAreaRepo.save(subjectArea);

        return resultResponse;
    }

    public SubjectAreaResponses getAllSubjectAreas() {

        List<SubjectArea> subjectAreas = this.subjectAreaRepo.findAll();

        return this.subjectAreaMapper.subjectAreasToSubjectAreaResponses(subjectAreas);
    }

    public ResultResponse deleteSubjectArea(Long subjectAreaId) {
        ResultResponse resultResponse = new ResultResponse();
        resultResponse.setErrorMessages(this.adminErrorService.getSubjectAreaNotFound(subjectAreaId));

        if(!resultResponse.getErrorMessages().isEmpty()) {
            return resultResponse;
        }

        SubjectArea subjectArea = this.subjectAreaRepo.findSubjectAreaBySubjectAreaId(subjectAreaId).get();

        List<Subject> subjects = new ArrayList<>(subjectArea.getSubjects());

        subjectArea.getSubjects().clear();
        this.subjectAreaRepo.save(subjectArea);
        this.subjectRepo.deleteAll(subjects);
        this.subjectAreaRepo.delete(subjectArea);

        logger.info(String.format("Deleted subject area %s", subjectArea.getName()));

        return resultResponse;
    }

    public SubjectAreaResultResponse getSubjectArea(Long subjectAreaId) {
        SubjectAreaResultResponse response = new SubjectAreaResultResponse();
        response.setErrorMessages(this.adminErrorService.getSubjectAreaNotFound(subjectAreaId));

        if (!response.getErrorMessages().isEmpty()) {
            return response;
        }

        SubjectArea subjectArea = this.subjectAreaRepo.findSubjectAreaBySubjectAreaId(subjectAreaId).get();
        response.setSubjectAreaResponse(this.subjectAreaMapper.subjectAreaToSubjectAreaResponse(subjectArea));

        return response;
    }
}
