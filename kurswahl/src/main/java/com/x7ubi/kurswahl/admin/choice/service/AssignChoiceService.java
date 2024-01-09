package com.x7ubi.kurswahl.admin.choice.service;

import com.x7ubi.kurswahl.admin.choice.mapper.ClassChoiceMapper;
import com.x7ubi.kurswahl.admin.choice.response.ClassChoiceResponse;
import com.x7ubi.kurswahl.common.models.Class;
import com.x7ubi.kurswahl.common.repository.ClassRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.List;

@Service
public class AssignChoiceService {

    private final Logger logger = LoggerFactory.getLogger(AssignChoiceService.class);

    private final ClassRepo classRepo;

    private final ClassChoiceMapper classChoiceMapper;

    public AssignChoiceService(ClassRepo classRepo, ClassChoiceMapper classChoiceMapper) {
        this.classRepo = classRepo;
        this.classChoiceMapper = classChoiceMapper;
    }

    public List<ClassChoiceResponse> getClassesWithChoices(Integer year) {
        List<Class> classes = this.classRepo.findAllByTapeYearAndTapeReleaseYear(year, Year.now().getValue());

        return this.classChoiceMapper.classesToClassChoiceResponses(classes);
    }
}
