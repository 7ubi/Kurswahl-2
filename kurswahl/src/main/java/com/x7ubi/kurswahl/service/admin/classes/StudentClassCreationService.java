package com.x7ubi.kurswahl.service.admin.classes;

import com.x7ubi.kurswahl.models.Student;
import com.x7ubi.kurswahl.models.StudentClass;
import com.x7ubi.kurswahl.models.Teacher;
import com.x7ubi.kurswahl.repository.StudentClassRepo;
import com.x7ubi.kurswahl.repository.StudentRepo;
import com.x7ubi.kurswahl.repository.TeacherRepo;
import com.x7ubi.kurswahl.request.admin.StudentClassCreationRequest;
import com.x7ubi.kurswahl.response.admin.classes.StudentClassResponse;
import com.x7ubi.kurswahl.response.admin.classes.StudentClassResponses;
import com.x7ubi.kurswahl.response.admin.user.TeacherResponse;
import com.x7ubi.kurswahl.response.common.ResultResponse;
import com.x7ubi.kurswahl.service.admin.AdminErrorService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

@Service
public class StudentClassCreationService {

    Logger logger = LoggerFactory.getLogger(StudentClassCreationService.class);

    private final AdminErrorService adminErrorService;

    private final TeacherRepo teacherRepo;

    private final StudentClassRepo studentClassRepo;

    private final StudentRepo studentRepo;

    private final ModelMapper modelMapper = new ModelMapper();

    protected StudentClassCreationService(AdminErrorService adminErrorService, TeacherRepo teacherRepo,
                                          StudentClassRepo studentClassRepo, StudentRepo studentRepo) {
        this.adminErrorService = adminErrorService;
        this.teacherRepo = teacherRepo;
        this.studentClassRepo = studentClassRepo;
        this.studentRepo = studentRepo;
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
        studentClass.setStudentClassId(null);
        studentClass.setTeacher(teacher);
        studentClass.setReleaseYear(Year.now().getValue());
        this.studentClassRepo.save(studentClass);
        teacher.getStudentClasses().add(studentClass);
        this.teacherRepo.save(teacher);

        logger.info(String.format("Student class %s with teacher %s was created", studentClass.getName(),
                teacher.getUser().getUsername()));

        return response;
    }

    public StudentClassResponses getAllStudentClasses() {
        List<StudentClass> studentClasses = this.studentClassRepo.findAll();
        StudentClassResponses studentClassResponses = new StudentClassResponses();
        studentClassResponses.setStudentClassResponses(new ArrayList<>());

        for (StudentClass studentClass : studentClasses) {
            StudentClassResponse studentClassResponse = this.modelMapper.map(studentClass, StudentClassResponse.class);
            studentClassResponse.setTeacher(this.modelMapper.map(studentClass.getTeacher().getUser(), TeacherResponse.class));
            studentClassResponse.getTeacher().setTeacherId(studentClass.getTeacher().getTeacherId());
            studentClassResponse.getTeacher().setAbbreviation(studentClass.getTeacher().getAbbreviation());
            studentClassResponses.getStudentClassResponses().add(studentClassResponse);
        }

        return studentClassResponses;
    }

    @Transactional
    public ResultResponse deleteStudentClass(Long studentClassId) {
        ResultResponse response = new ResultResponse();

        response.setErrorMessages(this.adminErrorService.getStudentClassNotFound(studentClassId));

        if (!response.getErrorMessages().isEmpty()) {
            return response;
        }

        StudentClass studentClass = this.studentClassRepo.findStudentClassByStudentClassId(studentClassId).get();
        studentClass.getTeacher().getStudentClasses().remove(studentClass);
        for (Student student : studentClass.getStudents()) {
            student.setStudentClass(null);
            this.studentRepo.save(student);
        }
        this.studentClassRepo.delete(studentClass);

        logger.info(String.format("Student Class %s was deleted", studentClass.getName()));

        return response;
    }
}
