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
import com.x7ubi.kurswahl.response.common.ResultResponse;
import com.x7ubi.kurswahl.service.admin.AdminErrorService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.List;

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

    public ClassResponses getAllClasses(Integer year) {
        List<Class> classes = this.classRepo.findAllByTapeYearAndTapeReleaseYear(year, Year.now().getValue()).get();
        return this.classMapper.classesToClassResponses(classes);
    }

    public ResultResponse deleteClass(Long classId) {
        ResultResponse response = new ResultResponse();

        response.setErrorMessages(this.adminErrorService.getClassNotFound(classId));


        if (!response.getErrorMessages().isEmpty()) {
            return response;
        }

        Class aclass = this.classRepo.findClassByClassId(classId).get();

        aclass.getTeacher().getClasses().remove(aclass);
        this.teacherRepo.save(aclass.getTeacher());

        aclass.getSubject().getClasses().remove(aclass);
        this.subjectRepo.save(aclass.getSubject());

        aclass.getTape().getaClass().remove(aclass);
        this.tapeRepo.save(aclass.getTape());

        this.classRepo.delete(aclass);
        logger.info(String.format("Deleted class %s", aclass.getName()));

        return response;
    }
}
