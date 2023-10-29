package com.x7ubi.kurswahl.service.admin.classes;

import com.x7ubi.kurswahl.models.StudentClass;
import com.x7ubi.kurswahl.models.Teacher;
import com.x7ubi.kurswahl.repository.StudentClassRepo;
import com.x7ubi.kurswahl.repository.TeacherRepo;
import com.x7ubi.kurswahl.request.admin.StudentClassCreationRequest;
import com.x7ubi.kurswahl.response.common.ResultResponse;
import com.x7ubi.kurswahl.service.admin.AdminErrorService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Year;

@Service
public class StudentClassCreationService {

    Logger logger = LoggerFactory.getLogger(StudentClassCreationService.class);

    private final AdminErrorService adminErrorService;

    private final TeacherRepo teacherRepo;

    private final StudentClassRepo studentClassRepo;

    private final ModelMapper modelMapper = new ModelMapper();

    protected StudentClassCreationService(AdminErrorService adminErrorService, TeacherRepo teacherRepo, StudentClassRepo studentClassRepo) {
        this.adminErrorService = adminErrorService;
        this.teacherRepo = teacherRepo;
        this.studentClassRepo = studentClassRepo;
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

        Teacher teacher = this.teacherRepo.findTeacherByTeacherId(studentClassCreationRequest.getTeacherId()).get();
        StudentClass studentClass = this.modelMapper.map(studentClassCreationRequest, StudentClass.class);
        studentClass.setTeacher(teacher);
        studentClass.setReleaseYear(Year.now().getValue());

        teacher.getStudentClasses().add(studentClass);

        this.studentClassRepo.save(studentClass);
        this.teacherRepo.save(teacher);

        logger.info(String.format("Student class %s with teacher %s was created", studentClass.getName(),
                teacher.getUser().getUsername()));

        return response;
    }
}
