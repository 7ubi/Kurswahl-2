package com.x7ubi.kurswahl.admin.classes.service;

import com.x7ubi.kurswahl.admin.classes.request.StudentClassCreationRequest;
import com.x7ubi.kurswahl.admin.classes.response.StudentClassResponses;
import com.x7ubi.kurswahl.admin.classes.response.StudentClassResultResponse;
import com.x7ubi.kurswahl.admin.user.service.AdminErrorService;
import com.x7ubi.kurswahl.common.mapper.StudentClassMapper;
import com.x7ubi.kurswahl.common.models.Student;
import com.x7ubi.kurswahl.common.models.StudentClass;
import com.x7ubi.kurswahl.common.models.Teacher;
import com.x7ubi.kurswahl.common.repository.StudentClassRepo;
import com.x7ubi.kurswahl.common.repository.StudentRepo;
import com.x7ubi.kurswahl.common.repository.TeacherRepo;
import com.x7ubi.kurswahl.common.response.ResultResponse;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.List;
import java.util.Objects;

@Service
public class StudentClassCreationService {

    Logger logger = LoggerFactory.getLogger(StudentClassCreationService.class);

    private final AdminErrorService adminErrorService;

    private final TeacherRepo teacherRepo;

    private final StudentClassRepo studentClassRepo;

    private final StudentRepo studentRepo;

    private final StudentClassMapper studentClassMapper;

    protected StudentClassCreationService(AdminErrorService adminErrorService, TeacherRepo teacherRepo,
                                          StudentClassRepo studentClassRepo, StudentRepo studentRepo,
                                          StudentClassMapper studentClassMapper) {
        this.adminErrorService = adminErrorService;
        this.teacherRepo = teacherRepo;
        this.studentClassRepo = studentClassRepo;
        this.studentRepo = studentRepo;
        this.studentClassMapper = studentClassMapper;
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
        StudentClass studentClass
                = this.studentClassMapper.studentClassRequestToStudentClass(studentClassCreationRequest);
        studentClass.setTeacher(teacher);
        studentClass.setReleaseYear(Year.now().getValue());
        this.studentClassRepo.save(studentClass);
        teacher.getStudentClasses().add(studentClass);
        this.teacherRepo.save(teacher);

        logger.info(String.format("Student class %s with teacher %s was created", studentClass.getName(),
                teacher.getUser().getUsername()));

        return response;
    }

    @Transactional
    public ResultResponse editStudentClass(Long studentClassId, StudentClassCreationRequest studentClassCreationRequest) {
        ResultResponse resultResponse = new ResultResponse();

        resultResponse.setErrorMessages(
                this.adminErrorService.getTeacherNotFound(studentClassCreationRequest.getTeacherId()));
        resultResponse.getErrorMessages().addAll(this.adminErrorService.getStudentClassNotFound(studentClassId));

        if (!resultResponse.getErrorMessages().isEmpty()) {
            return resultResponse;
        }
        StudentClass studentClass = this.studentClassRepo.findStudentClassByStudentClassId(studentClassId).get();
        if (!Objects.equals(studentClass.getName(), studentClassCreationRequest.getName())) {
            resultResponse.setErrorMessages(this.adminErrorService.findStudentClassCreationError(studentClassCreationRequest));
        }

        if (!resultResponse.getErrorMessages().isEmpty()) {
            return resultResponse;
        }

        this.studentClassMapper.studentClassRequestToStudentClass(studentClassCreationRequest, studentClass);

        if (!Objects.equals(studentClass.getTeacher().getTeacherId(), studentClassCreationRequest.getTeacherId())) {
            studentClass.getTeacher().getStudentClasses().remove(studentClass);
            this.teacherRepo.save(studentClass.getTeacher());

            Teacher teacher = this.teacherRepo.findTeacherByTeacherId(studentClassCreationRequest.getTeacherId()).get();
            teacher.getStudentClasses().add(studentClass);
            this.teacherRepo.save(teacher);

            studentClass.setTeacher(teacher);
        }

        this.studentClassRepo.save(studentClass);

        logger.info(String.format("Edited student class %s", studentClass.getName()));

        return resultResponse;
    }

    public StudentClassResultResponse getStudentClass(Long studentClassId) {
        StudentClassResultResponse response = new StudentClassResultResponse();

        response.setErrorMessages(this.adminErrorService.getStudentClassNotFound(studentClassId));

        if (!response.getErrorMessages().isEmpty()) {
            return response;
        }

        StudentClass studentClass = this.studentClassRepo.findStudentClassByStudentClassId(studentClassId).get();
        response.setStudentClassResponse(this.studentClassMapper.studentClassToStudentClassResponse(studentClass));

        logger.info(String.format("Got student class %s", studentClass.getName()));

        return response;
    }

    public StudentClassResponses getAllStudentClasses() {
        List<StudentClass> studentClasses = this.studentClassRepo.findAll();

        return this.studentClassMapper.studentClassesToStudentClassResponses(studentClasses);
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
