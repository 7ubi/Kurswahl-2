package com.x7ubi.kurswahl.admin.choice.service;

import com.x7ubi.kurswahl.admin.choice.mapper.ChoiceTapeMapper;
import com.x7ubi.kurswahl.admin.choice.mapper.ClassStudentsMapper;
import com.x7ubi.kurswahl.admin.choice.mapper.StudentChoiceMapper;
import com.x7ubi.kurswahl.admin.choice.request.AlternateChoiceRequest;
import com.x7ubi.kurswahl.admin.choice.response.ChoiceTapeResponse;
import com.x7ubi.kurswahl.admin.choice.response.ClassStudentsResponse;
import com.x7ubi.kurswahl.admin.choice.response.StudentChoicesResponse;
import com.x7ubi.kurswahl.common.error.ErrorMessage;
import com.x7ubi.kurswahl.common.exception.EntityNotFoundException;
import com.x7ubi.kurswahl.common.models.Class;
import com.x7ubi.kurswahl.common.models.*;
import com.x7ubi.kurswahl.common.repository.*;
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

    private final ChoiceRepo choiceRepo;

    private final TapeRepo tapeRepo;

    private final ChoiceTapeMapper choiceTapeMapper;

    public static final Integer ALTERNATE_CHOICE_NUMBER = 3;

    public AssignChoiceService(ClassRepo classRepo, ChoiceClassRepo choiceClassRepo, ClassStudentsMapper
            classStudentsMapper, StudentChoiceMapper studentChoiceMapper, StudentRepo studentRepo, ChoiceRepo
                                       choiceRepo, TapeRepo tapeRepo, ChoiceTapeMapper choiceTapeMapper) {
        this.classRepo = classRepo;
        this.choiceClassRepo = choiceClassRepo;
        this.classStudentsMapper = classStudentsMapper;
        this.studentChoiceMapper = studentChoiceMapper;
        this.studentRepo = studentRepo;
        this.choiceRepo = choiceRepo;
        this.tapeRepo = tapeRepo;
        this.choiceTapeMapper = choiceTapeMapper;
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

        deselectChoiceClassSameTapeOrSubject(choiceClass);

        return getStundetChoices(choiceClass.getChoice().getStudent().getStudentId());
    }

    @Transactional
    protected void deselectChoiceClassSameTapeOrSubject(ChoiceClass choiceClass) {
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

    @Transactional
    public StudentChoicesResponse assignAlternateChoice(AlternateChoiceRequest alternateChoiceRequest)
            throws EntityNotFoundException {
        Optional<Student> studentOptional = this.studentRepo.findStudentByStudentId(alternateChoiceRequest.getStudentId());
        if (studentOptional.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.STUDENT_NOT_FOUND);
        }
        Student student = studentOptional.get();

        Optional<Class> classOptional = this.classRepo.findClassByClassId(alternateChoiceRequest.getClassId());
        if (classOptional.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.CLASS_NOT_FOUND);
        }
        Class aClass = classOptional.get();

        Optional<Choice> choiceOptional = this.choiceRepo.findChoiceByChoiceNumberAndStudent_StudentIdAndReleaseYear(
                ALTERNATE_CHOICE_NUMBER, student.getStudentId(), Year.now().getValue());
        Choice choice;

        if (choiceOptional.isPresent()) {
            choice = choiceOptional.get();
        } else {
            choice = new Choice();
            choice.setStudent(student);
            choice.setChoiceNumber(ALTERNATE_CHOICE_NUMBER);
            choice.setReleaseYear(Year.now().getValue());
            choice.setChoiceClasses(new HashSet<>());

            this.choiceRepo.save(choice);

            choice = this.choiceRepo.findChoiceByChoiceNumberAndStudent_StudentIdAndReleaseYear(
                    ALTERNATE_CHOICE_NUMBER, student.getStudentId(), Year.now().getValue()).get();
            student.getChoices().add(choice);
            this.studentRepo.save(student);

            logger.info(String.format("Created alternate choice for %s", student.getUser().getUsername()));
        }

        ChoiceClass choiceClass = new ChoiceClass();
        choiceClass.setaClass(aClass);
        choiceClass.setChoice(choice);
        choiceClass.setSelected(true);

        this.choiceClassRepo.save(choiceClass);

        choice.getChoiceClasses().add(choiceClass);
        this.choiceRepo.save(choice);
        aClass.getChoiceClasses().add(choiceClass);
        this.classRepo.save(aClass);

        deselectChoiceClassSameTapeOrSubject(choiceClass);

        return getStundetChoices(choiceClass.getChoice().getStudent().getStudentId());
    }

    @Transactional
    public List<ChoiceTapeResponse> getTapes(Integer year) {
        List<Tape> tapes = this.tapeRepo.findAllByYearAndReleaseYear(year, Year.now().getValue());

        return this.choiceTapeMapper.tapesToChoiceTapeResponses(tapes);
    }

    @Transactional
    public StudentChoicesResponse deleteAlternativeChoiceClass(Long choiceClassId) throws EntityNotFoundException {
        Optional<ChoiceClass> choiceClassOptional = this.choiceClassRepo.findChoiceClassByChoiceClassId(choiceClassId);

        if (choiceClassOptional.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.CHOICE_NOT_FOUND);
        }
        ChoiceClass choiceClass = choiceClassOptional.get();

        choiceClass.getaClass().getChoiceClasses().remove(choiceClass);
        this.classRepo.save(choiceClass.getaClass());
        choiceClass.getChoice().getChoiceClasses().remove(choiceClass);
        this.choiceRepo.save(choiceClass.getChoice());
        this.choiceClassRepo.delete(choiceClass);

        return getStundetChoices(choiceClass.getChoice().getStudent().getStudentId());
    }
}
