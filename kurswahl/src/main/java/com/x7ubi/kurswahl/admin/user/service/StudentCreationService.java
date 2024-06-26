package com.x7ubi.kurswahl.admin.user.service;

import com.x7ubi.kurswahl.admin.user.mapper.StudentMapper;
import com.x7ubi.kurswahl.admin.user.request.StudentSignupRequest;
import com.x7ubi.kurswahl.admin.user.response.StudentResponse;
import com.x7ubi.kurswahl.common.auth.utils.PasswordGenerator;
import com.x7ubi.kurswahl.common.error.ErrorMessage;
import com.x7ubi.kurswahl.common.exception.EntityNotFoundException;
import com.x7ubi.kurswahl.common.models.*;
import com.x7ubi.kurswahl.common.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class StudentCreationService {

    private final Logger logger = LoggerFactory.getLogger(StudentCreationService.class);

    private final StudentRepo studentRepo;

    private final UserRepo userRepo;

    private final StudentClassRepo studentClassRepo;

    private final ChoiceRepo choiceRepo;

    private final ClassRepo classRepo;

    private final ChoiceClassRepo choiceClassRepo;

    private final PasswordEncoder passwordEncoder;

    private final UsernameService usernameService;

    private final StudentMapper studentMapper;

    private final MessageRepo messageRepo;

    private final AddresseeMessageRepo addresseeMessageRepo;

    protected StudentCreationService(StudentRepo studentRepo, UserRepo userRepo, StudentClassRepo studentClassRepo,
                                     ChoiceRepo choiceRepo, ClassRepo classRepo, ChoiceClassRepo choiceClassRepo,
                                     PasswordEncoder passwordEncoder, UsernameService usernameService,
                                     StudentMapper studentMapper, MessageRepo messageRepo, AddresseeMessageRepo addresseeMessageRepo) {
        this.studentRepo = studentRepo;
        this.userRepo = userRepo;
        this.studentClassRepo = studentClassRepo;
        this.choiceRepo = choiceRepo;
        this.classRepo = classRepo;
        this.choiceClassRepo = choiceClassRepo;
        this.passwordEncoder = passwordEncoder;
        this.usernameService = usernameService;
        this.studentMapper = studentMapper;
        this.messageRepo = messageRepo;
        this.addresseeMessageRepo = addresseeMessageRepo;
    }

    @Transactional
    public void registerStudent(StudentSignupRequest studentSignupRequest) throws EntityNotFoundException {

        StudentClass studentClass = getStudentClass(studentSignupRequest);

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

    public List<StudentResponse> getAllStudents() {
        List<Student> students = this.studentRepo.findAll();
        students = students.stream().filter(student -> null == student.getStudentClass() ||
                (null != student.getStudentClass() &&
                        Objects.equals(student.getStudentClass().getReleaseYear(), Year.now().getValue()))).toList();

        return this.studentMapper.studentsToStudentResponseList(students);
    }

    public void editStudent(Long studentId, StudentSignupRequest studentSignupRequest) throws EntityNotFoundException {

        StudentClass studentClass = getStudentClass(studentSignupRequest);

        Student student = getStudentFromId(studentId);
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

        Student student = getStudentFromId(studentId);

        return this.studentMapper.studentToStudentResponse(student);
    }

    @Transactional
    public List<StudentResponse> deleteStudent(Long studentId) throws EntityNotFoundException {

        deleteStudentHelper(studentId);

        return this.getAllStudents();
    }

    @Transactional
    protected void deleteStudentHelper(Long studentId) throws EntityNotFoundException {
        Student student = getStudentFromId(studentId);

        User studentUser = student.getUser();
        if (null != student.getStudentClass()) {
            student.getStudentClass().getStudents().remove(student);
            this.studentClassRepo.save(student.getStudentClass());
        }

        List<Choice> choices = new ArrayList<>(student.getChoices());
        student.getChoices().clear();
        this.studentRepo.save(student);

        for (Choice choice : choices) {
            ArrayList<ChoiceClass> choiceClasses = new ArrayList<>(choice.getChoiceClasses());
            for (ChoiceClass choiceClass : choiceClasses) {
                choiceClass.getaClass().getChoiceClasses().remove(choiceClass);
                this.classRepo.save(choiceClass.getaClass());
                choice.getChoiceClasses().remove(choiceClass);
                this.choiceRepo.save(choice);
                this.choiceClassRepo.delete(choiceClass);
            }
            this.choiceRepo.delete(choice);
        }

        this.messageRepo.deleteAll(studentUser.getSentMessages());
        this.addresseeMessageRepo.deleteAll(studentUser.getAddresseeMessage());
        studentUser.getSentMessages().clear();
        studentUser.getAddresseeMessage().clear();

        logger.info(String.format("Deleted Student %s", studentUser.getUsername()));

        this.studentRepo.delete(student);
        this.userRepo.delete(studentUser);
    }

    @Transactional
    public List<StudentResponse> deleteStudents(List<Long> studentIds) throws EntityNotFoundException {
        for (Long studentId : studentIds) {
            deleteStudentHelper(studentId);
        }

        return this.getAllStudents();
    }

    private Student getStudentFromId(Long studentId) throws EntityNotFoundException {
        Optional<Student> studentOptional = this.studentRepo.findStudentByStudentId(studentId);

        if (studentOptional.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.STUDENT_NOT_FOUND);
        }

        return studentOptional.get();
    }

    private StudentClass getStudentClass(StudentSignupRequest studentSignupRequest) throws EntityNotFoundException {
        Optional<StudentClass> studentClassOptional =
                this.studentClassRepo.findStudentClassByStudentClassId(studentSignupRequest.getStudentClassId());

        if (studentClassOptional.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.STUDENT_CLASS_NOT_FOUND);
        }

        return studentClassOptional.get();
    }
}
