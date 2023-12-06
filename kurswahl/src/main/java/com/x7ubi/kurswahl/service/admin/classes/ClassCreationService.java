package com.x7ubi.kurswahl.service.admin.classes;

import com.x7ubi.kurswahl.mapper.ClassMapper;
import com.x7ubi.kurswahl.models.Class;
import com.x7ubi.kurswahl.models.Subject;
import com.x7ubi.kurswahl.models.Tape;
import com.x7ubi.kurswahl.models.Teacher;
import com.x7ubi.kurswahl.repository.ClassRepo;
import com.x7ubi.kurswahl.repository.SubjectRepo;
import com.x7ubi.kurswahl.repository.TapeRepo;
import com.x7ubi.kurswahl.repository.TeacherRepo;
import com.x7ubi.kurswahl.request.admin.ClassCreationRequest;
import com.x7ubi.kurswahl.response.admin.classes.ClassResponses;
import com.x7ubi.kurswahl.response.admin.classes.ClassResultResponse;
import com.x7ubi.kurswahl.response.common.ResultResponse;
import com.x7ubi.kurswahl.service.admin.AdminErrorService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ClassCreationService {
    private final Logger logger = LoggerFactory.getLogger(ClassCreationService.class);

    private final AdminErrorService adminErrorService;

    private final ClassRepo classRepo;

    private final TapeRepo tapeRepo;

    private final TeacherRepo teacherRepo;

    private final SubjectRepo subjectRepo;

    private final ClassMapper classMapper;

    public ClassCreationService(AdminErrorService adminErrorService, ClassRepo classRepo, TapeRepo tapeRepo,
                                TeacherRepo teacherRepo, SubjectRepo subjectRepo, ClassMapper classMapper) {
        this.adminErrorService = adminErrorService;
        this.classRepo = classRepo;
        this.tapeRepo = tapeRepo;
        this.teacherRepo = teacherRepo;
        this.subjectRepo = subjectRepo;
        this.classMapper = classMapper;
    }

    @Transactional
    public ResultResponse createClass(ClassCreationRequest classCreationRequest) {
        ResultResponse resultResponse = new ResultResponse();

        resultResponse.setErrorMessages(this.adminErrorService.getTeacherNotFound(classCreationRequest.getTeacherId()));
        resultResponse.getErrorMessages()
                .addAll(this.adminErrorService.getTapeNotFound(classCreationRequest.getTapeId()));
        resultResponse.getErrorMessages()
                .addAll(this.adminErrorService.getSubjectNotFound(classCreationRequest.getSubjectId()));

        if (!resultResponse.getErrorMessages().isEmpty()) {
            return resultResponse;
        }

        Class aclass = this.classMapper.classRequestToClass(classCreationRequest);

        Teacher teacher = this.teacherRepo.findTeacherByTeacherId(classCreationRequest.getTeacherId()).get();
        aclass.setTeacher(teacher);

        Subject subject = this.subjectRepo.findSubjectBySubjectId(classCreationRequest.getSubjectId()).get();
        aclass.setSubject(subject);

        Tape tape = this.tapeRepo.findTapeByTapeId(classCreationRequest.getTapeId()).get();
        aclass.setTape(tape);

        this.classRepo.save(aclass);

        tape.getaClass().add(aclass);
        subject.getClasses().add(aclass);
        teacher.getClasses().add(aclass);
        this.teacherRepo.save(teacher);
        this.subjectRepo.save(subject);
        this.tapeRepo.save(tape);

        logger.info(String.format("Created class %s", aclass.getName()));

        return resultResponse;
    }

    @Transactional
    public ResultResponse editClass(Long classId, ClassCreationRequest classCreationRequest) {
        ResultResponse resultResponse = new ResultResponse();

        resultResponse.setErrorMessages(this.adminErrorService.getClassNotFound(classId));
        resultResponse.getErrorMessages().addAll(this.adminErrorService
                .getTeacherNotFound(classCreationRequest.getTeacherId()));
        resultResponse.getErrorMessages()
                .addAll(this.adminErrorService.getTapeNotFound(classCreationRequest.getTapeId()));
        resultResponse.getErrorMessages()
                .addAll(this.adminErrorService.getSubjectNotFound(classCreationRequest.getSubjectId()));

        if (!resultResponse.getErrorMessages().isEmpty()) {
            return resultResponse;
        }

        Class aclass = this.classRepo.findClassByClassId(classId).get();
        this.classMapper.classRequestToClass(classCreationRequest, aclass);

        if (!Objects.equals(aclass.getTeacher().getTeacherId(), classCreationRequest.getTeacherId())) {
            aclass.getTeacher().getClasses().remove(aclass);
            this.teacherRepo.save(aclass.getTeacher());

            Teacher teacher = this.teacherRepo.findTeacherByTeacherId(classCreationRequest.getTeacherId()).get();
            aclass.setTeacher(teacher);
            teacher.getClasses().add(aclass);
            this.teacherRepo.save(teacher);
        }

        if (!Objects.equals(aclass.getSubject().getSubjectId(), classCreationRequest.getSubjectId())) {
            aclass.getSubject().getClasses().remove(aclass);
            this.subjectRepo.save(aclass.getSubject());

            Subject subject = this.subjectRepo.findSubjectBySubjectId(classCreationRequest.getSubjectId()).get();
            aclass.setSubject(subject);
            subject.getClasses().add(aclass);
            this.subjectRepo.save(subject);
        }

        if (!Objects.equals(aclass.getTape().getTapeId(), classCreationRequest.getTapeId())) {
            aclass.getTape().getaClass().remove(aclass);
            this.tapeRepo.save(aclass.getTape());

            Tape tape = this.tapeRepo.findTapeByTapeId(classCreationRequest.getTapeId()).get();
            aclass.setTape(tape);
            tape.getaClass().add(aclass);
            this.tapeRepo.save(tape);
        }

        this.classRepo.save(aclass);


        logger.info(String.format("Edited class %s", aclass.getName()));

        return resultResponse;
    }

    @Transactional
    public ClassResultResponse getClassByClassId(Long classId) {

        ClassResultResponse response = new ClassResultResponse();

        response.setErrorMessages(this.adminErrorService.getClassNotFound(classId));

        if (!response.getErrorMessages().isEmpty()) {
            return response;
        }

        Class aclass = this.classRepo.findClassByClassId(classId).get();
        response.setClassResponse(this.classMapper.classToClassResponse(aclass));
        logger.info(String.format("Got class %s", aclass.getName()));

        return response;
    }

    @Transactional
    public ClassResponses getAllClasses(Integer year) {
        Optional<List<Class>> classes = this.classRepo.findAllByTapeYearAndTapeReleaseYear(year, Year.now().getValue());

        if (classes.isPresent()) {
            return this.classMapper.classesToClassResponses(classes.get());
        }

        return new ClassResponses();
    }

    @Transactional
    public ResultResponse deleteClass(Long classId) {
        ResultResponse response = new ResultResponse();

        response.setErrorMessages(this.adminErrorService.getClassNotFound(classId));


        if (!response.getErrorMessages().isEmpty()) {
            return response;
        }

        Class aclass = this.classRepo.findClassByClassId(classId).get();

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

        this.classRepo.delete(aclass);
        logger.info(String.format("Deleted class %s", aclass.getName()));

        return response;
    }
}
