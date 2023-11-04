package com.x7ubi.kurswahl.service.admin.classes;

import com.x7ubi.kurswahl.request.admin.TapeCreationRequest;
import com.x7ubi.kurswahl.response.common.ResultResponse;
import org.springframework.stereotype.Service;

@Service
public class TapeCreationService {

    protected TapeCreationService() {
    }

    public ResultResponse createTape(TapeCreationRequest tapeCreationRequest) {
        ResultResponse response = new ResultResponse();

        return response;
    }
}
