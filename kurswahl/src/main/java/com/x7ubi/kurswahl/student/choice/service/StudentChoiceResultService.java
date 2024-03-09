package com.x7ubi.kurswahl.student.choice.service;

import com.x7ubi.kurswahl.common.error.ErrorMessage;
import com.x7ubi.kurswahl.common.exception.EntityNotFoundException;
import com.x7ubi.kurswahl.common.models.ChoiceClass;
import com.x7ubi.kurswahl.common.models.Student;
import com.x7ubi.kurswahl.common.repository.ChoiceClassRepo;
import com.x7ubi.kurswahl.common.repository.StudentRepo;
import com.x7ubi.kurswahl.student.choice.mapper.ChoiceMapper;
import com.x7ubi.kurswahl.student.choice.response.ChoiceResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.Optional;
import java.util.Set;

@Service
public class StudentChoiceResultService {

    private final Logger logger = LoggerFactory.getLogger(StudentChoiceResultService.class);

    private final ChoiceClassRepo choiceClassRepo;

    private final StudentRepo studentRepo;

    private final ChoiceMapper choiceMapper;

    public StudentChoiceResultService(ChoiceClassRepo choiceClassRepo, StudentRepo studentRepo, ChoiceMapper choiceMapper) {
        this.choiceClassRepo = choiceClassRepo;
        this.studentRepo = studentRepo;
        this.choiceMapper = choiceMapper;
    }

    public ChoiceResponse getStudentChoiceResult(String username) throws EntityNotFoundException {
        Student student = getStudent(username);

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
