package com.x7ubi.kurswahl.admin.user;

import com.x7ubi.kurswahl.KurswahlServiceTest;
import com.x7ubi.kurswahl.admin.user.request.StudentCsvRequest;
import com.x7ubi.kurswahl.admin.user.response.StudentResponse;
import com.x7ubi.kurswahl.admin.user.service.StudentCsvService;
import com.x7ubi.kurswahl.common.auth.utils.PasswordGenerator;
import com.x7ubi.kurswahl.common.models.Student;
import com.x7ubi.kurswahl.common.models.StudentClass;
import com.x7ubi.kurswahl.common.models.User;
import com.x7ubi.kurswahl.common.repository.StudentClassRepo;
import com.x7ubi.kurswahl.common.repository.StudentRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Year;
import java.util.List;

@KurswahlServiceTest
public class StudentCsvServiceTest {

    @Autowired
    private StudentCsvService studentCsvService;

    @Autowired
    private StudentRepo studentRepo;

    @Autowired
    private StudentClassRepo studentClassRepo;

    private StudentClass studentClass;

    private void setupStudentClass(Integer releaseYear) {
        studentClass = new StudentClass();
        studentClass.setName("E2a");
        studentClass.setReleaseYear(releaseYear);
        studentClass.setYear(11);

        this.studentClassRepo.save(studentClass);

        studentClass = this.studentClassRepo.findStudentClassByName("E2a").get();
    }

    private void setupStudent() {
        User user = new User();
        user.setUsername("test.test");
        user.setFirstname("Test");
        user.setSurname("Test");
        user.setPassword("Password");
        user.setGeneratedPassword(PasswordGenerator.generatePassword());
        Student student = new Student();
        student.setUser(user);

        student.setStudentClass(studentClass);
        this.studentRepo.save(student);
        studentClass.getStudents().add(student);
        this.studentClassRepo.save(studentClass);
    }

    @Test
    public void testImportCsvCreateStudentAndStudentClass() {
        // Given
        StudentCsvRequest studentCsvRequest = new StudentCsvRequest();
        studentCsvRequest.setYear(11);
        studentCsvRequest.setCsv("E2_a;Test;Test\nE2_a;Test;Test\nE2_b;Max;Muster");

        // When
        List<StudentResponse> response = this.studentCsvService.importCsv(studentCsvRequest);

        // Then
        Assertions.assertEquals(response.size(), 3);

        Assertions.assertEquals(response.get(0).getUsername(), "test.test");
        Assertions.assertEquals(response.get(0).getFirstname(), "Test");
        Assertions.assertEquals(response.get(0).getSurname(), "Test");
        Assertions.assertEquals(response.get(0).getStudentClassResponse().getName(), "E2_a");
        Assertions.assertNotNull(response.get(0).getGeneratedPassword());

        Assertions.assertEquals(response.get(1).getUsername(), "test1.test");
        Assertions.assertEquals(response.get(1).getFirstname(), "Test");
        Assertions.assertEquals(response.get(1).getSurname(), "Test");
        Assertions.assertEquals(response.get(1).getStudentClassResponse().getName(), "E2_a");
        Assertions.assertNotNull(response.get(1).getGeneratedPassword());

        Assertions.assertEquals(response.get(2).getUsername(), "max.muster");
        Assertions.assertEquals(response.get(2).getFirstname(), "Max");
        Assertions.assertEquals(response.get(2).getSurname(), "Muster");
        Assertions.assertEquals(response.get(2).getStudentClassResponse().getName(), "E2_b");
        Assertions.assertNotNull(response.get(2).getGeneratedPassword());
    }

    @Test
    public void testImportCsvStundenClassExist() {
        // Given
        setupStudentClass(Year.now().getValue());

        StudentCsvRequest studentCsvRequest = new StudentCsvRequest();
        studentCsvRequest.setYear(11);
        studentCsvRequest.setCsv("E2a;Test;Test");

        // When
        List<StudentResponse> response = this.studentCsvService.importCsv(studentCsvRequest);

        // Then
        Assertions.assertEquals(response.size(), 1);

        Assertions.assertEquals(response.get(0).getUsername(), "test.test");
        Assertions.assertEquals(response.get(0).getFirstname(), "Test");
        Assertions.assertEquals(response.get(0).getSurname(), "Test");
        Assertions.assertEquals(response.get(0).getStudentClassResponse().getName(), "E2a");
        Assertions.assertNotNull(response.get(0).getGeneratedPassword());
    }

    @Test
    public void testImportCsvStudentClassExists() {
        // Given
        setupStudentClass(Year.now().getValue());

        StudentCsvRequest studentCsvRequest = new StudentCsvRequest();
        studentCsvRequest.setYear(11);
        studentCsvRequest.setCsv("E2a;Test;Test");

        // When
        List<StudentResponse> response = this.studentCsvService.importCsv(studentCsvRequest);

        // Then
        Assertions.assertEquals(response.size(), 1);

        Assertions.assertEquals(response.get(0).getUsername(), "test.test");
        Assertions.assertEquals(response.get(0).getFirstname(), "Test");
        Assertions.assertEquals(response.get(0).getSurname(), "Test");
        Assertions.assertEquals(response.get(0).getStudentClassResponse().getName(), "E2a");
        Assertions.assertEquals(response.get(0).getStudentClassResponse().getStudentClassId(),
                studentClass.getStudentClassId());
        Assertions.assertNotNull(response.get(0).getGeneratedPassword());
    }

    @Test
    public void testImportCsvStudentFromLastYearExists() {
        // Given
        setupStudentClass(Year.now().getValue() - 1);
        setupStudent();

        StudentCsvRequest studentCsvRequest = new StudentCsvRequest();
        studentCsvRequest.setYear(11);
        studentCsvRequest.setCsv("E2a;Test;Test");

        // When
        List<StudentResponse> response = this.studentCsvService.importCsv(studentCsvRequest);

        // Then
        Assertions.assertEquals(response.size(), 1);

        Assertions.assertEquals(response.get(0).getUsername(), "test.test");
        Assertions.assertEquals(response.get(0).getFirstname(), "Test");
        Assertions.assertEquals(response.get(0).getSurname(), "Test");
        Assertions.assertEquals(response.get(0).getStudentClassResponse().getName(), "E2a");
        Assertions.assertEquals(response.get(0).getStudentClassResponse().getReleaseYear(), Year.now().getValue());
        Assertions.assertNotNull(response.get(0).getGeneratedPassword());
    }

    @Test
    public void testImportCsvStudentCsvError() {
        // Given
        StudentCsvRequest studentCsvRequest = new StudentCsvRequest();
        studentCsvRequest.setYear(11);
        studentCsvRequest.setCsv("E2a;Test");

        // When
        List<StudentResponse> response = this.studentCsvService.importCsv(studentCsvRequest);

        // Then
        Assertions.assertTrue(response.isEmpty());
    }
}
