package com.x7ubi.kurswahl.service.admin.classes;

import com.x7ubi.kurswahl.error.ErrorMessage;
import com.x7ubi.kurswahl.mapper.LessonMapper;
import com.x7ubi.kurswahl.models.Lesson;
import com.x7ubi.kurswahl.models.Tape;
import com.x7ubi.kurswahl.repository.LessonRepo;
import com.x7ubi.kurswahl.repository.TapeRepo;
import com.x7ubi.kurswahl.request.admin.LessonCreationRequest;
import com.x7ubi.kurswahl.response.common.MessageResponse;
import com.x7ubi.kurswahl.response.common.ResultResponse;
import com.x7ubi.kurswahl.service.admin.AdminErrorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LessonCreationService {

    private final Logger logger = LoggerFactory.getLogger(LessonCreationService.class);

    private final LessonRepo lessonRepo;

    private final TapeRepo tapeRepo;

    private final LessonMapper lessonMapper;

    private final AdminErrorService adminErrorService;

    public LessonCreationService(LessonRepo lessonRepo, TapeRepo tapeRepo, LessonMapper lessonMapper,
                                 AdminErrorService adminErrorService) {
        this.lessonRepo = lessonRepo;
        this.tapeRepo = tapeRepo;
        this.lessonMapper = lessonMapper;
        this.adminErrorService = adminErrorService;
    }

    public ResultResponse createLesson(LessonCreationRequest lessonCreationRequest) {
        ResultResponse resultResponse = new ResultResponse();

        resultResponse.setErrorMessages(this.adminErrorService.getTapeNotFound(lessonCreationRequest.getTapeId()));
        resultResponse.getErrorMessages().addAll(isLessonAvailable(lessonCreationRequest));

        if (!resultResponse.getErrorMessages().isEmpty()) {
            return resultResponse;
        }

        Tape tape = tapeRepo.findTapeByTapeId(lessonCreationRequest.getTapeId()).get();
        Lesson lesson = lessonMapper.lessonRequestToLesson(lessonCreationRequest);
        lesson.setTape(tape);
        lessonRepo.save(lesson);

        tape.getLessons().add(lesson);
        tapeRepo.save(tape);

        return resultResponse;
    }

    private List<MessageResponse> isLessonAvailable(LessonCreationRequest lessonCreationRequest) {
        List<MessageResponse> errors = new ArrayList<>();
        Optional<Tape> tapeOptional = tapeRepo.findTapeByTapeId(lessonCreationRequest.getTapeId());
        if (tapeOptional.isPresent()) {

            Tape tape = tapeOptional.get();

            if (lessonRepo.existsByDayAndHourAndTape_YearAndTape_ReleaseYear(lessonCreationRequest.getDay(),
                    lessonCreationRequest.getHour(), tape.getYear(), tape.getReleaseYear())) {
                logger.error(ErrorMessage.Administration.LESSON_NOT_AVAILABLE);
                errors.add(new MessageResponse(ErrorMessage.Administration.LESSON_NOT_AVAILABLE));
            }
        }

        return errors;
    }
}
