package com.x7ubi.kurswahl.service.admin.classes;

import com.x7ubi.kurswahl.models.Subject;
import com.x7ubi.kurswahl.models.SubjectArea;
import com.x7ubi.kurswahl.repository.SubjectAreaRepo;
import com.x7ubi.kurswahl.repository.SubjectRepo;
import com.x7ubi.kurswahl.request.admin.SubjectCreationRequest;
import com.x7ubi.kurswahl.response.admin.classes.SubjectAreaResponse;
import com.x7ubi.kurswahl.response.admin.classes.SubjectResponse;
import com.x7ubi.kurswahl.response.admin.classes.SubjectResponses;
import com.x7ubi.kurswahl.response.common.ResultResponse;
import com.x7ubi.kurswahl.service.admin.AdminErrorService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SubjectCreationService {

    private final Logger logger = LoggerFactory.getLogger(SubjectCreationService.class);

    private final AdminErrorService adminErrorService;

    private final SubjectAreaRepo subjectAreaRepo;

    private final SubjectRepo subjectRepo;

    private final ModelMapper modelMapper = new ModelMapper();

    protected SubjectCreationService(AdminErrorService adminErrorService, SubjectAreaRepo subjectAreaRepo,
                                     SubjectRepo subjectRepo) {
        this.adminErrorService = adminErrorService;
        this.subjectAreaRepo = subjectAreaRepo;
        this.subjectRepo = subjectRepo;
    }

    @Transactional
    public ResultResponse createSubject(SubjectCreationRequest subjectCreationRequest) {
        ResultResponse resultResponse = new ResultResponse();

        resultResponse.setErrorMessages(this.adminErrorService.findSubjectCreationError(subjectCreationRequest));
        resultResponse.getErrorMessages().addAll(
                this.adminErrorService.getSubjectAreaNotFound(subjectCreationRequest.getSubjectAreaId()));

        if(!resultResponse.getErrorMessages().isEmpty()) {
            return resultResponse;
        }

        SubjectArea subjectArea
                = this.subjectAreaRepo.findSubjectAreaBySubjectAreaId(subjectCreationRequest.getSubjectAreaId()).get();
        Subject subject = new Subject();
        subject.setName(subjectCreationRequest.getName());
        subject.setSubjectArea(subjectArea);
        this.subjectRepo.save(subject);
        subjectArea.getSubjects().add(subject);
        this.subjectAreaRepo.save(subjectArea);

        logger.info(String.format("Subject %s was created", subjectArea.getName()));

        return resultResponse;
    }

    public SubjectResponses getAllSubjects() {
        SubjectResponses subjectResponses = new SubjectResponses();
        subjectResponses.setSubjectResponses(new ArrayList<>());

        List<Subject> subjects = this.subjectRepo.findAll();

        subjects.forEach(subject -> {
            SubjectResponse subjectResponse = modelMapper.map(subject, SubjectResponse.class);
            subjectResponse.setSubjectAreaResponse(modelMapper.map(subject.getSubjectArea(), SubjectAreaResponse.class));
            subjectResponses.getSubjectResponses().add(subjectResponse);
        });

        return subjectResponses;
    }

    public ResultResponse deleteSubject(Long subjectId) {
        ResultResponse resultResponse = new ResultResponse();
        resultResponse.setErrorMessages(this.adminErrorService.getSubjectNotFound(subjectId));

        if(!resultResponse.getErrorMessages().isEmpty()) {
            return resultResponse;
        }

        Subject subject = this.subjectRepo.findSubjectBySubjectId(subjectId).get();
        subject.getSubjectArea().getSubjects().remove(subject);
        this.subjectAreaRepo.save(subject.getSubjectArea());

        return resultResponse;
    }
}
