package com.x7ubi.kurswahl.student.choice.service;

import com.x7ubi.kurswahl.common.error.ErrorMessage;
import com.x7ubi.kurswahl.common.exception.EntityNotFoundException;
import com.x7ubi.kurswahl.common.exception.UnauthorizedException;
import com.x7ubi.kurswahl.common.models.Class;
import com.x7ubi.kurswahl.common.models.*;
import com.x7ubi.kurswahl.common.repository.*;
import com.x7ubi.kurswahl.common.rule.service.RuleService;
import com.x7ubi.kurswahl.student.choice.mapper.ChoiceMapper;
import com.x7ubi.kurswahl.student.choice.mapper.SubjectTapeMapper;
import com.x7ubi.kurswahl.student.choice.mapper.TapeClassMapper;
import com.x7ubi.kurswahl.student.choice.request.AlterStudentChoiceRequest;
import com.x7ubi.kurswahl.student.choice.request.DeleteClassFromChoiceRequest;
import com.x7ubi.kurswahl.student.choice.response.ChoiceResponse;
import com.x7ubi.kurswahl.student.choice.response.SubjectTapeResponse;
import com.x7ubi.kurswahl.student.choice.response.TapeClassResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudentChoiceService {

    private final ChoiceRepo choiceRepo;
    private final ClassRepo classRepo;
    private final TapeRepo tapeRepo;
    private final StudentRepo studentRepo;
    private final SubjectRepo subjectRepo;
    private final ChoiceClassRepo choiceClassRepo;
    private final ChoiceMapper choiceMapper;
    private final TapeClassMapper tapeClassMapper;
    private final SubjectTapeMapper subjectTapeMapper;
    private final RuleService ruleService;
    Logger logger = LoggerFactory.getLogger(StudentChoiceService.class);

    public StudentChoiceService(ChoiceRepo choiceRepo, ClassRepo classRepo, TapeRepo tapeRepo, StudentRepo studentRepo,
                                SubjectRepo subjectRepo, ChoiceClassRepo choiceClassRepo, ChoiceMapper choiceMapper,
                                TapeClassMapper tapeClassMapper, SubjectTapeMapper subjectTapeMapper,
                                RuleService ruleService) {
        this.choiceRepo = choiceRepo;
        this.classRepo = classRepo;
        this.tapeRepo = tapeRepo;
        this.studentRepo = studentRepo;
        this.subjectRepo = subjectRepo;
        this.choiceClassRepo = choiceClassRepo;
        this.choiceMapper = choiceMapper;
        this.tapeClassMapper = tapeClassMapper;
        this.subjectTapeMapper = subjectTapeMapper;
        this.ruleService = ruleService;
    }

    @Transactional
    public ChoiceResponse alterChoice(String username, AlterStudentChoiceRequest alterStudentChoiceRequest)
            throws EntityNotFoundException, UnauthorizedException {
        Student student = getStudent(username);

        Optional<Class> classOptional = this.classRepo.findClassByClassId(alterStudentChoiceRequest.getClassId());
        if (classOptional.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.CLASS_NOT_FOUND);
        }
        Class aClass = classOptional.get();

        if (alterStudentChoiceRequest.getChoiceNumber() < 1 || alterStudentChoiceRequest.getChoiceNumber() > 2) {
            throw new UnauthorizedException(ErrorMessage.INVALID_CHOICE_NUMBER);
        }

        Optional<Choice> choiceOptional = this.choiceRepo.findChoiceByChoiceNumberAndStudent_StudentIdAndReleaseYear(
                alterStudentChoiceRequest.getChoiceNumber(), student.getStudentId(), Year.now().getValue());
        Choice choice;

        if (choiceOptional.isPresent()) {
            choice = choiceOptional.get();
        } else {
            if (alterStudentChoiceRequest.getChoiceNumber() == 2) {
                selectChoiceClassesFirstChoice(student);
            }

            choice = this.choiceMapper.choiceRequestToChoice(alterStudentChoiceRequest);
            choice.setStudent(student);
            choice.setReleaseYear(Year.now().getValue());
            choice.setChoiceClasses(new HashSet<>());

            choice = this.choiceRepo.save(choice);
            
            student.getChoices().add(choice);
            this.studentRepo.save(student);

            logger.info(String.format("Created new choice for %s", student.getUser().getUsername()));
        }

        setClassToChoice(choice, aClass);
        logger.info(String.format("Altered choice for %s", student.getUser().getUsername()));

        ChoiceResponse choiceResponse = this.choiceMapper.choiceToChoiceResponse(choice);
        choiceResponse.setRuleResponses(this.ruleService.getRulesByChoiceClasses(student.getStudentClass().getYear(), choice.getChoiceClasses()));
        return choiceResponse;
    }

    @Transactional
    protected void selectChoiceClassesFirstChoice(Student student) {

        Optional<Choice> choiceOptional = this.choiceRepo.findChoiceByChoiceNumberAndStudent_StudentIdAndReleaseYear(
                1, student.getStudentId(), Year.now().getValue());

        if (choiceOptional.isPresent()) {
            Choice choice = choiceOptional.get();
            for (ChoiceClass choiceClass : choice.getChoiceClasses()) {
                choiceClass.setSelected(true);
                this.choiceClassRepo.save(choiceClass);
            }
        }
    }

    private void setClassToChoice(Choice choice, Class aClass) {
        Optional<ChoiceClass> choiceClassWithTapeOptional = choice.getChoiceClasses().stream().filter(c ->
                Objects.equals(c.getaClass().getTape().getTapeId(), aClass.getTape().getTapeId())).findFirst();

        if (choiceClassWithTapeOptional.isPresent()) {
            ChoiceClass choiceClass = choiceClassWithTapeOptional.get();
            choice.getChoiceClasses().remove(choiceClass);
            choiceClass.getaClass().getChoiceClasses().remove(choiceClass);

            this.classRepo.save(choiceClass.getaClass());
            this.choiceClassRepo.delete(choiceClass);
        }

        Optional<ChoiceClass> choiceClassWithSubjectOptional = choice.getChoiceClasses().stream().filter(c ->
                Objects.equals(c.getaClass().getSubject().getSubjectId(), aClass.getSubject().getSubjectId())).findFirst();


        if (choiceClassWithSubjectOptional.isPresent()) {
            ChoiceClass choiceClass = choiceClassWithSubjectOptional.get();
            choice.getChoiceClasses().remove(choiceClass);
            choiceClass.getaClass().getChoiceClasses().remove(choiceClass);

            this.classRepo.save(choiceClass.getaClass());
            this.choiceClassRepo.delete(choiceClass);
        }

        ChoiceClass choiceClass = new ChoiceClass();
        choiceClass.setaClass(aClass);
        choiceClass.setChoice(choice);

        this.choiceClassRepo.save(choiceClass);

        choice.getChoiceClasses().add(choiceClass);
        this.choiceRepo.save(choice);
        aClass.getChoiceClasses().add(choiceClass);
        this.classRepo.save(aClass);
    }

    @Transactional
    public List<TapeClassResponse> getTapesForChoice(String username) throws EntityNotFoundException {
        Student student = getStudent(username);
        List<Tape> tapes = this.tapeRepo.findAllByYearAndReleaseYear(student.getStudentClass().getYear(),
                Year.now().getValue());

        logger.info(String.format("Found all Tapes of year %s", student.getStudentClass().getYear()));
        return this.tapeClassMapper.tapesToTapeResponses(tapes);
    }

    @Transactional
    public List<SubjectTapeResponse> getTapesOfSubjects(String username) throws EntityNotFoundException {
        Student student = getStudent(username);

        List<Subject> subjects = this.subjectRepo.findAll();

        subjects.forEach(subject ->
                subject.setClasses(subject.getClasses().stream().filter(c -> c.getTape() != null &&
                        Objects.equals(c.getTape().getYear(), student.getStudentClass().getYear()) &&
                        Objects.equals(c.getTape().getReleaseYear(), Year.now().getValue())).collect(Collectors.toSet()))
        );

        return this.subjectTapeMapper.subjectsToSubjectTapeResponses(subjects);
    }

    public ChoiceResponse getChoice(String username, Integer choiceNumber) throws EntityNotFoundException {
        Student student = getStudent(username);


        Optional<Choice> choiceOptional = this.choiceRepo.findChoiceByChoiceNumberAndStudent_StudentIdAndReleaseYear(
                choiceNumber, student.getStudentId(), Year.now().getValue());

        if (choiceOptional.isEmpty()) {
            ChoiceResponse choiceResponse = new ChoiceResponse();
            choiceResponse.setRuleResponses(this.ruleService.getAllRules(student.getStudentClass().getYear()));
            return choiceResponse;
        }

        Choice choice = choiceOptional.get();

        logger.info("Found choice");
        ChoiceResponse choiceResponse = this.choiceMapper.choiceToChoiceResponse(choice);
        choiceResponse.setRuleResponses(this.ruleService.getRulesByChoiceClasses(student.getStudentClass().getYear(), choice.getChoiceClasses()));
        return choiceResponse;
    }

    public List<ChoiceResponse> getChoices(String username) throws EntityNotFoundException {
        Student student = getStudent(username);

        List<Choice> choices = this.choiceRepo.findAllByStudent_StudentIdAndReleaseYear(student.getStudentId(),
                Year.now().getValue()).stream().filter(choice -> choice.getChoiceNumber() == 1 ||
                choice.getChoiceNumber() == 2).toList();

        if (choices.size() < 2) {
            throw new EntityNotFoundException(ErrorMessage.NOT_ENOUGH_CHOICES);
        }

        return this.choiceMapper.choicesToChoiceResponses(choices);
    }

    private Student getStudent(String username) throws EntityNotFoundException {
        Optional<Student> studentOptional = this.studentRepo.findStudentByUser_Username(username);
        if (studentOptional.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.STUDENT_NOT_FOUND);
        }
        return studentOptional.get();
    }

    @Transactional
    public ChoiceResponse deleteClassFromChoice(DeleteClassFromChoiceRequest deleteClassFromChoiceRequest)
            throws EntityNotFoundException {
        Optional<Choice> choiceOptional
                = this.choiceRepo.findChoiceByChoiceId(deleteClassFromChoiceRequest.getChoiceId());

        if (choiceOptional.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.CHOICE_NOT_FOUND);
        }
        Choice choice = choiceOptional.get();

        Optional<ChoiceClass> choiceClassOptional = choice.getChoiceClasses().stream().filter(c ->
                Objects.equals(c.getaClass().getClassId(), deleteClassFromChoiceRequest.getClassId())).findFirst();

        if (choiceClassOptional.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.CLASS_NOT_IN_CHOICE);
        }

        ChoiceClass choiceClass = choiceClassOptional.get();

        choice.getChoiceClasses().remove(choiceClass);

        choiceClass.getaClass().getChoiceClasses().remove(choiceClass);

        this.choiceRepo.save(choice);

        this.classRepo.save(choiceClass.getaClass());

        this.choiceClassRepo.delete(choiceClass);

        logger.info(String.format("Removed %s from Choice", choiceClass.getaClass().getName()));
        ChoiceResponse choiceResponse = this.choiceMapper.choiceToChoiceResponse(choice);
        choiceResponse.setRuleResponses(this.ruleService.getRulesByChoiceClasses(choice.getStudent().getStudentClass().getYear(), choice.getChoiceClasses()));
        return choiceResponse;
    }
}
