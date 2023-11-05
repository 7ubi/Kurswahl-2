package com.x7ubi.kurswahl.service.admin.user;

import com.x7ubi.kurswahl.models.Student;
import com.x7ubi.kurswahl.models.User;
import com.x7ubi.kurswahl.repository.StudentRepo;
import com.x7ubi.kurswahl.repository.UserRepo;
import com.x7ubi.kurswahl.request.admin.StudentSignupRequest;
import com.x7ubi.kurswahl.response.admin.user.StudentResponse;
import com.x7ubi.kurswahl.response.admin.user.StudentResponses;
import com.x7ubi.kurswahl.response.common.ResultResponse;
import com.x7ubi.kurswahl.service.admin.AdminErrorService;
import com.x7ubi.kurswahl.utils.PasswordGenerator;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudentCreationService {

    private final Logger logger = LoggerFactory.getLogger(StudentCreationService.class);

    private final AdminErrorService adminErrorService;

    private final StudentRepo studentRepo;

    private final UserRepo userRepo;

    private final PasswordEncoder passwordEncoder;

    private final UsernameService usernameService;

    private final ModelMapper mapper = new ModelMapper();

    protected StudentCreationService(AdminErrorService adminErrorService, StudentRepo studentRepo, UserRepo userRepo,
                                     PasswordEncoder passwordEncoder, UsernameService usernameService) {
        this.adminErrorService = adminErrorService;
        this.studentRepo = studentRepo;
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.usernameService = usernameService;
    }

    public void registerStudent(StudentSignupRequest studentSignupRequest) {
        Student student = new Student();
        student.setUser(this.mapper.map(studentSignupRequest, User.class));
        student.getUser().setUsername(this.usernameService.generateUsernameFromName(studentSignupRequest));
        student.getUser().setGeneratedPassword(PasswordGenerator.generatePassword());
        student.getUser().setPassword(passwordEncoder.encode(student.getUser().getGeneratedPassword()));

        this.studentRepo.save(student);

        logger.info(String.format("Student %s was created", student.getUser().getUsername()));
    }

    public StudentResponses getAllStudents() {
        StudentResponses studentResponses = new StudentResponses();
        studentResponses.setStudentResponses(new ArrayList<>());

        List<Student> students = this.studentRepo.findAll();
        for(Student student: students) {
            StudentResponse studentResponse = this.mapper.map(student.getUser(), StudentResponse.class);
            studentResponse.setStudentId(student.getStudentId());
            studentResponses.getStudentResponses().add(studentResponse);
        }

        return studentResponses;
    }

    public ResultResponse deleteStudent(Long studentId) {
        ResultResponse resultResponse = new ResultResponse();

        resultResponse.setErrorMessages(this.adminErrorService.getStudentNotFound(studentId));

        if(!resultResponse.getErrorMessages().isEmpty()) {
            return resultResponse;
        }

        Student student = this.studentRepo.findStudentByStudentId(studentId).get();
        User studentUser = student.getUser();

        logger.info(String.format("Deleted Student %s", studentUser.getUsername()));

        this.studentRepo.delete(student);
        this.userRepo.delete(studentUser);

        return resultResponse;
    }
}
