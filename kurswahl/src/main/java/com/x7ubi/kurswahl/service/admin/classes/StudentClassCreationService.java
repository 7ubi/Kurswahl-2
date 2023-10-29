package com.x7ubi.kurswahl.service.admin.classes;

import com.x7ubi.kurswahl.models.StudentClass;
import com.x7ubi.kurswahl.request.admin.StudentClassCreationRequest;
import com.x7ubi.kurswahl.response.common.ResultResponse;
import com.x7ubi.kurswahl.service.admin.AdminErrorService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class StudentClassCreationService {

    private final AdminErrorService adminErrorService;

    private final ModelMapper modelMapper = new ModelMapper();

    protected StudentClassCreationService(AdminErrorService adminErrorService) {
        this.adminErrorService = adminErrorService;
    }

    @Transactional
    public ResultResponse createStudentClass(StudentClassCreationRequest studentClassCreationRequest) {
        ResultResponse response = new ResultResponse();

        response.setErrorMessages(this.adminErrorService.findStudentClassCreationError(studentClassCreationRequest));
        response.getErrorMessages().addAll(
                this.adminErrorService.getTeacherNotFound(studentClassCreationRequest.getTeacherId()));

        if (!response.getErrorMessages().isEmpty()) {
            return response;
        }

        //Teacher teacher = this.
        StudentClass studentClass = new StudentClass();
        studentClass = this.modelMapper.map(studentClassCreationRequest, StudentClass.class);


        return response;
    }
}
