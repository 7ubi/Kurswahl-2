package com.x7ubi.kurswahl.student.choice.service;

import com.x7ubi.kurswahl.common.error.ErrorMessage;
import com.x7ubi.kurswahl.common.exception.EntityNotFoundException;
import com.x7ubi.kurswahl.common.exception.UnauthorizedException;
import com.x7ubi.kurswahl.common.models.Choice;
import com.x7ubi.kurswahl.common.models.Class;
import com.x7ubi.kurswahl.common.models.Student;
import com.x7ubi.kurswahl.common.models.Tape;
import com.x7ubi.kurswahl.common.repository.ChoiceRepo;
import com.x7ubi.kurswahl.common.repository.ClassRepo;
import com.x7ubi.kurswahl.common.repository.StudentRepo;
import com.x7ubi.kurswahl.common.repository.TapeRepo;
import com.x7ubi.kurswahl.student.choice.mapper.ChoiceMapper;
import com.x7ubi.kurswahl.student.choice.mapper.TapeClassMapper;
import com.x7ubi.kurswahl.student.choice.request.AlterStudentChoiceRequest;
import com.x7ubi.kurswahl.student.choice.request.DeleteClassFromChoiceRequest;
import com.x7ubi.kurswahl.student.choice.response.ChoiceResponse;
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

@Service
public class StudentChoiceService {

    Logger logger = LoggerFactory.getLogger(StudentChoiceService.class);

    private final ChoiceRepo choiceRepo;

    private final ClassRepo classRepo;

    private final TapeRepo tapeRepo;

    private final StudentRepo studentRepo;

    private final ChoiceMapper choiceMapper;

    private final TapeClassMapper tapeClassMapper;

    public StudentChoiceService(ChoiceRepo choiceRepo, ClassRepo classRepo, TapeRepo tapeRepo, StudentRepo studentRepo,
                                ChoiceMapper choiceMapper, TapeClassMapper tapeClassMapper) {
        this.choiceRepo = choiceRepo;
        this.classRepo = classRepo;
        this.tapeRepo = tapeRepo;
        this.studentRepo = studentRepo;
        this.choiceMapper = choiceMapper;
        this.tapeClassMapper = tapeClassMapper;
    }

    @Transactional
    public void alterChoice(String username, AlterStudentChoiceRequest alterStudentChoiceRequest)
            throws EntityNotFoundException, UnauthorizedException {
        Optional<Student> studentOptional = this.studentRepo.findStudentByUser_Username(username);
        if (studentOptional.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.STUDENT_NOT_FOUND);
        }
        Student student = studentOptional.get();

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
    public List<TapeClassResponse> getTapesForChoice(String username) throws EntityNotFoundException {
        Optional<Student> studentOptional = this.studentRepo.findStudentByUser_Username(username);
        if (studentOptional.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.STUDENT_NOT_FOUND);
        }
        Student student = studentOptional.get();

        List<Tape> tapes = this.tapeRepo.findAllByYearAndReleaseYear(student.getStudentClass().getYear(),
                Year.now().getValue()).get();


        logger.info(String.format("Found all Tapes of year %s", student.getStudentClass().getYear()));
        return this.tapeClassMapper.tapesToTapeResponses(tapes);
    }

    public ChoiceResponse getChoice(String username, Integer choiceNumber) throws EntityNotFoundException {
        Optional<Student> studentOptional = this.studentRepo.findStudentByUser_Username(username);
        if (studentOptional.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.STUDENT_NOT_FOUND);
        }
        Student student = studentOptional.get();


        Optional<Choice> choiceOptional = this.choiceRepo.findChoiceByChoiceNumberAndStudent_StudentIdAndReleaseYear(
                choiceNumber, student.getStudentId(), Year.now().getValue());

        if (choiceOptional.isEmpty()) {
            return new ChoiceResponse();
        }

        Choice choice = choiceOptional.get();

        logger.info("Found choice");
        return this.choiceMapper.choiceToChoiceResponse(choice);
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
