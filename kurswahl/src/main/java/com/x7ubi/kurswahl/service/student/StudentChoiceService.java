package com.x7ubi.kurswahl.service.student;

import com.x7ubi.kurswahl.error.ErrorMessage;
import com.x7ubi.kurswahl.mapper.ChoiceMapper;
import com.x7ubi.kurswahl.models.Choice;
import com.x7ubi.kurswahl.models.Class;
import com.x7ubi.kurswahl.models.Student;
import com.x7ubi.kurswahl.repository.ChoiceRepo;
import com.x7ubi.kurswahl.repository.ClassRepo;
import com.x7ubi.kurswahl.repository.StudentRepo;
import com.x7ubi.kurswahl.repository.TapeRepo;
import com.x7ubi.kurswahl.request.student.AlterStudentChoiceRequest;
import com.x7ubi.kurswahl.response.common.MessageResponse;
import com.x7ubi.kurswahl.response.common.ResultResponse;
import com.x7ubi.kurswahl.service.admin.AdminErrorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class StudentChoiceService {

    Logger logger = LoggerFactory.getLogger(StudentChoiceService.class);

    private final AdminErrorService adminErrorService;

    private final ChoiceRepo choiceRepo;

    private final ClassRepo classRepo;

    private final TapeRepo tapeRepo;

    private final StudentRepo studentRepo;

    private final ChoiceMapper choiceMapper;

    public StudentChoiceService(AdminErrorService adminErrorService, ChoiceRepo choiceRepo, ClassRepo classRepo,
                                TapeRepo tapeRepo, StudentRepo studentRepo, ChoiceMapper choiceMapper) {
        this.adminErrorService = adminErrorService;
        this.choiceRepo = choiceRepo;
        this.classRepo = classRepo;
        this.tapeRepo = tapeRepo;
        this.studentRepo = studentRepo;
        this.choiceMapper = choiceMapper;
    }


    public ResultResponse alterChoice(String username, AlterStudentChoiceRequest alterStudentChoiceRequest) {
        ResultResponse resultResponse = new ResultResponse();

        resultResponse.setErrorMessages(this.adminErrorService.getClassNotFound(alterStudentChoiceRequest.getClassId()));

        if (!resultResponse.getErrorMessages().isEmpty()) {
            return resultResponse;
        }

        Optional<Student> studentOptional = this.studentRepo.findStudentByUser_Username(username);
        Student student;

        if (studentOptional.isPresent()) {
            student = studentOptional.get();
        } else {
            resultResponse.setErrorMessages(new ArrayList<>(List.of(
                    new MessageResponse(ErrorMessage.Administration.STUDENT_NOT_FOUND))));
            return resultResponse;
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
        }

        Class aClass = this.classRepo.findClassByClassId(alterStudentChoiceRequest.getClassId()).get();

        choice.getClasses().add(aClass);
        this.choiceRepo.save(choice);
        aClass.getChoices().add(choice);
        this.classRepo.save(aClass);

        return resultResponse;
    }
}
