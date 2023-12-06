package com.x7ubi.kurswahl.admin.user;

import com.x7ubi.kurswahl.KurswahlServiceTest;
import com.x7ubi.kurswahl.admin.request.TeacherSignupRequest;
import com.x7ubi.kurswahl.admin.response.user.TeacherResponse;
import com.x7ubi.kurswahl.admin.response.user.TeacherResponses;
import com.x7ubi.kurswahl.admin.response.user.TeacherResultResponse;
import com.x7ubi.kurswahl.admin.service.user.TeacherCreationService;
import com.x7ubi.kurswahl.common.error.ErrorMessage;
import com.x7ubi.kurswahl.common.models.Class;
import com.x7ubi.kurswahl.common.models.StudentClass;
import com.x7ubi.kurswahl.common.models.Teacher;
import com.x7ubi.kurswahl.common.models.User;
import com.x7ubi.kurswahl.common.repository.ClassRepo;
import com.x7ubi.kurswahl.common.repository.StudentClassRepo;
import com.x7ubi.kurswahl.common.repository.TeacherRepo;
import com.x7ubi.kurswahl.common.response.ResultResponse;
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

    @Autowired
    private ClassRepo classRepo;

    private Teacher teacher;

    private StudentClass studentClass;

    private Class aClass;

    @BeforeEach
    public void setupTests() {
        User user = new User();
        user.setUsername("test.user");
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

    private void setupClass() {
        aClass = new Class();
        aClass.setName("class");
        aClass.setTeacher(teacher);

        this.classRepo.save(aClass);

        teacher.setStudentClasses(new HashSet<>());
        teacher.getClasses().add(aClass);
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
    public void testEditTeacher() {
        // Given
        this.teacher = this.teacherRepo.findTeacherByUser_Username(this.teacher.getUser().getUsername()).get();
        Long id = this.teacher.getTeacherId();
        TeacherSignupRequest teacherSignupRequest = new TeacherSignupRequest();
        teacherSignupRequest.setFirstname("Firstname");
        teacherSignupRequest.setSurname("Surname");
        teacherSignupRequest.setAbbreviation("NE");

        // When
        ResultResponse response = this.teacherCreationService.editTeacher(id, teacherSignupRequest);

        // Then
        Assertions.assertTrue(response.getErrorMessages().isEmpty());
        Teacher editedTeacher = this.teacherRepo.findTeacherByTeacherId(id).get();
        Assertions.assertEquals(editedTeacher.getUser().getFirstname(), teacherSignupRequest.getFirstname());
        Assertions.assertEquals(editedTeacher.getUser().getSurname(), teacherSignupRequest.getSurname());
        Assertions.assertEquals(editedTeacher.getAbbreviation(), teacherSignupRequest.getAbbreviation());
        Assertions.assertEquals(editedTeacher.getUser().getUsername(), "test.user");
    }

    @Test
    public void testEditTeacherWrongId() {
        // Given
        this.teacher = this.teacherRepo.findTeacherByUser_Username(this.teacher.getUser().getUsername()).get();
        Long id = this.teacher.getTeacherId() + 1;
        TeacherSignupRequest teacherSignupRequest = new TeacherSignupRequest();
        teacherSignupRequest.setFirstname("Firstname");
        teacherSignupRequest.setSurname("Surname");
        teacherSignupRequest.setAbbreviation("NE");

        // When
        ResultResponse response = this.teacherCreationService.editTeacher(id, teacherSignupRequest);

        // Then
        Assertions.assertEquals(response.getErrorMessages().size(), 1);
        Assertions.assertEquals(response.getErrorMessages().get(0).getMessage(),
                ErrorMessage.Administration.TEACHER_NOT_FOUND);

        Teacher editedTeacher = this.teacherRepo.findTeacherByTeacherId(this.teacher.getTeacherId()).get();
        Assertions.assertEquals(editedTeacher.getUser().getFirstname(), teacher.getUser().getFirstname());
        Assertions.assertEquals(editedTeacher.getUser().getSurname(), teacher.getUser().getSurname());
        Assertions.assertEquals(editedTeacher.getAbbreviation(), teacher.getAbbreviation());
        Assertions.assertEquals(editedTeacher.getUser().getUsername(), "test.user");
    }

    @Test
    public void testGetTeacher() {
        // Given
        this.teacher = this.teacherRepo.findTeacherByUser_Username(this.teacher.getUser().getUsername()).get();
        Long id = this.teacher.getTeacherId();

        // When
        TeacherResultResponse response = this.teacherCreationService.getTeacher(id);

        // Then
        Assertions.assertTrue(response.getErrorMessages().isEmpty());

        TeacherResponse teacherResponse = response.getTeacherResponse();
        Assertions.assertEquals(teacherResponse.getFirstname(), teacher.getUser().getFirstname());
        Assertions.assertEquals(teacherResponse.getSurname(), teacher.getUser().getSurname());
        Assertions.assertEquals(teacherResponse.getAbbreviation(), teacher.getAbbreviation());
        Assertions.assertEquals(teacherResponse.getUsername(), "test.user");
    }

    @Test
    public void testGetTeacherWrongId() {
        // Given
        this.teacher = this.teacherRepo.findTeacherByUser_Username(this.teacher.getUser().getUsername()).get();
        Long id = this.teacher.getTeacherId() + 1;

        // When
        TeacherResultResponse response = this.teacherCreationService.getTeacher(id);

        // Then
        Assertions.assertEquals(response.getErrorMessages().size(), 1);
        Assertions.assertEquals(response.getErrorMessages().get(0).getMessage(),
                ErrorMessage.Administration.TEACHER_NOT_FOUND);

        TeacherResponse teacherResponse = response.getTeacherResponse();
        Assertions.assertNull(teacherResponse);
    }

    @Test
    public void testGetAllTeachers() {
        // When
        TeacherResponses response = this.teacherCreationService.getAllTeachers();

        // When
        Assertions.assertEquals(response.getTeacherResponses().size(), 1);

        TeacherResponse teacherResponse = response.getTeacherResponses().get(0);
        Assertions.assertEquals(teacherResponse.getFirstname(), teacher.getUser().getFirstname());
        Assertions.assertEquals(teacherResponse.getSurname(), teacher.getUser().getSurname());
        Assertions.assertEquals(teacherResponse.getAbbreviation(), teacher.getAbbreviation());
        Assertions.assertEquals(teacherResponse.getUsername(), "test.user");
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
        Assertions.assertEquals(response.getErrorMessages().get(0).getMessage(),
                ErrorMessage.Administration.TEACHER_NOT_FOUND);
        Assertions.assertTrue(this.teacherRepo.existsTeacherByUser_Username(this.teacher.getUser().getUsername()));
    }

    @Test
    public void testDeleteTeacherPartOfStudentClass() {
        // Given
        this.setupStudentClass();
        this.teacher = this.teacherRepo.findTeacherByUser_Username(this.teacher.getUser().getUsername()).get();
        Long id = this.teacher.getTeacherId();

        // When
        ResultResponse response = this.teacherCreationService.deleteTeacher(id);

        // Then
        Assertions.assertEquals(response.getErrorMessages().size(), 1);
        Assertions.assertEquals(response.getErrorMessages().get(0).getMessage(),
                ErrorMessage.Administration.TEACHER_STUDENT_CLASS);
        Assertions.assertTrue(this.teacherRepo.existsTeacherByUser_Username(this.teacher.getUser().getUsername()));
    }

    @Test
    public void testDeleteTeacherPartOfClass() {
        // Given
        this.setupClass();
        this.teacher = this.teacherRepo.findTeacherByUser_Username(this.teacher.getUser().getUsername()).get();
        Long id = this.teacher.getTeacherId();

        // When
        ResultResponse response = this.teacherCreationService.deleteTeacher(id);

        // Then
        Assertions.assertEquals(response.getErrorMessages().size(), 1);
        Assertions.assertEquals(response.getErrorMessages().get(0).getMessage(),
                ErrorMessage.Administration.TEACHER_CLASS);
        Assertions.assertTrue(this.teacherRepo.existsTeacherByUser_Username(this.teacher.getUser().getUsername()));
    }
}
