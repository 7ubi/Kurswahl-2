package com.x7ubi.kurswahl.admin.choice.service;

import com.x7ubi.kurswahl.admin.choice.mapper.ClassStudentsMapper;
import com.x7ubi.kurswahl.admin.choice.mapper.StudentSurveillanceMapper;
import com.x7ubi.kurswahl.admin.choice.response.ChoiceResultResponse;
import com.x7ubi.kurswahl.common.models.ChoiceClass;
import com.x7ubi.kurswahl.common.models.Class;
import com.x7ubi.kurswahl.common.models.RuleSet;
import com.x7ubi.kurswahl.common.models.Student;
import com.x7ubi.kurswahl.common.repository.ClassRepo;
import com.x7ubi.kurswahl.common.repository.RuleSetRepo;
import com.x7ubi.kurswahl.common.repository.StudentRepo;
import com.x7ubi.kurswahl.common.rule.service.RuleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ChoiceResultService {

    private final Logger logger = LoggerFactory.getLogger(ChoiceResultService.class);

    private final ClassRepo classRepo;

    private final StudentRepo studentRepo;

    private final RuleSetRepo ruleSetRepo;

    private final ClassStudentsMapper classStudentsMapper;

    private final StudentSurveillanceMapper studentSurveillanceMapper;

    private final RuleService ruleService;

    public ChoiceResultService(ClassRepo classRepo, StudentRepo studentRepo, RuleSetRepo ruleSetRepo,
                               ClassStudentsMapper classStudentsMapper,
                               StudentSurveillanceMapper studentSurveillanceMapper, RuleService ruleService) {
        this.classRepo = classRepo;
        this.studentRepo = studentRepo;
        this.ruleSetRepo = ruleSetRepo;
        this.classStudentsMapper = classStudentsMapper;
        this.studentSurveillanceMapper = studentSurveillanceMapper;
        this.ruleService = ruleService;
    }

    @Transactional(readOnly = true)
    public ChoiceResultResponse getResults(Integer year) {
        ChoiceResultResponse choiceResultResponse = new ChoiceResultResponse();

        getClasses(year, choiceResultResponse);

        List<Student> students = this.studentRepo.findAllByStudentClass_ReleaseYearAndStudentClass_Year(Year.now().getValue(), year);

        Optional<RuleSet> ruleSetOptional = this.ruleSetRepo.findRuleSetByYear(year);
        if (ruleSetOptional.isPresent()) {
            RuleSet ruleSet = ruleSetOptional.get();

            students.forEach(student -> {
                Set<ChoiceClass> choiceClasses = new HashSet<>();

                student.getChoices().forEach(choice -> {
                    if (choice.getReleaseYear() == Year.now().getValue()) {
                        choiceClasses.addAll(choice.getChoiceClasses().stream().filter(ChoiceClass::isSelected).toList());
                    }
                });

                if (!this.ruleService.getRulesFulfilled(ruleSet, choiceClasses)) {
                    choiceResultResponse.getStudentsNotFulfilledRules().add(this.studentSurveillanceMapper
                            .studentTostudentSurveillanceResponse(student));
                }
            });
        }
        logger.info("Generated results");

        return choiceResultResponse;
    }

    private void getClasses(Integer year, ChoiceResultResponse choiceResultResponse) {
        List<Class> classes = this.classRepo.findAllByTapeYearAndTapeReleaseYear(year, Year.now().getValue());
        classes.forEach(c -> c.setChoiceClasses(c.getChoiceClasses().stream().filter(ChoiceClass::isSelected).collect(Collectors.toSet())));

        logger.info(String.format("Filtered Students, who chose classes in year %s", year));

        choiceResultResponse.setClassStudentsResponses(this.classStudentsMapper.classesToClassChoiceResponses(classes));
    }
}
