package com.x7ubi.kurswahl.admin.classes;

import com.x7ubi.kurswahl.KurswahlServiceTest;
import com.x7ubi.kurswahl.error.ErrorMessage;
import com.x7ubi.kurswahl.models.StudentClass;
import com.x7ubi.kurswahl.models.Teacher;
import com.x7ubi.kurswahl.models.User;
import com.x7ubi.kurswahl.repository.StudentClassRepo;
import com.x7ubi.kurswahl.repository.TeacherRepo;
import com.x7ubi.kurswahl.request.admin.StudentClassCreationRequest;
import com.x7ubi.kurswahl.response.common.ResultResponse;
import com.x7ubi.kurswahl.service.admin.classes.StudentClassCreationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Year;

@KurswahlServiceTest
public class StudentClassCreationServiceTest {
    @Autowired
    private StudentClassCreationService studentClassCreationService;

    @Autowired
    private StudentClassRepo studentClassRepo;

    @Autowired
    private TeacherRepo teacherRepo;

    private Teacher teacher;

    private StudentClass studentClass;

    @BeforeEach
    public void setupTest() {
        setupTeacher();
        setupStudentClass();
    }

    private void setupTeacher() {
        User user = new User();
        user.setUsername("test");
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
    }

    @Test
    public void testCreateStudentClass() {
        // Given
        StudentClassCreationRequest studentClassCreationRequest = new StudentClassCreationRequest();
        studentClassCreationRequest.setName("E2a");
        studentClassCreationRequest.setYear(11);
        studentClassCreationRequest.setTeacherId(teacher.getTeacherId());

        // When
        ResultResponse response = this.studentClassCreationService.createStudentClass(studentClassCreationRequest);

        // Then
        Assertions.assertTrue(response.getErrorMessages().isEmpty());

        StudentClass createdStudentClass
                = this.studentClassRepo.findStudentClassAreaByName(studentClassCreationRequest.getName()).get();
        Assertions.assertEquals(createdStudentClass.getName(), studentClassCreationRequest.getName());
        Assertions.assertEquals(createdStudentClass.getYear(), studentClassCreationRequest.getYear());
        Assertions.assertEquals(createdStudentClass.getTeacher().getTeacherId(), studentClassCreationRequest.getTeacherId());
        Assertions.assertEquals(createdStudentClass.getReleaseYear(), Year.now().getValue());
    }

    @Test
    public void testCreateStudentClassAlreadyExists() {
        // Given
        teacher = this.teacherRepo.findTeacherByUser_Username(teacher.getUser().getUsername()).get();
        StudentClassCreationRequest studentClassCreationRequest = new StudentClassCreationRequest();
        studentClassCreationRequest.setName("Q2a");
        studentClassCreationRequest.setYear(11);
        studentClassCreationRequest.setTeacherId(teacher.getTeacherId());

        // When
        ResultResponse response = this.studentClassCreationService.createStudentClass(studentClassCreationRequest);

        // Then
        Assertions.assertEquals(response.getErrorMessages().size(), 1);
        Assertions.assertEquals(response.getErrorMessages().get(0).getMessage(),
                ErrorMessage.Administration.STUDENT_CLASS_ALREADY_EXISTS);

        StudentClass createdStudentClass
                = this.studentClassRepo.findStudentClassAreaByName(studentClassCreationRequest.getName()).get();
        Assertions.assertEquals(createdStudentClass.getName(), studentClass.getName());
        Assertions.assertEquals(createdStudentClass.getYear(), studentClass.getYear());
        Assertions.assertEquals(createdStudentClass.getTeacher().getTeacherId(), studentClass.getTeacher().getTeacherId());
        Assertions.assertEquals(createdStudentClass.getReleaseYear(), Year.now().getValue());
    }

    @Test
    public void testCreateStudentClassTeacherWrongId() {
        // Given
        teacher = this.teacherRepo.findTeacherByUser_Username(teacher.getUser().getUsername()).get();
        StudentClassCreationRequest studentClassCreationRequest = new StudentClassCreationRequest();
        studentClassCreationRequest.setName("E2a");
        studentClassCreationRequest.setYear(11);
        studentClassCreationRequest.setTeacherId(teacher.getTeacherId() + 1);

        // When
        ResultResponse response = this.studentClassCreationService.createStudentClass(studentClassCreationRequest);

        // Then
        Assertions.assertEquals(response.getErrorMessages().size(), 1);
        Assertions.assertEquals(response.getErrorMessages().get(0).getMessage(),
                ErrorMessage.Administration.TEACHER_NOT_FOUND);

        Assertions.assertFalse(this.studentClassRepo.existsStudentClassAreaByName(studentClassCreationRequest.getName()));
    }
}
