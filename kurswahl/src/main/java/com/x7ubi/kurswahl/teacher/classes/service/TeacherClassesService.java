package com.x7ubi.kurswahl.teacher.classes.service;

import com.x7ubi.kurswahl.common.models.ChoiceClass;
import com.x7ubi.kurswahl.common.models.Class;
import com.x7ubi.kurswahl.common.repository.ClassRepo;
import com.x7ubi.kurswahl.teacher.classes.mapper.TeacherClassesMapper;
import com.x7ubi.kurswahl.teacher.classes.response.TeacherClassResponse;
import com.x7ubi.kurswahl.teacher.classes.response.TeacherClassStudentResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeacherClassesService {

    private final ClassRepo classRepo;

    private final TeacherClassesMapper teacherClassesMapper;

    public TeacherClassesService(ClassRepo classRepo, TeacherClassesMapper teacherClassesMapper) {
        this.classRepo = classRepo;
        this.teacherClassesMapper = teacherClassesMapper;
    }

    @Transactional(readOnly = true)
    public List<TeacherClassResponse> getTeacherClasses(String username) {
        List<Class> classes = this.classRepo.findAllByTeacher_User_UsernameAndTape_ReleaseYear(username, Year.now().getValue());
        classes.forEach(c -> {
            if (c.getChoiceClasses() != null) {
                c.setChoiceClasses(c.getChoiceClasses().stream().filter(ChoiceClass::isSelected).collect(Collectors.toSet()));
            }
        });
        List<TeacherClassResponse> teacherClassRespons = this.teacherClassesMapper.mapClassesToClassResponses(classes);
        teacherClassRespons.forEach(classResponse -> {
            if (classResponse.getTeacherClassStudentResponses() != null) {
                classResponse.getTeacherClassStudentResponses().sort(Comparator.comparing(TeacherClassStudentResponse::getSurname));
            }
        });
        return teacherClassRespons;
    }
}
