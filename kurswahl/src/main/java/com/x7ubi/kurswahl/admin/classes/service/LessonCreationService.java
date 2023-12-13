package com.x7ubi.kurswahl.admin.classes.service;

import com.x7ubi.kurswahl.admin.classes.mapper.LessonMapper;
import com.x7ubi.kurswahl.admin.classes.request.LessonCreationRequest;
import com.x7ubi.kurswahl.common.error.ErrorMessage;
import com.x7ubi.kurswahl.common.exception.EntityCreationException;
import com.x7ubi.kurswahl.common.exception.EntityNotFoundException;
import com.x7ubi.kurswahl.common.models.Lesson;
import com.x7ubi.kurswahl.common.models.Tape;
import com.x7ubi.kurswahl.common.repository.LessonRepo;
import com.x7ubi.kurswahl.common.repository.TapeRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LessonCreationService {

    private final Logger logger = LoggerFactory.getLogger(LessonCreationService.class);

    private final LessonRepo lessonRepo;

    private final TapeRepo tapeRepo;

    private final LessonMapper lessonMapper;

    public LessonCreationService(LessonRepo lessonRepo, TapeRepo tapeRepo, LessonMapper lessonMapper) {
        this.lessonRepo = lessonRepo;
        this.tapeRepo = tapeRepo;
        this.lessonMapper = lessonMapper;
    }

    public void createLesson(LessonCreationRequest lessonCreationRequest) throws EntityNotFoundException, EntityCreationException {
        Optional<Tape> tapeOptional = tapeRepo.findTapeByTapeId(lessonCreationRequest.getTapeId());

        if (tapeOptional.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.TAPE_NOT_FOUND);
        }

        Tape tape = tapeOptional.get();

        isLessonAvailable(lessonCreationRequest, tape);

        Lesson lesson = lessonMapper.lessonRequestToLesson(lessonCreationRequest);
        lesson.setTape(tape);
        lessonRepo.save(lesson);

        tape.getLessons().add(lesson);
        tapeRepo.save(tape);

        logger.info(String.format("Saved Lesson to Tape %s", tape.getName()));
    }

    public void deleteLesson(Long lessonId) throws EntityNotFoundException {
        Optional<Lesson> lessonOptional = this.lessonRepo.findLessonByLessonId(lessonId);

        if (lessonOptional.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.LESSON_NOT_FOUND);
        }

        Lesson lesson = lessonOptional.get();
        lesson.getTape().getLessons().remove(lesson);
        tapeRepo.save(lesson.getTape());
        lessonRepo.delete(lesson);

        logger.info(String.format("Deleted Lesson from Tape %s", lesson.getTape().getName()));
    }

    private void isLessonAvailable(LessonCreationRequest lessonCreationRequest, Tape tape) throws EntityCreationException {
        if (lessonRepo.existsByDayAndHourAndTape_YearAndTape_ReleaseYear(lessonCreationRequest.getDay(),
                lessonCreationRequest.getHour(), tape.getYear(), tape.getReleaseYear())) {
            throw new EntityCreationException(ErrorMessage.LESSON_NOT_AVAILABLE);
        }
    }
}
