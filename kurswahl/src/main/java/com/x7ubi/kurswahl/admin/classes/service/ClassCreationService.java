package com.x7ubi.kurswahl.admin.classes.service;

import com.x7ubi.kurswahl.admin.classes.mapper.ClassMapper;
import com.x7ubi.kurswahl.admin.classes.request.ClassCreationRequest;
import com.x7ubi.kurswahl.admin.classes.response.ClassResponse;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ClassCreationService {
    private final Logger logger = LoggerFactory.getLogger(ClassCreationService.class);

    private final ClassRepo classRepo;

    private final TapeRepo tapeRepo;

    private final TeacherRepo teacherRepo;

    private final SubjectRepo subjectRepo;

    private final ChoiceRepo choiceRepo;

    private final ClassMapper classMapper;

    public ClassCreationService(ClassRepo classRepo, TapeRepo tapeRepo, TeacherRepo teacherRepo,
                                SubjectRepo subjectRepo, ChoiceRepo choiceRepo, ClassMapper classMapper) {
        this.classRepo = classRepo;
        this.tapeRepo = tapeRepo;
        this.teacherRepo = teacherRepo;
        this.subjectRepo = subjectRepo;
        this.choiceRepo = choiceRepo;
        this.classMapper = classMapper;
    }

    @Transactional
    public void createClass(ClassCreationRequest classCreationRequest) throws EntityNotFoundException {

        Class aclass = this.classMapper.classRequestToClass(classCreationRequest);
        Optional<Teacher> teacherOptional = this.teacherRepo.findTeacherByTeacherId(classCreationRequest.getTeacherId());
        if (teacherOptional.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.TEACHER_NOT_FOUND);
        }
        Teacher teacher = teacherOptional.get();
        aclass.setTeacher(teacher);
        Optional<Subject> subjectOptional = this.subjectRepo.findSubjectBySubjectId(classCreationRequest.getSubjectId());
        if (subjectOptional.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.SUBJECT_NOT_FOUND);
        }
        Subject subject = subjectOptional.get();
        aclass.setSubject(subject);
        Optional<Tape> tapeOptional = this.tapeRepo.findTapeByTapeId(classCreationRequest.getTapeId());
        if (tapeOptional.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.TAPE_NOT_FOUND);
        }
        Tape tape = tapeOptional.get();
        aclass.setTape(tape);

        this.classRepo.save(aclass);

        tape.getaClass().add(aclass);
        subject.getClasses().add(aclass);
        teacher.getClasses().add(aclass);
        this.teacherRepo.save(teacher);
        this.subjectRepo.save(subject);
        this.tapeRepo.save(tape);

        logger.info(String.format("Created class %s", aclass.getName()));
    }

    @Transactional
    public void editClass(Long classId, ClassCreationRequest classCreationRequest) throws EntityNotFoundException {
        Optional<Class> classOptional = this.classRepo.findClassByClassId(classId);
        if (classOptional.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.CLASS_NOT_FOUND);
        }
        Class aclass = classOptional.get();
        Optional<Teacher> teacherOptional = this.teacherRepo.findTeacherByTeacherId(classCreationRequest.getTeacherId());
        if (teacherOptional.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.TEACHER_NOT_FOUND);
        }
        Teacher teacher = teacherOptional.get();
        Optional<Subject> subjectOptional = this.subjectRepo.findSubjectBySubjectId(classCreationRequest.getSubjectId());
        if (subjectOptional.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.SUBJECT_NOT_FOUND);
        }
        Subject subject = subjectOptional.get();
        Optional<Tape> tapeOptional = this.tapeRepo.findTapeByTapeId(classCreationRequest.getTapeId());
        if (tapeOptional.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.TAPE_NOT_FOUND);
        }
        Tape tape = tapeOptional.get();
        this.classMapper.classRequestToClass(classCreationRequest, aclass);

        if (!Objects.equals(aclass.getTeacher().getTeacherId(), classCreationRequest.getTeacherId())) {
            aclass.getTeacher().getClasses().remove(aclass);
            this.teacherRepo.saveAndFlush(aclass.getTeacher());

            aclass.setTeacher(teacher);
            teacher.getClasses().add(aclass);
            this.teacherRepo.saveAndFlush(teacher);
        }

        if (!Objects.equals(aclass.getSubject().getSubjectId(), classCreationRequest.getSubjectId())) {
            aclass.getSubject().getClasses().remove(aclass);
            this.subjectRepo.saveAndFlush(aclass.getSubject());

            aclass.setSubject(subject);
            subject.getClasses().add(aclass);
            this.subjectRepo.saveAndFlush(subject);
        }

        if (!Objects.equals(aclass.getTape().getTapeId(), classCreationRequest.getTapeId())) {
            aclass.getTape().getaClass().remove(aclass);
            this.tapeRepo.saveAndFlush(aclass.getTape());

            aclass.setTape(tape);
            tape.getaClass().add(aclass);
            this.tapeRepo.saveAndFlush(tape);
        }

        this.classRepo.saveAndFlush(aclass);


        logger.info(String.format("Edited class %s", aclass.getName()));
    }

    @Transactional
    public ClassResponse getClassByClassId(Long classId) throws EntityNotFoundException {
        Optional<Class> classOptional = this.classRepo.findClassByClassId(classId);
        if (classOptional.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.CLASS_NOT_FOUND);
        }
        Class aclass = classOptional.get();
        logger.info(String.format("Got class %s", aclass.getName()));

        return this.classMapper.classToClassResponse(aclass);
    }

    @Transactional
    public List<ClassResponse> getAllClasses(Integer year) {
        List<Class> classes = this.classRepo.findAllByTapeYearAndTapeReleaseYear(year, Year.now().getValue());

        return this.classMapper.classesToClassResponseList(classes);
    }

    @Transactional
    public List<ClassResponse> deleteClass(Long classId) throws EntityNotFoundException {
        Integer year = deleteClassHelper(classId);

        return getAllClasses(year);
    }

    public Integer deleteClassHelper(Long classId) throws EntityNotFoundException {
        Optional<Class> classOptional = this.classRepo.findClassByClassId(classId);
        if (classOptional.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.CLASS_NOT_FOUND);
        }
        Class aclass = classOptional.get();

        if (null != aclass.getTeacher()) {
            aclass.getTeacher().getClasses().remove(aclass);
            this.teacherRepo.save(aclass.getTeacher());
        }

        if (null != aclass.getSubject()) {
            aclass.getSubject().getClasses().remove(aclass);
            this.subjectRepo.save(aclass.getSubject());
        }

        aclass.getTape().getaClass().remove(aclass);
        this.tapeRepo.save(aclass.getTape());

        List<Choice> choices = new ArrayList<>(aclass.getChoices());

        for (Choice choice : choices) {
            choice.getClasses().remove(aclass);
            this.choiceRepo.save(choice);
        }

        this.classRepo.delete(aclass);
        logger.info(String.format("Deleted class %s", aclass.getName()));

        return aclass.getTape().getYear();
    }

    @Transactional
    public List<ClassResponse> deleteClasses(List<Long> classIds) throws EntityNotFoundException {
        Integer year = null;

        for (Long classId : classIds) {
            year = this.deleteClassHelper(classId);
        }

        return getAllClasses(year);
    }
}
