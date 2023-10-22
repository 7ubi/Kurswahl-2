package com.x7ubi.kurswahl.service.admin.classes;

import com.x7ubi.kurswahl.models.SubjectArea;
import com.x7ubi.kurswahl.repository.SubjectAreaRepo;
import com.x7ubi.kurswahl.repository.SubjectRepo;
import com.x7ubi.kurswahl.request.admin.SubjectAreaCreationRequest;
import com.x7ubi.kurswahl.response.admin.classes.SubjectAreaResponse;
import com.x7ubi.kurswahl.response.admin.classes.SubjectAreaResponses;
import com.x7ubi.kurswahl.response.common.ResultResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SubjectAreaCreationService extends AbstractClassesCreationService {
    private final Logger logger = LoggerFactory.getLogger(SubjectAreaCreationService.class);

    public SubjectAreaCreationService(SubjectAreaRepo subjectAreaRepo, SubjectRepo subjectRepo) {
        super(subjectAreaRepo, subjectRepo);
    }

    public ResultResponse createSubjectArea(SubjectAreaCreationRequest subjectAreaCreationRequest) {
        ResultResponse resultResponse = new ResultResponse();

        resultResponse.setErrorMessages(this.findSubjectAreaCreationError(subjectAreaCreationRequest));

        if(!resultResponse.getErrorMessages().isEmpty()) {
            return resultResponse;
        }

        SubjectArea subjectArea = new SubjectArea();
        subjectArea.setName(subjectAreaCreationRequest.getName());
        this.subjectAreaRepo.save(subjectArea);

        logger.info(String.format("Subject Area %s was created", subjectArea.getName()));

        return resultResponse;
    }

    public SubjectAreaResponses getAllSubjectAreas() {
        SubjectAreaResponses subjectAreaResponses = new SubjectAreaResponses();
        subjectAreaResponses.setSubjectAreaResponses(new ArrayList<>());

        List<SubjectArea> subjectAreas = this.subjectAreaRepo.findAll();

        subjectAreas.forEach(subjectArea -> subjectAreaResponses.getSubjectAreaResponses()
                .add(modelMapper.map(subjectArea, SubjectAreaResponse.class)));

        return subjectAreaResponses;
    }

    public ResultResponse deleteSubjectArea(Long subjectAreaId) {
        ResultResponse resultResponse = new ResultResponse();
        resultResponse.setErrorMessages(this.getSubjectAreaNotFound(subjectAreaId));

        if(!resultResponse.getErrorMessages().isEmpty()) {
            return resultResponse;
        }

        SubjectArea subjectArea = this.subjectAreaRepo.findSubjectAreaBySubjectAreaId(subjectAreaId).get();
        this.subjectAreaRepo.delete(subjectArea);

        return resultResponse;
    }
}
