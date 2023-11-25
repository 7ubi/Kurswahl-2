package com.x7ubi.kurswahl.service.admin;

import com.x7ubi.kurswahl.error.ErrorMessage;
import com.x7ubi.kurswahl.repository.*;
import com.x7ubi.kurswahl.request.admin.StudentClassCreationRequest;
import com.x7ubi.kurswahl.request.admin.SubjectAreaCreationRequest;
import com.x7ubi.kurswahl.request.admin.SubjectCreationRequest;
import com.x7ubi.kurswahl.request.admin.TapeCreationRequest;
import com.x7ubi.kurswahl.response.common.MessageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

@Service
public class AdminErrorService {
    private final Logger logger = LoggerFactory.getLogger(AdminErrorService.class);

    private final SubjectAreaRepo subjectAreaRepo;

    private final SubjectRepo subjectRepo;

    private final StudentClassRepo studentClassRepo;

    private final UserRepo userRepo;

    private final AdminRepo adminRepo;

    private final StudentRepo studentRepo;

    private final TeacherRepo teacherRepo;

    private final TapeRepo tapeRepo;

    public AdminErrorService(SubjectAreaRepo subjectAreaRepo, SubjectRepo subjectRepo,
                             StudentClassRepo studentClassRepo, UserRepo userRepo,
                             AdminRepo adminRepo, StudentRepo studentRepo, TeacherRepo teacherRepo, TapeRepo tapeRepo) {
        this.subjectAreaRepo = subjectAreaRepo;
        this.subjectRepo = subjectRepo;
        this.studentClassRepo = studentClassRepo;
        this.userRepo = userRepo;
        this.adminRepo = adminRepo;
        this.studentRepo = studentRepo;
        this.teacherRepo = teacherRepo;
        this.tapeRepo = tapeRepo;
    }

    public List<MessageResponse> findSubjectAreaCreationError(
            SubjectAreaCreationRequest subjectAreaCreationRequest) {
        List<MessageResponse> errors = new ArrayList<>();

        if (this.subjectAreaRepo.existsSubjectAreaByName(subjectAreaCreationRequest.getName())) {
            logger.error(ErrorMessage.Administration.SUBJECT_AREA_ALREADY_EXISTS);
            errors.add(new MessageResponse(ErrorMessage.Administration.SUBJECT_AREA_ALREADY_EXISTS));
        }

        return errors;
    }

    public List<MessageResponse> getSubjectAreaNotFound(Long subjectAreaId) {
        List<MessageResponse> errors = new ArrayList<>();

        if (!this.subjectAreaRepo.existsSubjectAreaBySubjectAreaId(subjectAreaId)) {
            logger.error(ErrorMessage.Administration.SUBJECT_AREA_NOT_FOUND);
            errors.add(new MessageResponse(ErrorMessage.Administration.SUBJECT_AREA_NOT_FOUND));
        }

        return errors;
    }

    public List<MessageResponse> findSubjectCreationError(SubjectCreationRequest subjectCreationRequest) {
        List<MessageResponse> errors = new ArrayList<>();

        if (this.subjectRepo.existsSubjectByName(subjectCreationRequest.getName())) {
            logger.error(ErrorMessage.Administration.SUBJECT_ALREADY_EXISTS);
            errors.add(new MessageResponse(ErrorMessage.Administration.SUBJECT_ALREADY_EXISTS));
        }

        return errors;
    }

    public List<MessageResponse> getSubjectNotFound(Long subjectId) {
        List<MessageResponse> errors = new ArrayList<>();

        if (!this.subjectRepo.existsSubjectAreaBySubjectId(subjectId)) {
            logger.error(ErrorMessage.Administration.SUBJECT_NOT_FOUND);
            errors.add(new MessageResponse(ErrorMessage.Administration.SUBJECT_NOT_FOUND));
        }

        return errors;
    }

    public List<MessageResponse> findStudentClassCreationError(
            StudentClassCreationRequest studentClassCreationRequest) {
        List<MessageResponse> errors = new ArrayList<>();

        Integer currentYear = Year.now().getValue();

        if (this.studentClassRepo.existsStudentClassByNameAndReleaseYear(
                studentClassCreationRequest.getName(), currentYear)) {
            logger.error(ErrorMessage.Administration.STUDENT_CLASS_ALREADY_EXISTS);
            errors.add(new MessageResponse(ErrorMessage.Administration.STUDENT_CLASS_ALREADY_EXISTS));
        }

        return errors;
    }

    public List<MessageResponse> getAdminNotFound(Long adminId) {
        List<MessageResponse> errors = new ArrayList<>();

        if (!adminRepo.existsAdminByAdminId(adminId)) {
            logger.error(ErrorMessage.Administration.ADMIN_NOT_FOUND);
            errors.add(new MessageResponse(ErrorMessage.Administration.ADMIN_NOT_FOUND));
        }

        return errors;
    }

    public List<MessageResponse> getStudentNotFound(Long studentId) {

        List<MessageResponse> errors = new ArrayList<>();

        if (!studentRepo.existsStudentByStudentId(studentId)) {
            logger.error(ErrorMessage.Administration.STUDENT_NOT_FOUND);
            errors.add(new MessageResponse(ErrorMessage.Administration.STUDENT_NOT_FOUND));
        }

        return errors;
    }

    public List<MessageResponse> getTeacherNotFound(Long teacherId) {
        List<MessageResponse> errors = new ArrayList<>();

        if (!teacherRepo.existsTeacherByTeacherId(teacherId)) {
            logger.error(ErrorMessage.Administration.TEACHER_NOT_FOUND);
            errors.add(new MessageResponse(ErrorMessage.Administration.TEACHER_NOT_FOUND));
        }

        return errors;
    }

    public List<MessageResponse> getStudentClassNotFound(Long studentClassId) {
        List<MessageResponse> errors = new ArrayList<>();

        if (!studentClassRepo.existsStudentClassAreaByStudentClassId(studentClassId)) {
            logger.error(ErrorMessage.Administration.STUDENT_CLASS_NOT_FOUND);
            errors.add(new MessageResponse(ErrorMessage.Administration.STUDENT_CLASS_NOT_FOUND));
        }

        return errors;
    }

    public List<MessageResponse> findTapeCreationError(TapeCreationRequest tapeCreationRequest) {
        List<MessageResponse> errors = new ArrayList<>();

        if (tapeRepo.existsTapeByNameAndYearAndReleaseYear(tapeCreationRequest.getName(),
                tapeCreationRequest.getYear(), Year.now().getValue())) {
            logger.error(ErrorMessage.Administration.TAPE_ALREADY_EXISTS);
            errors.add(new MessageResponse(ErrorMessage.Administration.TAPE_ALREADY_EXISTS));
        }

        return errors;
    }

    public List<MessageResponse> getTapeNotFound(Long tapeId) {
        List<MessageResponse> errors = new ArrayList<>();

        if (!tapeRepo.existsTapeByTapeId(tapeId)) {
            logger.error(ErrorMessage.Administration.TAPE_NOT_FOUND);
            errors.add(new MessageResponse(ErrorMessage.Administration.TAPE_NOT_FOUND));
        }

        return errors;
    }
}
