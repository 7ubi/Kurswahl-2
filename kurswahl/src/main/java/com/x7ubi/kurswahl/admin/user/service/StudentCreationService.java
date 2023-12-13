package com.x7ubi.kurswahl.admin.user.service;

import com.x7ubi.kurswahl.admin.user.mapper.StudentMapper;
import com.x7ubi.kurswahl.admin.user.request.StudentSignupRequest;
import com.x7ubi.kurswahl.admin.user.response.StudentResponse;
import com.x7ubi.kurswahl.admin.user.response.StudentResponses;
import com.x7ubi.kurswahl.common.error.ErrorMessage;
import com.x7ubi.kurswahl.common.exception.EntityNotFoundException;
import com.x7ubi.kurswahl.common.models.Student;
import com.x7ubi.kurswahl.common.models.StudentClass;
import com.x7ubi.kurswahl.common.models.User;
import com.x7ubi.kurswahl.common.repository.StudentClassRepo;
import com.x7ubi.kurswahl.common.repository.StudentRepo;
import com.x7ubi.kurswahl.common.repository.UserRepo;
import com.x7ubi.kurswahl.common.utils.PasswordGenerator;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class StudentCreationService {

    private final Logger logger = LoggerFactory.getLogger(StudentCreationService.class);

    private final StudentRepo studentRepo;

    private final UserRepo userRepo;

    private final StudentClassRepo studentClassRepo;

    private final PasswordEncoder passwordEncoder;

    private final UsernameService usernameService;

    private final StudentMapper studentMapper;

    protected StudentCreationService(StudentRepo studentRepo, UserRepo userRepo, StudentClassRepo studentClassRepo,
                                     PasswordEncoder passwordEncoder, UsernameService usernameService,
                                     StudentMapper studentMapper) {
        this.studentRepo = studentRepo;
        this.userRepo = userRepo;
        this.studentClassRepo = studentClassRepo;
        this.passwordEncoder = passwordEncoder;
        this.usernameService = usernameService;
        this.studentMapper = studentMapper;
    }

    @Transactional
    public void registerStudent(StudentSignupRequest studentSignupRequest) throws EntityNotFoundException {

        Optional<StudentClass> studentClassOptional =
                this.studentClassRepo.findStudentClassByStudentClassId(studentSignupRequest.getStudentClassId());

        if(studentClassOptional.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.STUDENT_CLASS_NOT_FOUND);
        }

        StudentClass studentClass = studentClassOptional.get();

        Student student = this.studentMapper.studentRequestToStudent(studentSignupRequest);
        student.getUser().setUsername(this.usernameService.generateUsernameFromName(studentSignupRequest));
        student.getUser().setGeneratedPassword(PasswordGenerator.generatePassword());
        student.getUser().setPassword(passwordEncoder.encode(student.getUser().getGeneratedPassword()));
        student.setStudentClass(studentClass);

        this.studentRepo.save(student);
        studentClass.getStudents().add(student);
        this.studentClassRepo.save(studentClass);

        logger.info(String.format("Student %s was created", student.getUser().getUsername()));
    }

    public StudentResponses getAllStudents() {
        List<Student> students = this.studentRepo.findAll();

        return this.studentMapper.studentsToStudentResponses(students);
    }

    @Transactional
    public void editStudent(Long studentId, StudentSignupRequest studentSignupRequest) throws EntityNotFoundException {

        Optional<StudentClass> studentClassOptional =
                this.studentClassRepo.findStudentClassByStudentClassId(studentSignupRequest.getStudentClassId());

        if(studentClassOptional.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.STUDENT_CLASS_NOT_FOUND);
        }

        StudentClass studentClass = studentClassOptional.get();

        Optional<Student> studentOptional = this.studentRepo.findStudentByStudentId(studentId);

        if(studentOptional.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.STUDENT_NOT_FOUND);
        }

        Student student = studentOptional.get();
        this.studentMapper.studentRequestToStudent(studentSignupRequest, student);

        if (null == student.getStudentClass()) {
            studentClass.getStudents().add(student);
            student.setStudentClass(studentClass);
            this.studentClassRepo.save(studentClass);
        }

        if (!Objects.equals(student.getStudentClass().getStudentClassId(), studentSignupRequest.getStudentClassId())) {
            student.getStudentClass().getStudents().remove(student);
            this.studentClassRepo.save(student.getStudentClass());

            studentClass.getStudents().add(student);
            student.setStudentClass(studentClass);
            this.studentClassRepo.save(studentClass);
        }

        this.studentRepo.save(student);

        logger.info(String.format("Student %s was edited", student.getUser().getUsername()));
    }

    public StudentResponse getStudent(Long studentId) throws EntityNotFoundException {

        Optional<Student> studentOptional = this.studentRepo.findStudentByStudentId(studentId);

        if(studentOptional.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.STUDENT_NOT_FOUND);
        }

        Student student = studentOptional.get();

        return this.studentMapper.studentToStudentResponse(student);
    }

    public void deleteStudent(Long studentId) throws EntityNotFoundException {

        Optional<Student> studentOptional = this.studentRepo.findStudentByStudentId(studentId);

        if(studentOptional.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.STUDENT_NOT_FOUND);
        }

        Student student = studentOptional.get();

        User studentUser = student.getUser();
        if (null != student.getStudentClass()) {
            student.getStudentClass().getStudents().remove(student);
            this.studentClassRepo.save(student.getStudentClass());
        }

        logger.info(String.format("Deleted Student %s", studentUser.getUsername()));

        this.studentRepo.delete(student);
        this.userRepo.delete(studentUser);
    }
}
