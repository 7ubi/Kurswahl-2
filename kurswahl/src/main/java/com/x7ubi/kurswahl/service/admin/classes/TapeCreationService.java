package com.x7ubi.kurswahl.service.admin.classes;

import com.x7ubi.kurswahl.repository.SubjectAreaRepo;
import com.x7ubi.kurswahl.repository.SubjectRepo;
import com.x7ubi.kurswahl.request.admin.TapeCreationRequest;
import com.x7ubi.kurswahl.response.common.ResultResponse;
import org.springframework.stereotype.Service;

@Service
public class TapeCreationService extends AbstractClassesCreationService {

    protected TapeCreationService(SubjectAreaRepo subjectAreaRepo, SubjectRepo subjectRepo) {
        super(subjectAreaRepo, subjectRepo);
    }

    public ResultResponse createTape(TapeCreationRequest tapeCreationRequest) {
        ResultResponse response = new ResultResponse();

        return response;
    }
}
