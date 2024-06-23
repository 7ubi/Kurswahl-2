package com.x7ubi.kurswahl.admin.choice.service;

import com.x7ubi.kurswahl.admin.choice.mapper.ChoiceTapeMapper;
import com.x7ubi.kurswahl.admin.choice.mapper.ClassStudentsMapper;
import com.x7ubi.kurswahl.admin.choice.mapper.StudentChoiceMapper;
import com.x7ubi.kurswahl.admin.choice.request.AlternateChoiceRequest;
import com.x7ubi.kurswahl.admin.choice.request.AlternateChoicesRequest;
import com.x7ubi.kurswahl.admin.choice.request.AssignChoicesRequest;
import com.x7ubi.kurswahl.admin.choice.response.*;
import com.x7ubi.kurswahl.common.error.ErrorMessage;
import com.x7ubi.kurswahl.common.exception.EntityNotFoundException;
import com.x7ubi.kurswahl.common.models.Class;
import com.x7ubi.kurswahl.common.models.*;
import com.x7ubi.kurswahl.common.repository.*;
import com.x7ubi.kurswahl.common.rule.service.RuleService;
import com.x7ubi.kurswahl.common.settings.service.SettingsService;
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

    private final RuleService ruleService;

    private final SettingsService settingsService;

    public static final Integer ALTERNATE_CHOICE_NUMBER = 3;

    public AssignChoiceService(ClassRepo classRepo, ChoiceClassRepo choiceClassRepo, ClassStudentsMapper
            classStudentsMapper, StudentChoiceMapper studentChoiceMapper, StudentRepo studentRepo, ChoiceRepo
                                       choiceRepo, TapeRepo tapeRepo, ChoiceTapeMapper choiceTapeMapper,
                               RuleService ruleService, SettingsService settingsService) {
        this.classRepo = classRepo;
        this.choiceClassRepo = choiceClassRepo;
        this.classStudentsMapper = classStudentsMapper;
        this.studentChoiceMapper = studentChoiceMapper;
        this.studentRepo = studentRepo;
        this.choiceRepo = choiceRepo;
        this.tapeRepo = tapeRepo;
        this.choiceTapeMapper = choiceTapeMapper;
        this.ruleService = ruleService;
        this.settingsService = settingsService;
    }

    @Transactional(readOnly = true)
    public List<ClassStudentsResponse> getClassesWithStudents(Integer year) {
        List<Class> classes = this.classRepo.findAllByTapeYearAndTapeReleaseYear(year, Year.now().getValue());
        classes.forEach(c -> {
            List<Student> students = new ArrayList<>();
            c.setChoiceClasses(c.getChoiceClasses().stream().filter(choiceClass -> {
                if (students.contains(choiceClass.getChoice().getStudent()) || !choiceClass.isSelected()) {
                    return false;
                }
                students.add(choiceClass.getChoice().getStudent());
                return true;
            }).collect(Collectors.toSet()));
        });

        logger.info(String.format("Filtered Students, who chose classes in year %s", year));

        List<ClassStudentsResponse> response = this.classStudentsMapper.classesToClassChoiceResponses(classes);
        ChoiceHelper.setClassStudentsResponseWarnings(response, settingsService);
        return response;
    }

    public StudentChoicesResponse getStudentChoices(Long studentId) throws EntityNotFoundException {

        Optional<Student> studentOptional = this.studentRepo.findStudentByStudentId(studentId);

        if (studentOptional.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.STUDENT_NOT_FOUND);
        }

        Student student = studentOptional.get();
        student.setChoices(student.getChoices().stream().filter(choice -> choice.getReleaseYear() ==
                Year.now().getValue()).collect(Collectors.toSet()));

        Set<ChoiceClass> choiceClasses
                = this.choiceClassRepo.findAllByChoice_Student_StudentIdAndChoice_ReleaseYearAndSelected(studentId,
                Year.now().getValue(), true);

        StudentChoicesResponse studentChoicesResponse = this.studentChoiceMapper.studentToStudentChoicesResponse(studentOptional.get());
        studentChoicesResponse.setRuleResponses(this.ruleService.getRulesByChoiceClasses(student.getStudentClass().getYear(), choiceClasses));
        return studentChoicesResponse;
    }

    @Transactional(readOnly = true)
    public StudentsChoicesResponse getStudentsChoices(List<Long> studentIds) {
        List<Student> students = this.studentRepo.findAllByStudentIdIn(studentIds);

        StudentsChoicesResponse studentsChoicesResponses = new StudentsChoicesResponse();

        studentsChoicesResponses.setChoiceResponses(getChoicesInCommon(students));

        List<StudentRuleResponse> studentRuleResponses = new ArrayList<>();
        students.forEach(student -> {
            StudentRuleResponse studentRuleResponse = this.studentChoiceMapper.studentToStudentRuleResponse(student);
            Set<ChoiceClass> choiceClasses
                    = this.choiceClassRepo.findAllByChoice_Student_StudentIdAndChoice_ReleaseYearAndSelected(student.getStudentId(),
                    Year.now().getValue(), true);


            studentRuleResponse.setRuleResponses(this.ruleService.getRulesByChoiceClasses(student.getStudentClass().getYear(), choiceClasses));
            studentRuleResponses.add(studentRuleResponse);
        });
        studentsChoicesResponses.setStudentRuleResponses(studentRuleResponses);
        return studentsChoicesResponses;
    }

    private List<ChoiceResponse> getChoicesInCommon(List<Student> students) {
        List<ChoiceResponse> choiceResponses = List.of(new ChoiceResponse(), new ChoiceResponse(), new ChoiceResponse());
        choiceResponses.get(0).setChoiceNumber(1);
        choiceResponses.get(1).setChoiceNumber(2);
        choiceResponses.get(2).setChoiceNumber(3);

        students.get(0).getChoices().forEach(choice -> choiceResponses.get(choice.getChoiceNumber() - 1).setClassChoiceResponses(
                this.studentChoiceMapper.choiceClassSetToClassChoiceResponseList(choice.getChoiceClasses())));

        students.forEach(student -> student.getChoices().forEach(choice -> {
            if (choiceResponses.get(choice.getChoiceNumber() - 1).getClassChoiceResponses() == null) {
                return;
            }

            choiceResponses.get(choice.getChoiceNumber() - 1).getClassChoiceResponses().removeIf(
                    classChoiceResponse -> {
                        if (choice.getChoiceClasses() == null) {
                            return false;
                        }

                        Optional<ChoiceClass> choiceClassOptional = choice.getChoiceClasses().stream().filter(
                                choiceClass -> Objects.equals(choiceClass.getaClass().getClassId(),
                                        classChoiceResponse.getClassId())).findFirst();

                        if (choiceClassOptional.isEmpty()) {
                            return true;
                        }

                        classChoiceResponse.setSelected(choiceClassOptional.get().isSelected() && classChoiceResponse.isSelected());

                        return false;
                    }
            );
        }));

        return choiceResponses;
    }

    @Transactional
    public StudentChoicesResponse assignChoice(Long choiceClassId) throws EntityNotFoundException {
        ChoiceClass choiceClass = getChoiceClass(choiceClassId);
        choiceClass.setSelected(true);
        this.choiceClassRepo.save(choiceClass);

        deselectChoiceClassSameTapeOrSubject(choiceClass);

        return getStudentChoices(choiceClass.getChoice().getStudent().getStudentId());
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

    @Transactional
    public StudentChoicesResponse deleteChoiceSelection(Long choiceClassId) throws EntityNotFoundException {
        ChoiceClass choiceClass = getChoiceClass(choiceClassId);
        choiceClass.setSelected(false);

        this.choiceClassRepo.save(choiceClass);

        return getStudentChoices(choiceClass.getChoice().getStudent().getStudentId());
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

            choice = this.choiceRepo.save(choice);

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

        return getStudentChoices(choiceClass.getChoice().getStudent().getStudentId());
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

        return getStudentChoices(choiceClass.getChoice().getStudent().getStudentId());
    }

    @Transactional
    public StudentsChoicesResponse assignChoices(AssignChoicesRequest assignChoicesRequest) {
        List<Student> students = this.studentRepo.findAllByStudentIdIn(assignChoicesRequest.getStudentIds());

        students.forEach(student -> {
            student.getChoices().forEach(choice -> {
                if (!Objects.equals(choice.getChoiceNumber(), assignChoicesRequest.getChoiceNumber())) {
                    return;
                }
                choice.getChoiceClasses().forEach(choiceClass -> {
                    if (Objects.equals(assignChoicesRequest.getClassId(), choiceClass.getaClass().getClassId())) {
                        choiceClass.setSelected(true);
                        deselectChoiceClassSameTapeOrSubject(choiceClass);
                    }
                });
            });
        });

        return getStudentsChoices(assignChoicesRequest.getStudentIds());
    }

    @Transactional
    public StudentsChoicesResponse assignAlternateChoices(AlternateChoicesRequest alternateChoicesRequest) throws EntityNotFoundException {
        for (Long studentId : alternateChoicesRequest.getStudentIds()) {
            assignAlternateChoice(new AlternateChoiceRequest(alternateChoicesRequest.getClassId(), studentId));
        }

        return getStudentsChoices(alternateChoicesRequest.getStudentIds());
    }
}
