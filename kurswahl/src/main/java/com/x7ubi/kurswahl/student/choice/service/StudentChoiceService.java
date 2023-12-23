package com.x7ubi.kurswahl.student.choice.service;

import com.x7ubi.kurswahl.common.error.ErrorMessage;
import com.x7ubi.kurswahl.common.exception.EntityNotFoundException;
import com.x7ubi.kurswahl.common.exception.UnauthorizedException;
import com.x7ubi.kurswahl.common.models.Class;
import com.x7ubi.kurswahl.common.models.*;
import com.x7ubi.kurswahl.common.repository.*;
import com.x7ubi.kurswahl.student.choice.mapper.ChoiceMapper;
import com.x7ubi.kurswahl.student.choice.mapper.SubjectTapeMapper;
import com.x7ubi.kurswahl.student.choice.mapper.TapeClassMapper;
import com.x7ubi.kurswahl.student.choice.request.AlterStudentChoiceRequest;
import com.x7ubi.kurswahl.student.choice.request.DeleteClassFromChoiceRequest;
import com.x7ubi.kurswahl.student.choice.response.ChoiceResponse;
import com.x7ubi.kurswahl.student.choice.response.SubjectTapeResponse;
import com.x7ubi.kurswahl.student.choice.response.TapeClassResponse;
import com.x7ubi.kurswahl.student.choice.response.TapeResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StudentChoiceService {

    Logger logger = LoggerFactory.getLogger(StudentChoiceService.class);

    private final ChoiceRepo choiceRepo;

    private final ClassRepo classRepo;

    private final TapeRepo tapeRepo;

    private final StudentRepo studentRepo;

    private final SubjectRepo subjectRepo;

    private final ChoiceMapper choiceMapper;

    private final TapeClassMapper tapeClassMapper;

    private final SubjectTapeMapper subjectTapeMapper;

    public StudentChoiceService(ChoiceRepo choiceRepo, ClassRepo classRepo, TapeRepo tapeRepo, StudentRepo studentRepo,
                                SubjectRepo subjectRepo, ChoiceMapper choiceMapper, TapeClassMapper tapeClassMapper,
                                SubjectTapeMapper subjectTapeMapper) {
        this.choiceRepo = choiceRepo;
        this.classRepo = classRepo;
        this.tapeRepo = tapeRepo;
        this.studentRepo = studentRepo;
        this.subjectRepo = subjectRepo;
        this.choiceMapper = choiceMapper;
        this.tapeClassMapper = tapeClassMapper;
        this.subjectTapeMapper = subjectTapeMapper;
    }

    @Transactional
    public void alterChoice(String username, AlterStudentChoiceRequest alterStudentChoiceRequest)
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
            choice = this.choiceMapper.choiceRequestToChoice(alterStudentChoiceRequest);
            choice.setStudent(student);
            choice.setReleaseYear(Year.now().getValue());
            choice.setClasses(new HashSet<>());

            this.choiceRepo.save(choice);
            student.getChoices().add(choice);
            this.studentRepo.save(student);

            choice = this.choiceRepo.findChoiceByChoiceNumberAndStudent_StudentIdAndReleaseYear(
                    alterStudentChoiceRequest.getChoiceNumber(), student.getStudentId(), Year.now().getValue()).get();
            logger.info(String.format("Created new choice for %s", student.getUser().getUsername()));
        }

        setClassToChoice(choice, aClass);
        logger.info(String.format("Altered choice for %s", student.getUser().getUsername()));
    }

    private void setClassToChoice(Choice choice, Class aClass) {
        Optional<Class> classWithTapeOptional = choice.getClasses().stream().filter(c ->
                Objects.equals(c.getTape().getTapeId(), aClass.getTape().getTapeId())).findFirst();

        if (classWithTapeOptional.isPresent()) {
            Class classWithTape = classWithTapeOptional.get();
            choice.getClasses().remove(classWithTape);
            classWithTape.getChoices().remove(choice);

            this.classRepo.save(classWithTape);
        }

        Optional<Class> classWithSubjectOptional = choice.getClasses().stream().filter(c ->
                Objects.equals(c.getSubject().getSubjectId(), aClass.getSubject().getSubjectId())).findFirst();


        if (classWithSubjectOptional.isPresent()) {
            Class classWithSubject = classWithSubjectOptional.get();
            choice.getClasses().remove(classWithSubject);
            classWithSubject.getChoices().remove(choice);

            this.classRepo.save(classWithSubject);
        }

        choice.getClasses().add(aClass);
        this.choiceRepo.save(choice);
        aClass.getChoices().add(choice);
        this.classRepo.save(aClass);
    }

    @Transactional
    public TapeResponses getTapes(String username) throws EntityNotFoundException {
        TapeResponses tapeResponses = new TapeResponses();
        Student student = getStudent(username);
        tapeResponses.setTapeClassResponses(getTapesForChoice(student));
        tapeResponses.setSubjectTapeResponses(getTapesOfSubjects(student));

        return tapeResponses;
    }

    @Transactional
    public List<TapeClassResponse> getTapesForChoice(Student student) {
        List<Tape> tapes = this.tapeRepo.findAllByYearAndReleaseYear(student.getStudentClass().getYear(),
                Year.now().getValue()).get();

        logger.info(String.format("Found all Tapes of year %s", student.getStudentClass().getYear()));
        return this.tapeClassMapper.tapesToTapeResponses(tapes);
    }

    @Transactional
    public List<SubjectTapeResponse> getTapesOfSubjects(Student student) throws EntityNotFoundException {

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
            return new ChoiceResponse();
        }

        Choice choice = choiceOptional.get();

        logger.info("Found choice");
        return this.choiceMapper.choiceToChoiceResponse(choice);
    }

    public List<ChoiceResponse> getChoices(String username) throws EntityNotFoundException {
        Student student = getStudent(username);

        Optional<List<Choice>> choicesOptional = this.choiceRepo.findAllByStudent_StudentIdAndReleaseYearAndChoiceNumberOrChoiceNumber(
                student.getStudentId(), Year.now().getValue(), 1, 2);

        if (choicesOptional.isEmpty()) {
            return new ArrayList<>();
        }

        List<Choice> choices = choicesOptional.get();

        return this.choiceMapper.choicesToChoiceResponses(choices);
    }

    private Student getStudent(String username) throws EntityNotFoundException {
        Optional<Student> studentOptional = this.studentRepo.findStudentByUser_Username(username);
        if (studentOptional.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.STUDENT_NOT_FOUND);
        }
        return studentOptional.get();
    }

    public void deleteClassFromChoice(DeleteClassFromChoiceRequest deleteClassFromChoiceRequest)
            throws EntityNotFoundException {
        Optional<Choice> choiceOptional
                = this.choiceRepo.findChoiceByChoiceId(deleteClassFromChoiceRequest.getChoiceId());

        if (choiceOptional.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.CHOICE_NOT_FOUND);
        }
        Choice choice = choiceOptional.get();

        Optional<Class> classOptional = choice.getClasses().stream().filter(c -> Objects.equals(c.getClassId(),
                deleteClassFromChoiceRequest.getClassId())).findFirst();

        if (classOptional.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.CLASS_NOT_IN_CHOICE);
        }

        Class aclass = classOptional.get();

        choice.getClasses().remove(aclass);

        aclass.getChoices().remove(choice);

        this.choiceRepo.save(choice);

        this.classRepo.save(aclass);

        logger.info(String.format("Removed %s from Choice", aclass.getName()));
    }
}
