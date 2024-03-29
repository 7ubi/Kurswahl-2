package com.x7ubi.kurswahl.admin.classes.service;

import com.x7ubi.kurswahl.admin.classes.mapper.StudentClassMapper;
import com.x7ubi.kurswahl.admin.classes.request.StudentClassCreationRequest;
import com.x7ubi.kurswahl.admin.classes.response.StudentClassResponse;
import com.x7ubi.kurswahl.common.error.ErrorMessage;
import com.x7ubi.kurswahl.common.exception.EntityCreationException;
import com.x7ubi.kurswahl.common.exception.EntityNotFoundException;
import com.x7ubi.kurswahl.common.models.Student;
import com.x7ubi.kurswahl.common.models.StudentClass;
import com.x7ubi.kurswahl.common.models.Teacher;
import com.x7ubi.kurswahl.common.repository.StudentClassRepo;
import com.x7ubi.kurswahl.common.repository.StudentRepo;
import com.x7ubi.kurswahl.common.repository.TeacherRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class StudentClassCreationService {

    Logger logger = LoggerFactory.getLogger(StudentClassCreationService.class);

    private final TeacherRepo teacherRepo;

    private final StudentClassRepo studentClassRepo;

    private final StudentRepo studentRepo;

    private final StudentClassMapper studentClassMapper;

    protected StudentClassCreationService(TeacherRepo teacherRepo, StudentClassRepo studentClassRepo,
                                          StudentRepo studentRepo, StudentClassMapper studentClassMapper) {
        this.teacherRepo = teacherRepo;
        this.studentClassRepo = studentClassRepo;
        this.studentRepo = studentRepo;
        this.studentClassMapper = studentClassMapper;
    }

    @Transactional
    public void createStudentClass(StudentClassCreationRequest studentClassCreationRequest)
            throws EntityCreationException, EntityNotFoundException {
        this.findStudentClassCreationError(studentClassCreationRequest);

        Optional<Teacher> teacherOptional = this.teacherRepo.findTeacherByTeacherId(
                studentClassCreationRequest.getTeacherId());

        if (teacherOptional.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.TEACHER_NOT_FOUND);
        }

        Teacher teacher = teacherOptional.get();
        StudentClass studentClass
                = this.studentClassMapper.studentClassRequestToStudentClass(studentClassCreationRequest);
        studentClass.setTeacher(teacher);
        studentClass.setReleaseYear(Year.now().getValue());
        this.studentClassRepo.save(studentClass);
        teacher.getStudentClasses().add(studentClass);
        this.teacherRepo.save(teacher);

        logger.info(String.format("Student class %s with teacher %s was created", studentClass.getName(),
                teacher.getUser().getUsername()));
    }

    public void editStudentClass(Long studentClassId, StudentClassCreationRequest studentClassCreationRequest)
            throws EntityCreationException, EntityNotFoundException {

        Optional<Teacher> teacherOptional = this.teacherRepo.findTeacherByTeacherId(
                studentClassCreationRequest.getTeacherId());

        if (teacherOptional.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.TEACHER_NOT_FOUND);
        }

        Teacher teacher = teacherOptional.get();

        Optional<StudentClass> studentClassOptional
                = this.studentClassRepo.findStudentClassByStudentClassId(studentClassId);

        if (studentClassOptional.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.STUDENT_CLASS_NOT_FOUND);
        }

        StudentClass studentClass = studentClassOptional.get();
        if (!Objects.equals(studentClass.getName(), studentClassCreationRequest.getName())) {
            this.findStudentClassCreationError(studentClassCreationRequest);
        }
        this.studentClassMapper.studentClassRequestToStudentClass(studentClassCreationRequest, studentClass);

        if (studentClass.getTeacher() == null || !Objects.equals(studentClass.getTeacher().getTeacherId(),
                studentClassCreationRequest.getTeacherId())) {
            if (studentClass.getTeacher() != null) {
                studentClass.getTeacher().getStudentClasses().remove(studentClass);
                this.teacherRepo.save(studentClass.getTeacher());
            }

            teacher.getStudentClasses().add(studentClass);
            this.teacherRepo.save(teacher);

            studentClass.setTeacher(teacher);
        }

        this.studentClassRepo.save(studentClass);

        logger.info(String.format("Edited student class %s", studentClass.getName()));
    }

    public StudentClassResponse getStudentClass(Long studentClassId) throws EntityNotFoundException {
        Optional<StudentClass> studentClassOptional
                = this.studentClassRepo.findStudentClassByStudentClassId(studentClassId);

        if (studentClassOptional.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.STUDENT_CLASS_NOT_FOUND);
        }

        StudentClass studentClass = studentClassOptional.get();

        logger.info(String.format("Got student class %s", studentClass.getName()));

        return this.studentClassMapper.studentClassToStudentClassResponse(studentClass);
    }

    @Transactional
    public List<StudentClassResponse> getAllStudentClasses() {
        List<StudentClass> studentClasses = this.studentClassRepo.findAllByReleaseYear(Year.now().getValue());

        return this.studentClassMapper.studentClassListToStudentClassResponseList(studentClasses);
    }

    @Transactional
    public List<StudentClassResponse> deleteStudentClass(Long studentClassId) throws EntityNotFoundException {

        deleteStudentClassHelper(studentClassId);

        return getAllStudentClasses();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteStudentClassHelper(Long studentClassId) throws EntityNotFoundException {
        Optional<StudentClass> studentClassOptional
                = this.studentClassRepo.findStudentClassByStudentClassId(studentClassId);

        if (studentClassOptional.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.STUDENT_CLASS_NOT_FOUND);
        }

        StudentClass studentClass = studentClassOptional.get();

        studentClass.getTeacher().getStudentClasses().remove(studentClass);
        this.teacherRepo.save(studentClass.getTeacher());
        for (Student student : studentClass.getStudents()) {
            student.setStudentClass(null);
            this.studentRepo.save(student);
        }
        this.studentClassRepo.delete(studentClass);

        logger.info(String.format("Student Class %s was deleted", studentClass.getName()));
    }


    public void findStudentClassCreationError(StudentClassCreationRequest studentClassCreationRequest)
            throws EntityCreationException {
        Integer currentYear = Year.now().getValue();

        if (this.studentClassRepo.existsStudentClassByNameAndReleaseYear(
                studentClassCreationRequest.getName(), currentYear)) {
            throw new EntityCreationException(ErrorMessage.STUDENT_CLASS_ALREADY_EXISTS);
        }
    }

    @Transactional
    public List<StudentClassResponse> deleteStudentClasses(List<Long> studentClassIds) throws EntityNotFoundException {
        for (Long studentClassId : studentClassIds) {
            this.deleteStudentClassHelper(studentClassId);
        }

        return getAllStudentClasses();
    }
}
