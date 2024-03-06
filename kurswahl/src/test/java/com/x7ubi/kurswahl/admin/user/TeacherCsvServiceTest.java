package com.x7ubi.kurswahl.admin.user;

import com.x7ubi.kurswahl.KurswahlServiceTest;
import com.x7ubi.kurswahl.admin.user.response.TeacherResponse;
import com.x7ubi.kurswahl.admin.user.service.TeacherCsvService;
import com.x7ubi.kurswahl.common.auth.utils.PasswordGenerator;
import com.x7ubi.kurswahl.common.models.Teacher;
import com.x7ubi.kurswahl.common.models.User;
import com.x7ubi.kurswahl.common.repository.TeacherRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@KurswahlServiceTest
public class TeacherCsvServiceTest {

    @Autowired
    private TeacherCsvService teacherCsvService;

    @Autowired
    private TeacherRepo teacherRepo;

    private Teacher teacher;

    private void setupTeacher() {
        User user = new User();
        user.setUsername("test.test");
        user.setFirstname("Test");
        user.setSurname("Test");
        user.setPassword("Password");
        user.setGeneratedPassword(PasswordGenerator.generatePassword());
        teacher = new Teacher();
        teacher.setUser(user);
        teacher.setAbbreviation("NN");

        teacher = this.teacherRepo.save(teacher);
    }

    @Test
    public void testImportCsvCreatesTeacher() {
        // given
        String csv = "NN;Test;Test\nNE;Test;Test";

        // when
        List<TeacherResponse> teacherResponses = this.teacherCsvService.importCsv(csv);

        // then
        Assertions.assertEquals(teacherResponses.size(), 2);

        Assertions.assertEquals(teacherResponses.get(0).getAbbreviation(), "NN");
        Assertions.assertEquals(teacherResponses.get(0).getUsername(), "test.test");
        Assertions.assertEquals(teacherResponses.get(0).getFirstname(), "Test");
        Assertions.assertEquals(teacherResponses.get(0).getSurname(), "Test");
        Assertions.assertNotNull(teacherResponses.get(0).getGeneratedPassword());

        Assertions.assertEquals(teacherResponses.get(1).getAbbreviation(), "NE");
        Assertions.assertEquals(teacherResponses.get(1).getUsername(), "test1.test");
        Assertions.assertEquals(teacherResponses.get(1).getFirstname(), "Test");
        Assertions.assertEquals(teacherResponses.get(1).getSurname(), "Test");
        Assertions.assertNotNull(teacherResponses.get(1).getGeneratedPassword());
    }

    @Test
    public void testImportCsvTeacherExists() {
        // given
        setupTeacher();
        String csv = "NN;Test;Test\nNE;Test;Test";

        // when
        List<TeacherResponse> teacherResponses = this.teacherCsvService.importCsv(csv);

        // then
        Assertions.assertEquals(teacherResponses.size(), 2);

        Assertions.assertEquals(teacherResponses.get(0).getTeacherId(), teacher.getTeacherId());
        Assertions.assertEquals(teacherResponses.get(0).getAbbreviation(), "NN");
        Assertions.assertEquals(teacherResponses.get(0).getUsername(), "test.test");
        Assertions.assertEquals(teacherResponses.get(0).getFirstname(), "Test");
        Assertions.assertEquals(teacherResponses.get(0).getSurname(), "Test");
        Assertions.assertNotNull(teacherResponses.get(0).getGeneratedPassword());

        Assertions.assertEquals(teacherResponses.get(1).getAbbreviation(), "NE");
        Assertions.assertEquals(teacherResponses.get(1).getUsername(), "test1.test");
        Assertions.assertEquals(teacherResponses.get(1).getFirstname(), "Test");
        Assertions.assertEquals(teacherResponses.get(1).getSurname(), "Test");
        Assertions.assertNotNull(teacherResponses.get(1).getGeneratedPassword());
    }

    @Test
    public void testImportCsvWrongFormat() {
        // given
        String csv = "NN;Test";

        // when
        List<TeacherResponse> teacherResponses = this.teacherCsvService.importCsv(csv);

        // then
        Assertions.assertTrue(teacherResponses.isEmpty());
    }
}
