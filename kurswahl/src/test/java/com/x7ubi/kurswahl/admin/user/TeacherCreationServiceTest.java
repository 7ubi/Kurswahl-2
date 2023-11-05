package com.x7ubi.kurswahl.admin.user;

import com.x7ubi.kurswahl.KurswahlServiceTest;
import com.x7ubi.kurswahl.error.ErrorMessage;
import com.x7ubi.kurswahl.models.StudentClass;
import com.x7ubi.kurswahl.models.Teacher;
import com.x7ubi.kurswahl.models.User;
import com.x7ubi.kurswahl.repository.StudentClassRepo;
import com.x7ubi.kurswahl.repository.TeacherRepo;
import com.x7ubi.kurswahl.request.admin.TeacherSignupRequest;
import com.x7ubi.kurswahl.response.common.ResultResponse;
import com.x7ubi.kurswahl.service.admin.user.TeacherCreationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Year;
import java.util.HashSet;

@KurswahlServiceTest
public class TeacherCreationServiceTest {

    @Autowired
    private TeacherCreationService teacherCreationService;

    @Autowired
    private TeacherRepo teacherRepo;

    @Autowired
    private StudentClassRepo studentClassRepo;

    private Teacher teacher;

    private StudentClass studentClass;

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
    }

    @Test
    public void testCreateTeacher() {
        // Given
        TeacherSignupRequest teacherSignupRequest = new TeacherSignupRequest();
        teacherSignupRequest.setFirstname("Firstname");
        teacherSignupRequest.setSurname("Surname");
        teacherSignupRequest.setAbbreviation("NN");

        // When
        this.teacherCreationService.registerTeacher(teacherSignupRequest);

        // Then
        Teacher createdTeacher = this.teacherRepo.findTeacherByUser_Username("firstname.surname").get();
        Assertions.assertEquals(createdTeacher.getUser().getFirstname(), teacherSignupRequest.getFirstname());
        Assertions.assertEquals(createdTeacher.getUser().getSurname(), teacherSignupRequest.getSurname());
        Assertions.assertEquals(createdTeacher.getAbbreviation(), teacherSignupRequest.getAbbreviation());
        Assertions.assertEquals(createdTeacher.getUser().getUsername(),
                String.format("%s.%s", teacherSignupRequest.getFirstname().toLowerCase(),
                        teacherSignupRequest.getSurname().toLowerCase()));
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

    @Test
    public void testDeleteTeacherStudentClass() {
        // Given
        this.setupStudentClass();
        this.teacher = this.teacherRepo.findTeacherByUser_Username(this.teacher.getUser().getUsername()).get();
        Long id = this.teacher.getTeacherId();

        // When
        ResultResponse response = this.teacherCreationService.deleteTeacher(id);

        // Then
        Assertions.assertEquals(response.getErrorMessages().size(), 1);
        Assertions.assertEquals(response.getErrorMessages().get(0).getMessage(), ErrorMessage.Administration.TEACHER_STUDENT_CLASS);
        Assertions.assertTrue(this.teacherRepo.existsTeacherByUser_Username(this.teacher.getUser().getUsername()));
    }
}
