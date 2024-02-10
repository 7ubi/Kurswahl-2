package com.x7ubi.kurswahl.admin.user.service;

import com.x7ubi.kurswahl.admin.user.mapper.TeacherMapper;
import com.x7ubi.kurswahl.admin.user.request.TeacherSignupRequest;
import com.x7ubi.kurswahl.admin.user.response.TeacherResponse;
import com.x7ubi.kurswahl.common.auth.utils.PasswordGenerator;
import com.x7ubi.kurswahl.common.error.ErrorMessage;
import com.x7ubi.kurswahl.common.exception.EntityDependencyException;
import com.x7ubi.kurswahl.common.exception.EntityNotFoundException;
import com.x7ubi.kurswahl.common.models.Teacher;
import com.x7ubi.kurswahl.common.models.User;
import com.x7ubi.kurswahl.common.repository.TeacherRepo;
import com.x7ubi.kurswahl.common.repository.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeacherCreationService {

    Logger logger = LoggerFactory.getLogger(TeacherCreationService.class);

    private final TeacherRepo teacherRepo;

    private final UserRepo userRepo;

    private final PasswordEncoder passwordEncoder;

    private final UsernameService usernameService;

    private final TeacherMapper teacherMapper;

    public TeacherCreationService(TeacherRepo teacherRepo, UserRepo userRepo, PasswordEncoder passwordEncoder,
                                  UsernameService usernameService, TeacherMapper teacherMapper) {
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

    public void editTeacher(Long teacherId, TeacherSignupRequest teacherSignupRequest) throws EntityNotFoundException {
        Teacher teacher = getTeacherFromTeacherId(teacherId);
        this.teacherMapper.teacherRequestToTeacher(teacherSignupRequest, teacher);
        this.teacherRepo.save(teacher);

        logger.info(String.format("Edited Teacher %s", teacher.getUser().getUsername()));
    }

    public List<TeacherResponse> getAllTeachers() {
        List<Teacher> teachers = this.teacherRepo.findAll();

        return this.teacherMapper.teachersToTeacherResponse(teachers);
    }

    public TeacherResponse getTeacher(Long teacherId) throws EntityNotFoundException {
        Teacher teacher = getTeacherFromTeacherId(teacherId);

        logger.info(String.format("Got Teacher %s", teacher.getUser().getUsername()));

        return this.teacherMapper.teacherToTeacherResponse(teacher);
    }

    public List<TeacherResponse> deleteTeacher(Long teacherId) throws EntityNotFoundException, EntityDependencyException {
        deleteTeacherHelper(teacherId);

        return getAllTeachers();
    }

    private void deleteTeacherHelper(Long teacherId) throws EntityNotFoundException, EntityDependencyException {
        Teacher teacher = getTeacherFromTeacherId(teacherId);
        getStudentClassesTeacher(teacher);
        getClassesTeacher(teacher);
        User teacherUser = teacher.getUser();

        logger.info(String.format("Deleted Teacher %s", teacherUser.getUsername()));

        this.teacherRepo.delete(teacher);
        this.userRepo.delete(teacherUser);
    }

    private void getStudentClassesTeacher(Teacher teacher) throws EntityDependencyException {
        if (!teacher.getStudentClasses().isEmpty()) {
            throw new EntityDependencyException(ErrorMessage.TEACHER_STUDENT_CLASS);
        }
    }

    private void getClassesTeacher(Teacher teacher) throws EntityDependencyException {
        if (!teacher.getClasses().isEmpty()) {
            throw new EntityDependencyException(ErrorMessage.TEACHER_CLASS);
        }
    }

    private Teacher getTeacherFromTeacherId(Long teacherId) throws EntityNotFoundException {
        Optional<Teacher> teacherOptional = this.teacherRepo.findTeacherByTeacherId(teacherId);
        if (teacherOptional.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.TEACHER_NOT_FOUND);
        }
        return teacherOptional.get();
    }

    public List<TeacherResponse> deleteTeachers(List<Long> teacherIds)
            throws EntityDependencyException, EntityNotFoundException {
        for (Long teacherId : teacherIds) {
            this.deleteTeacherHelper(teacherId);
        }

        return getAllTeachers();
    }
}
