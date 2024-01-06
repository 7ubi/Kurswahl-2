package com.x7ubi.kurswahl.admin.choice.service;

import com.x7ubi.kurswahl.admin.choice.mapper.StudentSurveillanceMapper;
import com.x7ubi.kurswahl.admin.choice.response.ChoiceSurveillanceResponse;
import com.x7ubi.kurswahl.common.models.*;
import com.x7ubi.kurswahl.common.repository.RuleSetRepo;
import com.x7ubi.kurswahl.common.repository.StudentRepo;
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
public class ChoiceSurveillanceService {

    private final Logger logger = LoggerFactory.getLogger(ChoiceSurveillanceService.class);

    private final RuleSetRepo ruleSetRepo;

    private final StudentRepo studentRepo;

    private final StudentSurveillanceMapper studentSurveillanceMapper;

    public ChoiceSurveillanceService(RuleSetRepo ruleSetRepo, StudentRepo studentRepo, StudentSurveillanceMapper studentSurveillanceMapper) {
        this.ruleSetRepo = ruleSetRepo;
        this.studentRepo = studentRepo;
        this.studentSurveillanceMapper = studentSurveillanceMapper;
    }

    @Transactional
    public List<ChoiceSurveillanceResponse> getChoiceSurveillanceForStudents() {
        List<ChoiceSurveillanceResponse> responses = new ArrayList<>();
        List<Student> students = this.studentRepo.findAllByStudentClass_ReleaseYear(Year.now().getValue());
        List<RuleSet> ruleSets = this.ruleSetRepo.findAll();

        for (Student student : students) {

            ChoiceSurveillanceResponse response = new ChoiceSurveillanceResponse();
            response.setStudentSurveillanceResponse(
                    this.studentSurveillanceMapper.studentTostudentSurveillanceResponse(student));

            Optional<Choice> firstChoiceOptional =
                    student.getChoices().stream().filter(choice -> choice.getChoiceNumber() == 1 &&
                            choice.getReleaseYear() == Year.now().getValue()).findFirst();

            if (firstChoiceOptional.isEmpty()) {
                responses.add(response);
                continue;
            }

            Optional<Choice> secondChoiceOptional =
                    student.getChoices().stream().filter(choice -> choice.getChoiceNumber() == 2 &&
                            choice.getReleaseYear() == Year.now().getValue()).findFirst();

            if (secondChoiceOptional.isEmpty()) {
                responses.add(response);
                continue;
            }
            response.setChosen(true);

            Optional<RuleSet> ruleSetOptional = ruleSets.stream().filter(ruleSet -> Objects.equals(ruleSet.getYear(),
                    student.getStudentClass().getYear())).findFirst();
            if (ruleSetOptional.isEmpty()) {
                response.setFulfilledRules(true);
                responses.add(response);
                continue;
            }
            RuleSet ruleSet = ruleSetOptional.get();
            response.setFulfilledRules(getRulesFulfilled(ruleSet, firstChoiceOptional.get()) &&
                    getRulesFulfilled(ruleSet, secondChoiceOptional.get()));

            responses.add(response);
        }

        logger.info("Analyzed all Student Choices");

        return responses;
    }

    private Boolean getRulesFulfilled(RuleSet ruleSet, Choice choice) {
        for (Rule rule : ruleSet.getRules()) {
            boolean fulfilled = false;
            for (Subject subject : rule.getSubjects()) {
                if (choice.getClasses().stream().anyMatch(c -> Objects.equals(c.getSubject().getSubjectId(),
                        subject.getSubjectId()))) {
                    fulfilled = true;
                    break;
                }
            }

            if (!fulfilled) {
                return false;
            }
        }
        return true;
    }
}
