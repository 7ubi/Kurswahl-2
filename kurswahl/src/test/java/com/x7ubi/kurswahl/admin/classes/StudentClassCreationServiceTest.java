package com.x7ubi.kurswahl.admin.classes;

import com.x7ubi.kurswahl.KurswahlServiceTest;
import com.x7ubi.kurswahl.admin.classes.request.StudentClassCreationRequest;
import com.x7ubi.kurswahl.admin.classes.response.StudentClassResponse;
import com.x7ubi.kurswahl.admin.classes.response.StudentClassResponses;
import com.x7ubi.kurswahl.admin.classes.response.StudentClassResultResponse;
import com.x7ubi.kurswahl.admin.classes.service.StudentClassCreationService;
import com.x7ubi.kurswahl.common.error.ErrorMessage;
import com.x7ubi.kurswahl.common.models.StudentClass;
import com.x7ubi.kurswahl.common.models.Teacher;
import com.x7ubi.kurswahl.common.models.User;
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
public class StudentClassCreationServiceTest {
    @Autowired
    private StudentClassCreationService studentClassCreationService;

    @Autowired
    private StudentClassRepo studentClassRepo;

    @Autowired
    private TeacherRepo teacherRepo;

    private Teacher teacher;

    private StudentClass studentClass;

    private Teacher teacherOther;

    private StudentClass studentClassOther;

    @BeforeEach
    public void setupTest() {
        setupTeacher();
        setupStudentClass();
        setupOtherTeacher();
        setupOtherStudentClass();
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

        teacher.setStudentClasses(new HashSet<>());
        teacher.getStudentClasses().add(studentClass);
        this.teacherRepo.save(teacher);
    }

    private void setupOtherTeacher() {
        User user = new User();
        user.setUsername("test other");
        user.setFirstname("Test");
        user.setSurname("User");
        user.setPassword("Password");
        teacherOther = new Teacher();
        teacherOther.setAbbreviation("NO");
        teacherOther.setUser(user);

        this.teacherRepo.save(teacherOther);
        teacherOther = this.teacherRepo.findTeacherByUser_Username(teacherOther.getUser().getUsername()).get();
    }

    private void setupOtherStudentClass() {
        studentClassOther = new StudentClass();
        studentClassOther.setTeacher(teacher);
        studentClassOther.setName("Q2b");
        studentClassOther.setReleaseYear(Year.now().getValue());
        studentClassOther.setYear(12);

        this.studentClassRepo.save(studentClassOther);

        teacherOther.setStudentClasses(new HashSet<>());
        teacherOther.getStudentClasses().add(studentClassOther);
        this.teacherRepo.save(teacherOther);
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
                = this.studentClassRepo.findStudentClassByName(studentClassCreationRequest.getName()).get();
        Assertions.assertEquals(createdStudentClass.getName(), studentClassCreationRequest.getName());
        Assertions.assertEquals(createdStudentClass.getYear(), studentClassCreationRequest.getYear());
        Assertions.assertEquals(createdStudentClass.getTeacher().getTeacherId(),
                studentClassCreationRequest.getTeacherId());
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
                = this.studentClassRepo.findStudentClassByName(studentClassCreationRequest.getName()).get();
        Assertions.assertEquals(createdStudentClass.getName(), studentClass.getName());
        Assertions.assertEquals(createdStudentClass.getYear(), studentClass.getYear());
        Assertions.assertEquals(createdStudentClass.getTeacher().getTeacherId(),
                studentClass.getTeacher().getTeacherId());
        Assertions.assertEquals(createdStudentClass.getReleaseYear(), Year.now().getValue());
    }

    @Test
    public void testCreateStudentClassTeacherWrongId() {
        // Given
        teacher = this.teacherRepo.findTeacherByUser_Username(teacher.getUser().getUsername()).get();
        StudentClassCreationRequest studentClassCreationRequest = new StudentClassCreationRequest();
        studentClassCreationRequest.setName("E2a");
        studentClassCreationRequest.setYear(11);
        studentClassCreationRequest.setTeacherId(teacher.getTeacherId() + 3);

        // When
        ResultResponse response = this.studentClassCreationService.createStudentClass(studentClassCreationRequest);

        // Then
        Assertions.assertEquals(response.getErrorMessages().size(), 1);
        Assertions.assertEquals(response.getErrorMessages().get(0).getMessage(),
                ErrorMessage.Administration.TEACHER_NOT_FOUND);

        Assertions.assertFalse(this.studentClassRepo.existsStudentClassByName(studentClassCreationRequest.getName()));
    }

    @Test
    public void testEditStudentClass() {
        // Given
        StudentClassCreationRequest studentClassCreationRequest = new StudentClassCreationRequest();
        studentClassCreationRequest.setName("E2a");
        studentClassCreationRequest.setYear(11);
        studentClassCreationRequest.setTeacherId(teacherOther.getTeacherId());

        // When
        ResultResponse response = this.studentClassCreationService.editStudentClass(studentClass.getStudentClassId(),
                studentClassCreationRequest);

        // Then
        Assertions.assertTrue(response.getErrorMessages().isEmpty());

        StudentClass editedStudentClass
                = this.studentClassRepo.findStudentClassByName(studentClassCreationRequest.getName()).get();
        Assertions.assertEquals(editedStudentClass.getStudentClassId(), studentClass.getStudentClassId());
        Assertions.assertEquals(editedStudentClass.getName(), studentClassCreationRequest.getName());
        Assertions.assertEquals(editedStudentClass.getYear(), studentClassCreationRequest.getYear());
        Assertions.assertEquals(editedStudentClass.getTeacher().getTeacherId(),
                studentClassCreationRequest.getTeacherId());
        Assertions.assertEquals(editedStudentClass.getReleaseYear(), Year.now().getValue());

        teacher = this.teacherRepo.findTeacherByTeacherId(teacher.getTeacherId()).get();
        teacherOther = this.teacherRepo.findTeacherByTeacherId(teacherOther.getTeacherId()).get();
        Assertions.assertTrue(teacher.getStudentClasses().isEmpty());
        Assertions.assertEquals(teacherOther.getStudentClasses().size(), 2);
    }

    @Test
    public void testEditStudentClassSameName() {
        // Given
        StudentClassCreationRequest studentClassCreationRequest = new StudentClassCreationRequest();
        studentClassCreationRequest.setName("Q2a");
        studentClassCreationRequest.setYear(11);
        studentClassCreationRequest.setTeacherId(teacherOther.getTeacherId());

        // When
        ResultResponse response = this.studentClassCreationService.editStudentClass(studentClass.getStudentClassId(),
                studentClassCreationRequest);

        // Then
        Assertions.assertTrue(response.getErrorMessages().isEmpty());

        StudentClass editedStudentClass
                = this.studentClassRepo.findStudentClassByName(studentClassCreationRequest.getName()).get();
        Assertions.assertEquals(editedStudentClass.getStudentClassId(), studentClass.getStudentClassId());
        Assertions.assertEquals(editedStudentClass.getName(), studentClassCreationRequest.getName());
        Assertions.assertEquals(editedStudentClass.getYear(), studentClassCreationRequest.getYear());
        Assertions.assertEquals(editedStudentClass.getTeacher().getTeacherId(),
                studentClassCreationRequest.getTeacherId());
        Assertions.assertEquals(editedStudentClass.getReleaseYear(), Year.now().getValue());

        teacher = this.teacherRepo.findTeacherByTeacherId(teacher.getTeacherId()).get();
        teacherOther = this.teacherRepo.findTeacherByTeacherId(teacherOther.getTeacherId()).get();
        Assertions.assertTrue(teacher.getStudentClasses().isEmpty());
        Assertions.assertEquals(teacherOther.getStudentClasses().size(), 2);
    }

    @Test
    public void testEditStudentClassAlreadyExists() {
        // Given
        StudentClassCreationRequest studentClassCreationRequest = new StudentClassCreationRequest();
        studentClassCreationRequest.setName("Q2b");
        studentClassCreationRequest.setYear(11);
        studentClassCreationRequest.setTeacherId(teacherOther.getTeacherId());

        // When
        ResultResponse response = this.studentClassCreationService.editStudentClass(studentClass.getStudentClassId(),
                studentClassCreationRequest);

        // Then
        Assertions.assertEquals(response.getErrorMessages().size(), 1);
        Assertions.assertEquals(response.getErrorMessages().get(0).getMessage(),
                ErrorMessage.Administration.STUDENT_CLASS_ALREADY_EXISTS);

        StudentClass editedStudentClass
                = this.studentClassRepo.findStudentClassByName(studentClass.getName()).get();
        Assertions.assertEquals(editedStudentClass.getStudentClassId(), studentClass.getStudentClassId());
        Assertions.assertEquals(editedStudentClass.getName(), studentClass.getName());
        Assertions.assertEquals(editedStudentClass.getYear(), studentClass.getYear());
        Assertions.assertEquals(editedStudentClass.getTeacher().getTeacherId(),
                studentClass.getTeacher().getTeacherId());
        Assertions.assertEquals(editedStudentClass.getReleaseYear(), Year.now().getValue());

        teacher = this.teacherRepo.findTeacherByTeacherId(teacher.getTeacherId()).get();
        teacherOther = this.teacherRepo.findTeacherByTeacherId(teacherOther.getTeacherId()).get();
        Assertions.assertEquals(teacher.getStudentClasses().size(), 1);
        Assertions.assertEquals(teacherOther.getStudentClasses().size(), 1);
    }

    @Test
    public void testEditStudentClassWrongStudentClassId() {
        // Given
        StudentClassCreationRequest studentClassCreationRequest = new StudentClassCreationRequest();
        studentClassCreationRequest.setName("E2a");
        studentClassCreationRequest.setYear(11);
        studentClassCreationRequest.setTeacherId(teacherOther.getTeacherId());

        // When
        ResultResponse response = this.studentClassCreationService
                .editStudentClass(studentClass.getStudentClassId() + 3, studentClassCreationRequest);

        // Then
        Assertions.assertEquals(response.getErrorMessages().size(), 1);
        Assertions.assertEquals(response.getErrorMessages().get(0).getMessage(),
                ErrorMessage.Administration.STUDENT_CLASS_NOT_FOUND);

        StudentClass editedStudentClass
                = this.studentClassRepo.findStudentClassByName(studentClass.getName()).get();
        Assertions.assertEquals(editedStudentClass.getStudentClassId(), studentClass.getStudentClassId());
        Assertions.assertEquals(editedStudentClass.getName(), studentClass.getName());
        Assertions.assertEquals(editedStudentClass.getYear(), studentClass.getYear());
        Assertions.assertEquals(editedStudentClass.getTeacher().getTeacherId(),
                studentClass.getTeacher().getTeacherId());
        Assertions.assertEquals(editedStudentClass.getReleaseYear(), Year.now().getValue());

        teacher = this.teacherRepo.findTeacherByTeacherId(teacher.getTeacherId()).get();
        teacherOther = this.teacherRepo.findTeacherByTeacherId(teacherOther.getTeacherId()).get();
        Assertions.assertEquals(teacher.getStudentClasses().size(), 1);
        Assertions.assertEquals(teacherOther.getStudentClasses().size(), 1);
    }

    @Test
    public void testEditStudentClassTeacherId() {
        // Given
        StudentClassCreationRequest studentClassCreationRequest = new StudentClassCreationRequest();
        studentClassCreationRequest.setName("E2a");
        studentClassCreationRequest.setYear(11);
        studentClassCreationRequest.setTeacherId(teacherOther.getTeacherId() + 3);

        // When
        ResultResponse response = this.studentClassCreationService.editStudentClass(studentClass.getStudentClassId(),
                studentClassCreationRequest);

        // Then
        Assertions.assertEquals(response.getErrorMessages().size(), 1);
        Assertions.assertEquals(response.getErrorMessages().get(0).getMessage(),
                ErrorMessage.Administration.TEACHER_NOT_FOUND);

        StudentClass editedStudentClass
                = this.studentClassRepo.findStudentClassByName(studentClass.getName()).get();
        Assertions.assertEquals(editedStudentClass.getStudentClassId(), studentClass.getStudentClassId());
        Assertions.assertEquals(editedStudentClass.getName(), studentClass.getName());
        Assertions.assertEquals(editedStudentClass.getYear(), studentClass.getYear());
        Assertions.assertEquals(editedStudentClass.getTeacher().getTeacherId(),
                studentClass.getTeacher().getTeacherId());
        Assertions.assertEquals(editedStudentClass.getReleaseYear(), Year.now().getValue());

        teacher = this.teacherRepo.findTeacherByTeacherId(teacher.getTeacherId()).get();
        teacherOther = this.teacherRepo.findTeacherByTeacherId(teacherOther.getTeacherId()).get();
        Assertions.assertEquals(teacher.getStudentClasses().size(), 1);
        Assertions.assertEquals(teacherOther.getStudentClasses().size(), 1);
    }

    @Test
    public void testGetAllStudentClasses() {
        // When
        StudentClassResponses responses = this.studentClassCreationService.getAllStudentClasses();

        // Then
        Assertions.assertEquals(responses.getStudentClassResponses().size(), 2);

        StudentClassResponse studentClassResponse = responses.getStudentClassResponses().get(0);
        Assertions.assertEquals(studentClassResponse.getStudentClassId(), studentClass.getStudentClassId());
        Assertions.assertEquals(studentClassResponse.getName(), studentClass.getName());
        Assertions.assertEquals(studentClassResponse.getYear(), studentClass.getYear());
        Assertions.assertEquals(studentClassResponse.getTeacher().getTeacherId(),
                studentClass.getTeacher().getTeacherId());
        Assertions.assertEquals(studentClassResponse.getReleaseYear(), Year.now().getValue());

        StudentClassResponse studentClassResponse2 = responses.getStudentClassResponses().get(1);
        Assertions.assertEquals(studentClassResponse2.getStudentClassId(), studentClassOther.getStudentClassId());
        Assertions.assertEquals(studentClassResponse2.getName(), studentClassOther.getName());
        Assertions.assertEquals(studentClassResponse2.getYear(), studentClassOther.getYear());
        Assertions.assertEquals(studentClassResponse2.getTeacher().getTeacherId(),
                studentClassOther.getTeacher().getTeacherId());
        Assertions.assertEquals(studentClassResponse2.getReleaseYear(), Year.now().getValue());
    }

    @Test
    public void testGetStudentClass() {
        // Given
        Long id = this.studentClass.getStudentClassId();

        // When
        StudentClassResultResponse response = this.studentClassCreationService.getStudentClass(id);

        // Then
        Assertions.assertTrue(response.getErrorMessages().isEmpty());

        StudentClassResponse studentClassResponse = response.getStudentClassResponse();

        Assertions.assertEquals(studentClassResponse.getStudentClassId(), studentClass.getStudentClassId());
        Assertions.assertEquals(studentClassResponse.getName(), studentClass.getName());
        Assertions.assertEquals(studentClassResponse.getYear(), studentClass.getYear());
        Assertions.assertEquals(studentClassResponse.getTeacher().getTeacherId(),
                studentClass.getTeacher().getTeacherId());
        Assertions.assertEquals(studentClassResponse.getReleaseYear(), Year.now().getValue());
    }

    @Test
    public void testGetStudentClassWrongId() {
        // Given
        Long id = this.studentClass.getStudentClassId() + 3;

        // When
        StudentClassResultResponse response = this.studentClassCreationService.getStudentClass(id);

        // Then
        Assertions.assertEquals(response.getErrorMessages().size(), 1);
        Assertions.assertEquals(response.getErrorMessages().get(0).getMessage(),
                ErrorMessage.Administration.STUDENT_CLASS_NOT_FOUND);

        Assertions.assertNull(response.getStudentClassResponse());
    }

    @Test
    public void testDeleteStudentClass() {
        // Given
        studentClass = this.studentClassRepo.findStudentClassByName(studentClass.getName()).get();

        // When
        ResultResponse response = this.studentClassCreationService.deleteStudentClass(studentClass.getStudentClassId());

        // Then
        Assertions.assertTrue(response.getErrorMessages().isEmpty());
        Assertions.assertFalse(this.studentClassRepo.existsStudentClassByName(studentClass.getName()));

        teacher = this.teacherRepo.findTeacherByTeacherId(teacher.getTeacherId()).get();
        Assertions.assertTrue(teacher.getStudentClasses().isEmpty());
    }

    @Test
    public void testDeleteStudentClassWrongId() {
        // Given
        studentClass = this.studentClassRepo.findStudentClassByName(studentClass.getName()).get();

        // When
        ResultResponse response = this.studentClassCreationService.deleteStudentClass(studentClass.getStudentClassId() + 3);

        // Then
        Assertions.assertEquals(response.getErrorMessages().size(), 1);
        Assertions.assertEquals(response.getErrorMessages().get(0).getMessage(), ErrorMessage.Administration.STUDENT_CLASS_NOT_FOUND);
        Assertions.assertTrue(this.studentClassRepo.existsStudentClassByName(studentClass.getName()));

        teacher = this.teacherRepo.findTeacherByTeacherId(teacher.getTeacherId()).get();
        Assertions.assertEquals(teacher.getStudentClasses().size(), 1);
    }
}
