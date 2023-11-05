package com.x7ubi.kurswahl.admin.user;

import com.x7ubi.kurswahl.KurswahlServiceTest;
import com.x7ubi.kurswahl.error.ErrorMessage;
import com.x7ubi.kurswahl.models.Student;
import com.x7ubi.kurswahl.models.User;
import com.x7ubi.kurswahl.repository.StudentRepo;
import com.x7ubi.kurswahl.request.admin.StudentSignupRequest;
import com.x7ubi.kurswahl.response.common.ResultResponse;
import com.x7ubi.kurswahl.service.admin.user.StudentCreationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@KurswahlServiceTest
public class StudentCreationServiceTest {

    @Autowired
    private StudentCreationService studentCreationService;

    @Autowired
    private StudentRepo studentRepo;

    private Student student;

    @BeforeEach
    public void setupTests() {
        User user = new User();
        user.setUsername("test");
        user.setFirstname("Test");
        user.setSurname("User");
        user.setPassword("Password");
        student = new Student();
        student.setUser(user);

        this.studentRepo.save(student);
    }

    @Test
    public void testCreateStudent() {
        // Given
        StudentSignupRequest studentSignupRequest = new StudentSignupRequest();
        studentSignupRequest.setFirstname("Firstname");
        studentSignupRequest.setSurname("Surname");

        // When
        this.studentCreationService.registerStudent(studentSignupRequest);

        // Then
        Student createdStudent = this.studentRepo.findStudentByUser_Username("firstname.surname").get();
        Assertions.assertEquals(createdStudent.getUser().getFirstname(), studentSignupRequest.getFirstname());
        Assertions.assertEquals(createdStudent.getUser().getSurname(), studentSignupRequest.getSurname());
        Assertions.assertEquals(createdStudent.getUser().getUsername(),
                String.format("%s.%s", studentSignupRequest.getFirstname().toLowerCase(),
                        studentSignupRequest.getSurname().toLowerCase()));
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
}
