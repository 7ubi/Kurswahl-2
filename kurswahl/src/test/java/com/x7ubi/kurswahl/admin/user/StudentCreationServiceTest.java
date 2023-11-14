package com.x7ubi.kurswahl.admin.user;

import com.x7ubi.kurswahl.KurswahlServiceTest;
import com.x7ubi.kurswahl.error.ErrorMessage;
import com.x7ubi.kurswahl.models.Student;
import com.x7ubi.kurswahl.models.StudentClass;
import com.x7ubi.kurswahl.models.Teacher;
import com.x7ubi.kurswahl.models.User;
import com.x7ubi.kurswahl.repository.StudentClassRepo;
import com.x7ubi.kurswahl.repository.StudentRepo;
import com.x7ubi.kurswahl.repository.TeacherRepo;
import com.x7ubi.kurswahl.request.admin.StudentSignupRequest;
import com.x7ubi.kurswahl.response.admin.user.StudentResponse;
import com.x7ubi.kurswahl.response.admin.user.StudentResponses;
import com.x7ubi.kurswahl.response.admin.user.StudentResultResponse;
import com.x7ubi.kurswahl.response.common.ResultResponse;
import com.x7ubi.kurswahl.service.admin.user.StudentCreationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Year;
import java.util.HashSet;
import java.util.Objects;

@KurswahlServiceTest
public class StudentCreationServiceTest {

    @Autowired
    private StudentCreationService studentCreationService;

    @Autowired
    private StudentRepo studentRepo;

    @Autowired
    private TeacherRepo teacherRepo;

    @Autowired
    private StudentClassRepo studentClassRepo;

    private Student student;

    private Teacher teacher;

    private StudentClass studentClass;

    @BeforeEach
    public void setupTests() {
        User user = new User();
        user.setUsername("test.user");
        user.setFirstname("Test");
        user.setSurname("User");
        user.setPassword("Password");
        student = new Student();
        student.setUser(user);

        setupTeacher();
        setupStudentClass();
        student.setStudentClass(studentClass);
        this.studentRepo.save(student);
        studentClass.getStudents().add(student);
        this.studentClassRepo.save(studentClass);
    }

    private void setupTeacher() {
        User user = new User();
        user.setUsername("test.teacher");
        user.setFirstname("Test");
        user.setSurname("User");
        user.setPassword("Password");
        teacher = new Teacher();
        teacher.setAbbreviation("NN");
        teacher.setUser(user);

        this.teacherRepo.save(teacher);
        teacher = this.teacherRepo.findTeacherByUser_Username(teacher.getUser().getUsername()).get();
    }

    private void setupStudentClass() {
        studentClass = new StudentClass();
        studentClass.setTeacher(teacher);
        studentClass.setName("Q2a");
        studentClass.setReleaseYear(Year.now().getValue());
        studentClass.setYear(12);

        this.studentClassRepo.save(studentClass);

        teacher.setStudentClasses(new HashSet<>());
        teacher.getStudentClasses().add(studentClass);
        this.teacherRepo.save(teacher);

        studentClass = this.studentClassRepo.findStudentClassByName("Q2a").get();
    }

    @Test
    public void testCreateStudent() {
        // Given
        StudentSignupRequest studentSignupRequest = new StudentSignupRequest();
        studentSignupRequest.setFirstname("Firstname");
        studentSignupRequest.setSurname("Surname");
        studentSignupRequest.setStudentClassId(studentClass.getStudentClassId());

        // When
        ResultResponse response = this.studentCreationService.registerStudent(studentSignupRequest);

        // Then
        Assertions.assertTrue(response.getErrorMessages().isEmpty());

        Student createdStudent = this.studentRepo.findStudentByUser_Username("firstname.surname").get();
        StudentClass updatedStudentClass
                = this.studentClassRepo.findStudentClassByStudentClassId(studentClass.getStudentClassId()).get();

        Assertions.assertEquals(createdStudent.getUser().getFirstname(), studentSignupRequest.getFirstname());
        Assertions.assertEquals(createdStudent.getUser().getSurname(), studentSignupRequest.getSurname());
        Assertions.assertEquals(createdStudent.getUser().getUsername(),
                String.format("%s.%s", studentSignupRequest.getFirstname().toLowerCase(),
                        studentSignupRequest.getSurname().toLowerCase()));
        Assertions.assertEquals(createdStudent.getStudentClass().getStudentClassId(),
                updatedStudentClass.getStudentClassId());
        Assertions.assertEquals(updatedStudentClass.getStudents()
                .stream().filter(s -> Objects.equals(s.getStudentId(), createdStudent.getStudentId()))
                .findFirst().get().getStudentId(), createdStudent.getStudentId());
    }

    @Test
    public void testCreateStudentWrongStudentClassId() {
        // Given
        StudentSignupRequest studentSignupRequest = new StudentSignupRequest();
        studentSignupRequest.setFirstname("Firstname");
        studentSignupRequest.setSurname("Surname");
        studentSignupRequest.setStudentClassId(studentClass.getStudentClassId() + 1);

        // When
        ResultResponse response = this.studentCreationService.registerStudent(studentSignupRequest);

        // Then
        Assertions.assertEquals(response.getErrorMessages().size(), 1);
        Assertions.assertEquals(response.getErrorMessages().get(0).getMessage(),
                ErrorMessage.Administration.STUDENT_CLASS_NOT_FOUND);
    }

    @Test
    public void testEditStudent() {
        // Given
        this.student = this.studentRepo.findStudentByUser_Username(this.student.getUser().getUsername()).get();
        Long id = this.student.getStudentId();
        StudentSignupRequest studentSignupRequest = new StudentSignupRequest();
        studentSignupRequest.setFirstname("Firstname");
        studentSignupRequest.setSurname("Surname");
        studentSignupRequest.setStudentClassId(studentClass.getStudentClassId());

        // When
        ResultResponse response = this.studentCreationService.editStudent(id, studentSignupRequest);

        // Then
        Assertions.assertTrue(response.getErrorMessages().isEmpty());

        Student editedStudent = this.studentRepo.findStudentByUser_Username("test.user").get();
        StudentClass updatedStudentClass
                = this.studentClassRepo.findStudentClassByStudentClassId(studentClass.getStudentClassId()).get();

        Assertions.assertEquals(editedStudent.getUser().getFirstname(), studentSignupRequest.getFirstname());
        Assertions.assertEquals(editedStudent.getUser().getSurname(), studentSignupRequest.getSurname());
        Assertions.assertEquals(editedStudent.getUser().getUsername(), "test.user");
        Assertions.assertEquals(editedStudent.getStudentClass().getStudentClassId(),
                updatedStudentClass.getStudentClassId());
        Assertions.assertEquals(updatedStudentClass.getStudents().stream().findFirst().get().getStudentId(),
                editedStudent.getStudentId());
    }

    @Test
    public void testEditStudentWrongStudentId() {
        // Given
        this.student = this.studentRepo.findStudentByUser_Username(this.student.getUser().getUsername()).get();
        Long id = this.student.getStudentId() + 1;
        StudentSignupRequest studentSignupRequest = new StudentSignupRequest();
        studentSignupRequest.setFirstname("Firstname");
        studentSignupRequest.setSurname("Surname");
        studentSignupRequest.setStudentClassId(studentClass.getStudentClassId());

        // When
        ResultResponse response = this.studentCreationService.editStudent(id, studentSignupRequest);

        // Then
        Assertions.assertEquals(response.getErrorMessages().size(), 1);
        Assertions.assertEquals(response.getErrorMessages().get(0).getMessage(),
                ErrorMessage.Administration.STUDENT_NOT_FOUND);

        Student editedStudent = this.studentRepo.findStudentByUser_Username("test.user").get();
        StudentClass updatedStudentClass
                = this.studentClassRepo.findStudentClassByStudentClassId(studentClass.getStudentClassId()).get();

        Assertions.assertEquals(editedStudent.getUser().getFirstname(), student.getUser().getFirstname());
        Assertions.assertEquals(editedStudent.getUser().getSurname(), student.getUser().getSurname());
        Assertions.assertEquals(editedStudent.getUser().getUsername(), "test.user");
        Assertions.assertEquals(editedStudent.getStudentClass().getStudentClassId(),
                student.getStudentClass().getStudentClassId());
        Assertions.assertEquals(updatedStudentClass.getStudents().stream().findFirst().get().getStudentId(),
                editedStudent.getStudentId());
    }

    @Test
    public void testEditStudentWrongStudentClassId() {
        // Given
        this.student = this.studentRepo.findStudentByUser_Username(this.student.getUser().getUsername()).get();
        Long id = this.student.getStudentId();
        StudentSignupRequest studentSignupRequest = new StudentSignupRequest();
        studentSignupRequest.setFirstname("Firstname");
        studentSignupRequest.setSurname("Surname");
        studentSignupRequest.setStudentClassId(studentClass.getStudentClassId() + 1);

        // When
        ResultResponse response = this.studentCreationService.editStudent(id, studentSignupRequest);

        // Then
        Assertions.assertEquals(response.getErrorMessages().size(), 1);
        Assertions.assertEquals(response.getErrorMessages().get(0).getMessage(),
                ErrorMessage.Administration.STUDENT_CLASS_NOT_FOUND);

        Student editedStudent = this.studentRepo.findStudentByUser_Username("test.user").get();
        StudentClass updatedStudentClass
                = this.studentClassRepo.findStudentClassByStudentClassId(studentClass.getStudentClassId()).get();

        Assertions.assertEquals(editedStudent.getUser().getFirstname(), student.getUser().getFirstname());
        Assertions.assertEquals(editedStudent.getUser().getSurname(), student.getUser().getSurname());
        Assertions.assertEquals(editedStudent.getUser().getUsername(), "test.user");
        Assertions.assertEquals(editedStudent.getStudentClass().getStudentClassId(),
                student.getStudentClass().getStudentClassId());
        Assertions.assertEquals(updatedStudentClass.getStudents().stream().findFirst().get().getStudentId(),
                editedStudent.getStudentId());
    }

    @Test
    public void testDeleteStudent() {
        // Given
        this.student = this.studentRepo.findStudentByUser_Username(this.student.getUser().getUsername()).get();
        Long id = this.student.getStudentId();

        // When
        ResultResponse response = this.studentCreationService.deleteStudent(id);

        // Then
        Assertions.assertTrue(response.getErrorMessages().isEmpty());
        Assertions.assertFalse(this.studentRepo.existsStudentByUser_Username(this.student.getUser().getUsername()));

        StudentClass updatedStudentClass
                = this.studentClassRepo.findStudentClassByStudentClassId(studentClass.getStudentClassId()).get();

        Assertions.assertTrue(updatedStudentClass.getStudents().isEmpty());
    }

    @Test
    public void testDeleteStudentWrongId() {
        // Given
        this.student = this.studentRepo.findStudentByUser_Username(this.student.getUser().getUsername()).get();
        Long id = this.student.getStudentId() + 1;

        // When
        ResultResponse response = this.studentCreationService.deleteStudent(id);

        // Then
        Assertions.assertEquals(response.getErrorMessages().size(), 1);
        Assertions.assertEquals(response.getErrorMessages().get(0).getMessage(), ErrorMessage.Administration.STUDENT_NOT_FOUND);
        Assertions.assertTrue(this.studentRepo.existsStudentByUser_Username(this.student.getUser().getUsername()));
    }

    @Test
    public void testGetStudent() {
        // Given
        this.student = this.studentRepo.findStudentByUser_Username(this.student.getUser().getUsername()).get();
        Long id = this.student.getStudentId();

        // When
        StudentResultResponse studentResponses = this.studentCreationService.getStudent(id);

        // Then
        Assertions.assertTrue(studentResponses.getErrorMessages().isEmpty());

        StudentResponse studentResponse = studentResponses.getStudentResponse();
        Assertions.assertEquals(studentResponse.getFirstname(), student.getUser().getFirstname());
        Assertions.assertEquals(studentResponse.getSurname(), student.getUser().getSurname());
        Assertions.assertEquals(studentResponse.getUsername(), "test.user");
        Assertions.assertEquals(studentResponse.getStudentClassResponse().getStudentClassId(),
                student.getStudentClass().getStudentClassId());
        Assertions.assertEquals(studentResponse.getStudentClassResponse().getName(),
                student.getStudentClass().getName());
    }

    @Test
    public void testGetStudentWrongId() {
        // Given
        this.student = this.studentRepo.findStudentByUser_Username(this.student.getUser().getUsername()).get();
        Long id = this.student.getStudentId() + 1;

        // When
        StudentResultResponse studentResponses = this.studentCreationService.getStudent(id);

        // Then
        Assertions.assertEquals(studentResponses.getErrorMessages().size(), 1);
        Assertions.assertEquals(studentResponses.getErrorMessages().get(0).getMessage(),
                ErrorMessage.Administration.STUDENT_NOT_FOUND);
    }

    @Test
    public void testGetAllStudents() {
        // When
        StudentResponses studentResponses = this.studentCreationService.getAllStudents();

        // Then
        Assertions.assertEquals(studentResponses.getStudentResponses().size(), 1);
        StudentResponse studentResponse = studentResponses.getStudentResponses().get(0);
        Assertions.assertEquals(studentResponse.getFirstname(), student.getUser().getFirstname());
        Assertions.assertEquals(studentResponse.getSurname(), student.getUser().getSurname());
        Assertions.assertEquals(studentResponse.getUsername(), "test.user");
        Assertions.assertEquals(studentResponse.getStudentClassResponse().getStudentClassId(),
                student.getStudentClass().getStudentClassId());
        Assertions.assertEquals(studentResponse.getStudentClassResponse().getName(),
                student.getStudentClass().getName());
    }
}
