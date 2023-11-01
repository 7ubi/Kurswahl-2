package com.x7ubi.kurswahl.admin.user;

import com.x7ubi.kurswahl.error.ErrorMessage;
import com.x7ubi.kurswahl.models.Teacher;
import com.x7ubi.kurswahl.models.User;
import com.x7ubi.kurswahl.repository.TeacherRepo;
import com.x7ubi.kurswahl.request.admin.TeacherSignupRequest;
import com.x7ubi.kurswahl.response.common.ResultResponse;
import com.x7ubi.kurswahl.service.admin.user.TeacherCreationService;
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
        teacher.setAbbreviation("NN");
        teacher.setUser(user);

        this.teacherRepo.save(teacher);
    }

    @Test
    public void testCreateTeacher() {
        // Given
        TeacherSignupRequest teacherSignupRequest = new TeacherSignupRequest();
        teacherSignupRequest.setFirstname("Firstname");
        teacherSignupRequest.setSurname("Surname");
        teacherSignupRequest.setUsername("Username");
        teacherSignupRequest.setAbbreviation("NN");

        // When
        ResultResponse response = this.teacherCreationService.registerTeacher(teacherSignupRequest);

        // Then
        Teacher createdTeacher = this.teacherRepo.findTeacherByUser_Username("Username").get();
        Assertions.assertTrue(response.getErrorMessages().isEmpty());
        Assertions.assertEquals(createdTeacher.getUser().getFirstname(), teacherSignupRequest.getFirstname());
        Assertions.assertEquals(createdTeacher.getUser().getSurname(), teacherSignupRequest.getSurname());
        Assertions.assertEquals(createdTeacher.getUser().getUsername(), teacherSignupRequest.getUsername());
        Assertions.assertEquals(createdTeacher.getAbbreviation(), teacherSignupRequest.getAbbreviation());
    }

    @Test
    public void testCreateTeacherUsernameExists() {
        // Given
        TeacherSignupRequest teacherSignupRequest = new TeacherSignupRequest();
        teacherSignupRequest.setFirstname("Firstname");
        teacherSignupRequest.setSurname("Surname");
        teacherSignupRequest.setUsername("test");
        teacherSignupRequest.setAbbreviation("EN");

        // When
        ResultResponse response = this.teacherCreationService.registerTeacher(teacherSignupRequest);

        // Then
        Teacher createdTeacher = this.teacherRepo.findTeacherByUser_Username("test").get();
        Assertions.assertEquals(response.getErrorMessages().size(), 1);
        Assertions.assertEquals(response.getErrorMessages().get(0).getMessage(), ErrorMessage.Authentication.USERNAME_EXITS);
        Assertions.assertEquals(createdTeacher.getUser().getFirstname(), teacher.getUser().getFirstname());
        Assertions.assertEquals(createdTeacher.getUser().getSurname(), teacher.getUser().getSurname());
        Assertions.assertEquals(createdTeacher.getUser().getUsername(), teacher.getUser().getUsername());
        Assertions.assertEquals(createdTeacher.getAbbreviation(), teacher.getAbbreviation());
    }

    @Test
    public void testDeleteTeacher() {
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
    public void testDeleteTeacherWrongId() {
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
