package com.x7ubi.kurswahl.service.admin.user;

import com.x7ubi.kurswahl.mapper.StudentMapper;
import com.x7ubi.kurswahl.models.Student;
import com.x7ubi.kurswahl.models.StudentClass;
import com.x7ubi.kurswahl.models.User;
import com.x7ubi.kurswahl.repository.StudentClassRepo;
import com.x7ubi.kurswahl.repository.StudentRepo;
import com.x7ubi.kurswahl.repository.UserRepo;
import com.x7ubi.kurswahl.request.admin.StudentSignupRequest;
import com.x7ubi.kurswahl.response.admin.user.StudentResponses;
import com.x7ubi.kurswahl.response.common.ResultResponse;
import com.x7ubi.kurswahl.service.admin.AdminErrorService;
import com.x7ubi.kurswahl.utils.PasswordGenerator;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class StudentCreationService {

    private final Logger logger = LoggerFactory.getLogger(StudentCreationService.class);

    private final AdminErrorService adminErrorService;

    private final StudentRepo studentRepo;

    private final UserRepo userRepo;

    private final StudentClassRepo studentClassRepo;

    private final PasswordEncoder passwordEncoder;

    private final UsernameService usernameService;

    private final StudentMapper studentMapper;

    protected StudentCreationService(AdminErrorService adminErrorService, StudentRepo studentRepo, UserRepo userRepo,
                                     StudentClassRepo studentClassRepo, PasswordEncoder passwordEncoder,
                                     UsernameService usernameService, StudentMapper studentMapper) {
        this.adminErrorService = adminErrorService;
        this.studentRepo = studentRepo;
        this.userRepo = userRepo;
        this.studentClassRepo = studentClassRepo;
        this.passwordEncoder = passwordEncoder;
        this.usernameService = usernameService;
        this.studentMapper = studentMapper;
    }

    @Transactional
    public ResultResponse registerStudent(StudentSignupRequest studentSignupRequest) {
        ResultResponse resultResponse = new ResultResponse();

        resultResponse.setErrorMessages(this.adminErrorService
                .getStudentClassNotFound(studentSignupRequest.getStudentClassId()));

        if (!resultResponse.getErrorMessages().isEmpty()) {
            return resultResponse;
        }

        Student student = this.studentMapper.studentRequestToStudent(studentSignupRequest);
        student.getUser().setUsername(this.usernameService.generateUsernameFromName(studentSignupRequest));
        student.getUser().setGeneratedPassword(PasswordGenerator.generatePassword());
        student.getUser().setPassword(passwordEncoder.encode(student.getUser().getGeneratedPassword()));
        StudentClass studentClass =
                this.studentClassRepo.findStudentClassByStudentClassId(studentSignupRequest.getStudentClassId()).get();
        student.setStudentClass(studentClass);

        this.studentRepo.save(student);
        studentClass.getStudents().add(student);
        this.studentClassRepo.save(studentClass);

        logger.info(String.format("Student %s was created", student.getUser().getUsername()));

        return resultResponse;
    }

    public StudentResponses getAllStudents() {
        List<Student> students = this.studentRepo.findAll();

        return this.studentMapper.studentsToStudentResponses(students);
    }

    @Transactional
    public ResultResponse editStudent(Long studentId, StudentSignupRequest studentSignupRequest) {
        ResultResponse resultResponse = new ResultResponse();

        resultResponse.setErrorMessages(this.adminErrorService.getStudentNotFound(studentId));
        resultResponse.getErrorMessages().addAll(
                this.adminErrorService.getStudentClassNotFound(studentSignupRequest.getStudentClassId()));

        if (!resultResponse.getErrorMessages().isEmpty()) {
            return resultResponse;
        }

        Student student = this.studentRepo.findStudentByStudentId(studentId).get();
        this.studentMapper.studentRequestToStudent(studentSignupRequest, student);

        if (null == student.getStudentClass()) {
            StudentClass studentClass = this.studentClassRepo
                    .findStudentClassByStudentClassId(studentSignupRequest.getStudentClassId()).get();
            studentClass.getStudents().add(student);
            student.setStudentClass(studentClass);
            this.studentClassRepo.save(studentClass);
        }

        if (!Objects.equals(student.getStudentClass().getStudentClassId(), studentSignupRequest.getStudentClassId())) {
            student.getStudentClass().getStudents().remove(student);
            this.studentClassRepo.save(student.getStudentClass());

            StudentClass studentClass = this.studentClassRepo
                    .findStudentClassByStudentClassId(studentSignupRequest.getStudentClassId()).get();
            studentClass.getStudents().add(student);
            student.setStudentClass(studentClass);
            this.studentClassRepo.save(studentClass);
        }

        this.studentRepo.save(student);

        return resultResponse;
    }

    public ResultResponse deleteStudent(Long studentId) {
        ResultResponse resultResponse = new ResultResponse();

        resultResponse.setErrorMessages(this.adminErrorService.getStudentNotFound(studentId));

        if(!resultResponse.getErrorMessages().isEmpty()) {
            return resultResponse;
        }

        Student student = this.studentRepo.findStudentByStudentId(studentId).get();
        User studentUser = student.getUser();
        if (null != student.getStudentClass()) {
            student.getStudentClass().getStudents().remove(student);
            this.studentClassRepo.save(student.getStudentClass());
        }

        logger.info(String.format("Deleted Student %s", studentUser.getUsername()));

        this.studentRepo.delete(student);
        this.userRepo.delete(studentUser);

        return resultResponse;
    }
}
