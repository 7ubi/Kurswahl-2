package com.x7ubi.kurswahl.teacher.classes;

import com.x7ubi.kurswahl.KurswahlServiceTest;
import com.x7ubi.kurswahl.common.models.Class;
import com.x7ubi.kurswahl.common.models.*;
import com.x7ubi.kurswahl.common.repository.ClassRepo;
import com.x7ubi.kurswahl.teacher.classes.mapper.TeacherClassesMapper;
import com.x7ubi.kurswahl.teacher.classes.response.TeacherClassResponse;
import com.x7ubi.kurswahl.teacher.classes.service.TeacherClassesService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Year;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.when;

@KurswahlServiceTest
public class TeacherClassesServiceTest {

    @Autowired
    private TeacherClassesService teacherClassesService;

    @Mock
    private ClassRepo classRepo;

    @Autowired
    private TeacherClassesMapper teacherClassesMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        teacherClassesService = new TeacherClassesService(classRepo, teacherClassesMapper);
    }

    private Class setupClass(boolean isSelected) {
        Student student = new Student();
        student.setUser(new User());
        student.getUser().setSurname("surname");
        student.getUser().setFirstname("firstname");
        student.getUser().setUsername("firstname.surname");

        Choice choice = new Choice();
        choice.setStudent(student);

        Class class1 = new Class();

        ChoiceClass choiceClass = new ChoiceClass();
        choiceClass.setSelected(isSelected);
        choiceClass.setaClass(class1);
        choiceClass.setChoice(choice);

        class1.setChoiceClasses(Set.of(choiceClass));

        return class1;
    }

    @Test
    public void testGetTeacherClasses() {
        // Given
        String username = "teacher";

        List<Class> classes = List.of(setupClass(true));

        when(classRepo.findAllByTeacher_User_UsernameAndTape_ReleaseYear(username, Year.now().getValue())).thenReturn(classes);

        // When
        List<TeacherClassResponse> result = teacherClassesService.getTeacherClasses(username);

        // Then
        Assertions.assertEquals(result.size(), 1);
        Assertions.assertEquals(result.get(0).getTeacherClassStudentResponses().size(), 1);
    }

    @Test
    public void testGetTeacherClassesNotSelected() {
        // Given
        String username = "teacher";

        List<Class> classes = List.of(setupClass(false));

        when(classRepo.findAllByTeacher_User_UsernameAndTape_ReleaseYear(username, Year.now().getValue())).thenReturn(classes);

        // When
        List<TeacherClassResponse> result = teacherClassesService.getTeacherClasses(username);

        // Then
        Assertions.assertEquals(result.size(), 1);
        Assertions.assertTrue(result.get(0).getTeacherClassStudentResponses().isEmpty());
    }

    @Test
    public void testGetTeacherClassesEmptyClass() {
        // Given
        String username = "teacher";
        Class class1 = new Class();
        List<Class> classes = List.of(class1);

        when(classRepo.findAllByTeacher_User_UsernameAndTape_ReleaseYear(username, Year.now().getValue())).thenReturn(classes);

        // When
        List<TeacherClassResponse> result = teacherClassesService.getTeacherClasses(username);

        // Then
        Assertions.assertEquals(result.size(), 1);
        Assertions.assertNull(result.get(0).getTeacherClassStudentResponses());
    }
}
