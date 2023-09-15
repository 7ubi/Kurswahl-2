package com.x7ubi.kurswahl.service.admin;

import com.x7ubi.kurswahl.models.Teacher;
import com.x7ubi.kurswahl.models.User;
import com.x7ubi.kurswahl.repository.AdminRepo;
import com.x7ubi.kurswahl.repository.StudentRepo;
import com.x7ubi.kurswahl.repository.TeacherRepo;
import com.x7ubi.kurswahl.repository.UserRepo;
import com.x7ubi.kurswahl.request.admin.TeacherSignupRequest;
import com.x7ubi.kurswahl.response.admin.TeacherResponse;
import com.x7ubi.kurswahl.response.admin.TeacherResponses;
import com.x7ubi.kurswahl.response.common.ResultResponse;
import com.x7ubi.kurswahl.utils.PasswordGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TeacherCreationService extends AbstractUserCreationService {

    Logger logger = LoggerFactory.getLogger(TeacherCreationService.class);

    protected TeacherCreationService(UserRepo userRepo, AdminRepo adminRepo, StudentRepo studentRepo,
                                     TeacherRepo teacherRepo, PasswordEncoder passwordEncoder) {
        super(userRepo, adminRepo, studentRepo, teacherRepo, passwordEncoder);
    }

    public ResultResponse registerTeacher(TeacherSignupRequest teacherSignupRequest) {
        ResultResponse resultResponse = new ResultResponse();

        resultResponse.setErrorMessages(this.findRegisterErrors(teacherSignupRequest));

        if(!resultResponse.getErrorMessages().isEmpty()) {
            return resultResponse;
        }

        Teacher teacher = new Teacher();
        teacher.setUser(this.mapper.map(teacherSignupRequest, User.class));
        teacher.getUser().setGeneratedPassword(PasswordGenerator.generatePassword());
        teacher.getUser().setPassword(passwordEncoder.encode(teacher.getUser().getGeneratedPassword()));

        this.teacherRepo.save(teacher);

        logger.info(String.format("Teacher %s was created", teacher.getUser().getUsername()));

        return resultResponse;
    }

    public TeacherResponses getAllTeachers() {
        TeacherResponses teacherResponses = new TeacherResponses();
        teacherResponses.setTeacherResponses(new ArrayList<>());

        List<Teacher> teachers = this.teacherRepo.findAll();
        for(Teacher teacher: teachers) {
            TeacherResponse teacherResponse = this.mapper.map(teacher.getUser(), TeacherResponse.class);
            teacherResponse.setTeacherId(teacher.getTeacherId());
            teacherResponses.getTeacherResponses().add(teacherResponse);
        }

        return teacherResponses;
    }

    public ResultResponse deleteTeacher(Long teacherId) {
        ResultResponse resultResponse = new ResultResponse();

        resultResponse.setErrorMessages(this.getTeacherNotFound(teacherId));

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
}
