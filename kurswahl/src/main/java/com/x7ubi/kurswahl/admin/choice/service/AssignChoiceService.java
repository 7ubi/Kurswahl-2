package com.x7ubi.kurswahl.admin.choice.service;

import com.x7ubi.kurswahl.admin.choice.mapper.ClassStudentsMapper;
import com.x7ubi.kurswahl.admin.choice.mapper.StudentChoiceMapper;
import com.x7ubi.kurswahl.admin.choice.response.ClassStudentsResponse;
import com.x7ubi.kurswahl.admin.choice.response.StudentChoicesResponse;
import com.x7ubi.kurswahl.common.error.ErrorMessage;
import com.x7ubi.kurswahl.common.exception.EntityNotFoundException;
import com.x7ubi.kurswahl.common.models.Choice;
import com.x7ubi.kurswahl.common.models.ChoiceClass;
import com.x7ubi.kurswahl.common.models.Class;
import com.x7ubi.kurswahl.common.models.Student;
import com.x7ubi.kurswahl.common.repository.ChoiceClassRepo;
import com.x7ubi.kurswahl.common.repository.ClassRepo;
import com.x7ubi.kurswahl.common.repository.StudentRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AssignChoiceService {

    private final Logger logger = LoggerFactory.getLogger(AssignChoiceService.class);

    private final ClassRepo classRepo;

    private final ChoiceClassRepo choiceClassRepo;

    private final ClassStudentsMapper classStudentsMapper;

    private final StudentChoiceMapper studentChoiceMapper;

    private final StudentRepo studentRepo;

    public AssignChoiceService(ClassRepo classRepo, ChoiceClassRepo choiceClassRepo, ClassStudentsMapper classStudentsMapper,
                               StudentChoiceMapper studentChoiceMapper, StudentRepo studentRepo) {
        this.classRepo = classRepo;
        this.choiceClassRepo = choiceClassRepo;
        this.classStudentsMapper = classStudentsMapper;
        this.studentChoiceMapper = studentChoiceMapper;
        this.studentRepo = studentRepo;
    }

    @Transactional
    public List<ClassStudentsResponse> getClassesWithStudents(Integer year) {
        List<Class> classes = this.classRepo.findAllByTapeYearAndTapeReleaseYear(year, Year.now().getValue());
        classes.forEach(c -> {
            List<Student> students = new ArrayList<>();
            c.setChoiceClasses(c.getChoiceClasses().stream().filter(choiceClass -> {
                if (students.contains(choiceClass.getChoice().getStudent())) {
                    return false;
                }
                students.add(choiceClass.getChoice().getStudent());
                return true;
            }).collect(Collectors.toSet()));
        });

        logger.info(String.format("Filtered Students, who chose classes in year %s", year));

        return this.classStudentsMapper.classesToClassChoiceResponses(classes);
    }

    public StudentChoicesResponse getStundetChoices(Long studentId) throws EntityNotFoundException {

        Optional<Student> studentOptional = this.studentRepo.findStudentByStudentId(studentId);

        if (studentOptional.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.STUDENT_NOT_FOUND);
        }

        Student student = studentOptional.get();
        student.setChoices(student.getChoices().stream().filter(choice -> choice.getReleaseYear() ==
                Year.now().getValue()).collect(Collectors.toSet()));

        return this.studentChoiceMapper.studentToStudentChoicesResponse(studentOptional.get());
    }

    @Transactional
    public StudentChoicesResponse assignChoice(Long choiceClassId) throws EntityNotFoundException {
        ChoiceClass choiceClass = getChoiceClass(choiceClassId);
        choiceClass.setSelected(true);
        this.choiceClassRepo.save(choiceClass);

        Set<Choice> choices = choiceClass.getChoice().getStudent().getChoices().stream()
                .filter(choice -> choice.getReleaseYear() == Year.now().getValue()).collect(Collectors.toSet());

        choices.forEach(choice -> choice.getChoiceClasses().forEach(choiceClassOfChoice -> {
            if (Objects.equals(choiceClassOfChoice.getChoiceClassId(), choiceClass.getChoiceClassId())) {
                return;
            }

            if (choiceClassOfChoice.isSelected() &&
                    (Objects.equals(choiceClassOfChoice.getaClass().getSubject().getSubjectId(),
                            choiceClass.getaClass().getSubject().getSubjectId()) ||
                            Objects.equals(choiceClassOfChoice.getaClass().getTape().getTapeId(),
                                    choiceClass.getaClass().getTape().getTapeId()))) {
                choiceClassOfChoice.setSelected(false);
                this.choiceClassRepo.save(choiceClassOfChoice);
            }
        }));

        return getStundetChoices(choiceClass.getChoice().getStudent().getStudentId());
    }

    private ChoiceClass getChoiceClass(Long choiceClassId) throws EntityNotFoundException {
        Optional<ChoiceClass> choiceClassOptional = this.choiceClassRepo.findChoiceClassByChoiceClassId(choiceClassId);

        if (choiceClassOptional.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.CHOICE_NOT_FOUND);
        }

        return choiceClassOptional.get();
    }

    public StudentChoicesResponse deleteChoiceSelection(Long choiceClassId) throws EntityNotFoundException {
        ChoiceClass choiceClass = getChoiceClass(choiceClassId);
        choiceClass.setSelected(false);

        this.choiceClassRepo.save(choiceClass);

        return getStundetChoices(choiceClass.getChoice().getStudent().getStudentId());
    }
}
