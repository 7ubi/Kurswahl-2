package com.x7ubi.kurswahl.admin.service.user;

import com.x7ubi.kurswahl.admin.request.TeacherSignupRequest;
import com.x7ubi.kurswahl.admin.response.user.TeacherResponses;
import com.x7ubi.kurswahl.admin.response.user.TeacherResultResponse;
import com.x7ubi.kurswahl.admin.service.AdminErrorService;
import com.x7ubi.kurswahl.common.error.ErrorMessage;
import com.x7ubi.kurswahl.common.mapper.TeacherMapper;
import com.x7ubi.kurswahl.common.models.Teacher;
import com.x7ubi.kurswahl.common.models.User;
import com.x7ubi.kurswahl.common.repository.TeacherRepo;
import com.x7ubi.kurswahl.common.repository.UserRepo;
import com.x7ubi.kurswahl.common.response.MessageResponse;
import com.x7ubi.kurswahl.common.response.ResultResponse;
import com.x7ubi.kurswahl.common.utils.PasswordGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TeacherCreationService {

    Logger logger = LoggerFactory.getLogger(TeacherCreationService.class);

    private final AdminErrorService adminErrorService;

    private final TeacherRepo teacherRepo;

    private final UserRepo userRepo;

    private final PasswordEncoder passwordEncoder;

    private final UsernameService usernameService;

    private final TeacherMapper teacherMapper;

    public TeacherCreationService(AdminErrorService adminErrorService, TeacherRepo teacherRepo, UserRepo userRepo,
                                  PasswordEncoder passwordEncoder, UsernameService usernameService,
                                  TeacherMapper teacherMapper) {
        this.adminErrorService = adminErrorService;
        this.teacherRepo = teacherRepo;
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.usernameService = usernameService;
        this.teacherMapper = teacherMapper;
    }

    public void registerTeacher(TeacherSignupRequest teacherSignupRequest) {

        Teacher teacher = this.teacherMapper.teacherRequestToTeacher(teacherSignupRequest);
        teacher.getUser().setUsername(this.usernameService.generateUsernameFromName(teacherSignupRequest));
        teacher.getUser().setGeneratedPassword(PasswordGenerator.generatePassword());
        teacher.getUser().setPassword(passwordEncoder.encode(teacher.getUser().getGeneratedPassword()));

        this.teacherRepo.save(teacher);

        logger.info(String.format("Teacher %s was created", teacher.getUser().getUsername()));
    }

    public ResultResponse editTeacher(Long teacherId, TeacherSignupRequest teacherSignupRequest) {
        ResultResponse resultResponse = new ResultResponse();

        resultResponse.setErrorMessages(this.adminErrorService.getTeacherNotFound(teacherId));

        if (!resultResponse.getErrorMessages().isEmpty()) {
            return resultResponse;
        }

        Teacher teacher = this.teacherRepo.findTeacherByTeacherId(teacherId).get();
        this.teacherMapper.teacherRequestToTeacher(teacherSignupRequest, teacher);
        this.teacherRepo.save(teacher);

        logger.info(String.format("Edited Teacher %s", teacher.getUser().getUsername()));

        return resultResponse;
    }

    public TeacherResponses getAllTeachers() {
        List<Teacher> teachers = this.teacherRepo.findAll();

        return this.teacherMapper.teachersToTeacherResponses(teachers);
    }

    public ResultResponse deleteTeacher(Long teacherId) {
        ResultResponse resultResponse = new ResultResponse();

        resultResponse.setErrorMessages(this.adminErrorService.getTeacherNotFound(teacherId));
        resultResponse.getErrorMessages().addAll(getStudentClassesTeacher(teacherId));
        resultResponse.getErrorMessages().addAll(getClassesTeacher(teacherId));

        if(!resultResponse.getErrorMessages().isEmpty()) {
            return resultResponse;
        }

        Teacher teacher = this.teacherRepo.findTeacherByTeacherId(teacherId).get();
        User teacherUser = teacher.getUser();

        logger.info(String.format("Deleted Teacher %s", teacherUser.getUsername()));

        this.teacherRepo.delete(teacher);
        this.userRepo.delete(teacherUser);

        return resultResponse;
    }

    private List<MessageResponse> getStudentClassesTeacher(Long teacherId) {
        List<MessageResponse> error = new ArrayList<>();

        Optional<Teacher> teacherOptional = this.teacherRepo.findTeacherByTeacherId(teacherId);

        if (teacherOptional.isEmpty()) {
            return error;
        }
        Teacher teacher = teacherOptional.get();

        if (!teacher.getStudentClasses().isEmpty()) {
            logger.error(ErrorMessage.Administration.TEACHER_STUDENT_CLASS);
            error.add(new MessageResponse(ErrorMessage.Administration.TEACHER_STUDENT_CLASS));
        }

        return error;
    }

    private List<MessageResponse> getClassesTeacher(Long teacherId) {
        List<MessageResponse> error = new ArrayList<>();

        Optional<Teacher> teacherOptional = this.teacherRepo.findTeacherByTeacherId(teacherId);

        if (teacherOptional.isEmpty()) {
            return error;
        }
        Teacher teacher = teacherOptional.get();

        if (!teacher.getClasses().isEmpty()) {
            logger.error(ErrorMessage.Administration.TEACHER_CLASS);
            error.add(new MessageResponse(ErrorMessage.Administration.TEACHER_CLASS));
        }

        return error;
    }

    public TeacherResultResponse getTeacher(Long teacherId) {
        TeacherResultResponse teacherResultResponse = new TeacherResultResponse();

        teacherResultResponse.setErrorMessages(this.adminErrorService.getTeacherNotFound(teacherId));

        if (!teacherResultResponse.getErrorMessages().isEmpty()) {
            return teacherResultResponse;
        }

        Teacher teacher = this.teacherRepo.findTeacherByTeacherId(teacherId).get();
        teacherResultResponse.setTeacherResponse(this.teacherMapper.teacherToTeacherResponse(teacher));

        logger.info(String.format("Got Teacher %s", teacher.getUser().getUsername()));

        return teacherResultResponse;
    }
}