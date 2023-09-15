package com.x7ubi.kurswahl.admin;

import com.x7ubi.kurswahl.error.ErrorMessage;
import com.x7ubi.kurswahl.models.Teacher;
import com.x7ubi.kurswahl.models.User;
import com.x7ubi.kurswahl.repository.TeacherRepo;
import com.x7ubi.kurswahl.request.admin.TeacherSignupRequest;
import com.x7ubi.kurswahl.response.common.ResultResponse;
import com.x7ubi.kurswahl.service.admin.TeacherCreationService;
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
        "spring.datasource.url=jdbc:h2:mem:kurswahlTestdb;NON_KEYWORDS=user"
})
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class TeacherCreationServiceTest {

    @Autowired
    private TeacherCreationService teacherCreationService;

    @Autowired
    private TeacherRepo teacherRepo;

    private Teacher teacher;

    @BeforeEach
    public void setupTests() {
        User user = new User();
        user.setUsername("test");
        user.setFirstname("Test");
        user.setSurname("User");
        user.setPassword("Password");
        teacher = new Teacher();
        teacher.setUser(user);

        this.teacherRepo.save(teacher);
    }

    @Test
    public void testCreateAdmin() {
        // Given
        TeacherSignupRequest teacherSignupRequest = new TeacherSignupRequest();
        teacherSignupRequest.setFirstname("Firstname");
        teacherSignupRequest.setSurname("Surname");
        teacherSignupRequest.setUsername("Username");

        // When
        ResultResponse response = this.teacherCreationService.registerTeacher(teacherSignupRequest);

        // Then
        Teacher createdTeacher = this.teacherRepo.findTeacherByUser_Username("Username").get();
        Assertions.assertTrue(response.getErrorMessages().isEmpty());
        Assertions.assertEquals(createdTeacher.getUser().getFirstname(), teacherSignupRequest.getFirstname());
        Assertions.assertEquals(createdTeacher.getUser().getSurname(), teacherSignupRequest.getSurname());
        Assertions.assertEquals(createdTeacher.getUser().getUsername(), teacherSignupRequest.getUsername());
    }

    @Test
    public void testCreateAdminUsernameExists() {
        // Given
        TeacherSignupRequest teacherSignupRequest = new TeacherSignupRequest();
        teacherSignupRequest.setFirstname("Firstname");
        teacherSignupRequest.setSurname("Surname");
        teacherSignupRequest.setUsername("test");

        // When
        ResultResponse response = this.teacherCreationService.registerTeacher(teacherSignupRequest);

        // Then
        Teacher createdTeacher = this.teacherRepo.findTeacherByUser_Username("test").get();
        Assertions.assertEquals(response.getErrorMessages().size(), 1);
        Assertions.assertEquals(response.getErrorMessages().get(0).getMessage(), ErrorMessage.Authentication.USERNAME_EXITS);
        Assertions.assertEquals(createdTeacher.getUser().getFirstname(), teacher.getUser().getFirstname());
        Assertions.assertEquals(createdTeacher.getUser().getSurname(), teacher.getUser().getSurname());
        Assertions.assertEquals(createdTeacher.getUser().getUsername(), teacher.getUser().getUsername());
    }

    @Test
    public void testDeleteAdmin() {
        // Given
        this.teacher = this.teacherRepo.findTeacherByUser_Username(this.teacher.getUser().getUsername()).get();
        Long id = this.teacher.getTeacherId();

        // When
        ResultResponse response = this.teacherCreationService.deleteTeacher(id);

        // Then
        Assertions.assertTrue(response.getErrorMessages().isEmpty());
        Assertions.assertFalse(this.teacherRepo.existsTeacherByUser_Username(this.teacher.getUser().getUsername()));
    }

    @Test
    public void testDeleteAdminWrongId() {
        // Given
        this.teacher = this.teacherRepo.findTeacherByUser_Username(this.teacher.getUser().getUsername()).get();
        Long id = this.teacher.getTeacherId() + 1;

        // When
        ResultResponse response = this.teacherCreationService.deleteTeacher(id);

        // Then
        Assertions.assertEquals(response.getErrorMessages().size(), 1);
        Assertions.assertEquals(response.getErrorMessages().get(0).getMessage(), ErrorMessage.Administration.TEACHER_NOT_FOUND);
        Assertions.assertTrue(this.teacherRepo.existsTeacherByUser_Username(this.teacher.getUser().getUsername()));
    }
}
