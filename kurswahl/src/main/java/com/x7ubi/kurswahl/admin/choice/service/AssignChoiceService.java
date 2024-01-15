package com.x7ubi.kurswahl.admin.choice.service;

import com.x7ubi.kurswahl.admin.choice.mapper.ClassStudentsMapper;
import com.x7ubi.kurswahl.admin.choice.response.ClassStudentsResponse;
import com.x7ubi.kurswahl.common.models.Class;
import com.x7ubi.kurswahl.common.models.Student;
import com.x7ubi.kurswahl.common.repository.ClassRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AssignChoiceService {

    private final Logger logger = LoggerFactory.getLogger(AssignChoiceService.class);

    private final ClassRepo classRepo;

    private final ClassStudentsMapper classStudentsMapper;

    public AssignChoiceService(ClassRepo classRepo, ClassStudentsMapper classStudentsMapper) {
        this.classRepo = classRepo;
        this.classStudentsMapper = classStudentsMapper;
    }

    public List<ClassStudentsResponse> getClassesWithStudents(Integer year) {
        List<Class> classes = this.classRepo.findAllByTapeYearAndTapeReleaseYear(year, Year.now().getValue());
        classes.forEach(c -> {
            List<Student> students = new ArrayList<>();
            c.setChoices(c.getChoices().stream().filter(choice -> {
                if (students.contains(choice.getStudent())) {
                    return false;
                }
                students.add(choice.getStudent());
                return true;
            }).collect(Collectors.toSet()));
        });

        logger.info(String.format("Filtered Students, who chose classes in year %s", year));

        return this.classStudentsMapper.classesToClassChoiceResponses(classes);
    }
}
