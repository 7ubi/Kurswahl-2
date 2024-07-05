package com.x7ubi.kurswahl.student.choice.service;

import com.x7ubi.kurswahl.common.error.ErrorMessage;
import com.x7ubi.kurswahl.common.exception.DisabledException;
import com.x7ubi.kurswahl.common.exception.EntityNotFoundException;
import com.x7ubi.kurswahl.common.models.ChoiceClass;
import com.x7ubi.kurswahl.common.models.Student;
import com.x7ubi.kurswahl.common.repository.ChoiceClassRepo;
import com.x7ubi.kurswahl.common.repository.StudentRepo;
import com.x7ubi.kurswahl.common.settings.service.SettingsService;
import com.x7ubi.kurswahl.student.choice.mapper.ChoiceMapper;
import com.x7ubi.kurswahl.student.choice.response.ChoiceResponse;
import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
public class StudentChoiceResultService {

    private final Logger logger = LoggerFactory.getLogger(StudentChoiceResultService.class);

    private final ChoiceClassRepo choiceClassRepo;

    private final StudentRepo studentRepo;

    private final ChoiceMapper choiceMapper;

    private final SettingsService settingsService;

    public StudentChoiceResultService(ChoiceClassRepo choiceClassRepo, StudentRepo studentRepo, ChoiceMapper choiceMapper, SettingsService settingsService) {
        this.choiceClassRepo = choiceClassRepo;
        this.studentRepo = studentRepo;
        this.choiceMapper = choiceMapper;
        this.settingsService = settingsService;
    }

    public ChoiceResponse getStudentChoiceResult(String username) throws EntityNotFoundException, DisabledException {
        Student student = getStudent(username);

        if (student.getStudentClass() != null && Objects.equals(student.getStudentClass().getYear(), 11)) {
            if (!BooleanUtils.toBoolean(this.settingsService.getOrCreateSetting(SettingsService.RESULT_OPEN_11, SettingsService.RESULT_OPEN_DEFAULT_VALUE).getValue())) {
                throw new DisabledException(ErrorMessage.RESULT_DISABLED);
            }
        }
        if (student.getStudentClass() != null && Objects.equals(student.getStudentClass().getYear(), 12)) {
            if (!BooleanUtils.toBoolean(this.settingsService.getOrCreateSetting(SettingsService.RESULT_OPEN_12, SettingsService.RESULT_OPEN_DEFAULT_VALUE).getValue())) {
                throw new DisabledException(ErrorMessage.RESULT_DISABLED);
            }
        }

        Set<ChoiceClass> choiceClasses = this.choiceClassRepo
                .findAllByChoice_Student_StudentIdAndChoice_ReleaseYearAndSelected(student.getStudentId(),
                        Year.now().getValue(), true);

        ChoiceResponse choiceResponse = new ChoiceResponse();
        choiceResponse.setClassChoiceResponses(this.choiceMapper.choiceClassSetToClassChoiceResponseList(choiceClasses));

        logger.info(String.format("Got result for %s", student.getUser().getUsername()));

        return choiceResponse;
    }

    private Student getStudent(String username) throws EntityNotFoundException {
        Optional<Student> studentOptional = this.studentRepo.findStudentByUser_Username(username);
        if (studentOptional.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.STUDENT_NOT_FOUND);
        }
        return studentOptional.get();
    }
}
