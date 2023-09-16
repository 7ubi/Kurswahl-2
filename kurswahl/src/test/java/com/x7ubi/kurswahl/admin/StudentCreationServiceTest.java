package com.x7ubi.kurswahl.admin;

import com.x7ubi.kurswahl.error.ErrorMessage;
import com.x7ubi.kurswahl.models.Student;
import com.x7ubi.kurswahl.models.User;
import com.x7ubi.kurswahl.repository.StudentRepo;
import com.x7ubi.kurswahl.request.admin.StudentSignupRequest;
import com.x7ubi.kurswahl.response.common.ResultResponse;
import com.x7ubi.kurswahl.service.admin.StudentCreationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest()
@TestPropertySource(properties = {
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.url=jdbc:h2:mem:kurswahlTestdb;NON_KEYWORDS=user",
        "spring.jpa.properties.hibernate.globally_quoted_identifiers=true"
})
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
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
    public void testCreateAdmin() {
        // Given
        StudentSignupRequest studentSignupRequest = new StudentSignupRequest();
        studentSignupRequest.setFirstname("Firstname");
        studentSignupRequest.setSurname("Surname");
        studentSignupRequest.setUsername("Username");

        // When
        ResultResponse response = this.studentCreationService.registerStudent(studentSignupRequest);

        // Then
        Student createdStudent = this.studentRepo.findStudentByUser_Username("Username").get();
        Assertions.assertTrue(response.getErrorMessages().isEmpty());
        Assertions.assertEquals(createdStudent.getUser().getFirstname(), studentSignupRequest.getFirstname());
        Assertions.assertEquals(createdStudent.getUser().getSurname(), studentSignupRequest.getSurname());
        Assertions.assertEquals(createdStudent.getUser().getUsername(), studentSignupRequest.getUsername());
    }

    @Test
    public void testCreateAdminUsernameExists() {
        // Given
        StudentSignupRequest studentSignupRequest = new StudentSignupRequest();
        studentSignupRequest.setFirstname("Firstname");
        studentSignupRequest.setSurname("Surname");
        studentSignupRequest.setUsername("test");

        // When
        ResultResponse response = this.studentCreationService.registerStudent(studentSignupRequest);

        // Then
        Student createdStudent = this.studentRepo.findStudentByUser_Username("test").get();
        Assertions.assertEquals(response.getErrorMessages().size(), 1);
        Assertions.assertEquals(response.getErrorMessages().get(0).getMessage(), ErrorMessage.Authentication.USERNAME_EXITS);
        Assertions.assertEquals(createdStudent.getUser().getFirstname(), student.getUser().getFirstname());
        Assertions.assertEquals(createdStudent.getUser().getSurname(), student.getUser().getSurname());
        Assertions.assertEquals(createdStudent.getUser().getUsername(), student.getUser().getUsername());
    }

    @Test
    public void testDeleteAdmin() {
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
    public void testDeleteAdminWrongId() {
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
