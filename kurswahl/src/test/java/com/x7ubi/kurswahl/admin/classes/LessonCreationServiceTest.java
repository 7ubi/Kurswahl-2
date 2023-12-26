package com.x7ubi.kurswahl.admin.classes;

import com.x7ubi.kurswahl.KurswahlServiceTest;
import com.x7ubi.kurswahl.admin.classes.request.LessonCreationRequest;
import com.x7ubi.kurswahl.admin.classes.response.TapeResponse;
import com.x7ubi.kurswahl.admin.classes.service.LessonCreationService;
import com.x7ubi.kurswahl.common.error.ErrorMessage;
import com.x7ubi.kurswahl.common.exception.EntityCreationException;
import com.x7ubi.kurswahl.common.exception.EntityNotFoundException;
import com.x7ubi.kurswahl.common.models.Lesson;
import com.x7ubi.kurswahl.common.models.Tape;
import com.x7ubi.kurswahl.common.repository.LessonRepo;
import com.x7ubi.kurswahl.common.repository.TapeRepo;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Year;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

@KurswahlServiceTest
public class LessonCreationServiceTest {
    @Autowired
    private LessonCreationService lessonCreationService;

    @Autowired
    private LessonRepo lessonRepo;

    @Autowired
    private TapeRepo tapeRepo;

    private Lesson lesson;

    private Tape tape;

    @BeforeEach
    public void setupTest() {
        tape = new Tape();
        tape.setName("GK 1");
        tape.setYear(11);
        tape.setReleaseYear(Year.now().getValue());
        tape.setLk(false);

        tapeRepo.save(tape);

        lesson = new Lesson();
        lesson.setDay(0);
        lesson.setHour(0);
        lesson.setTape(tape);
        lessonRepo.save(lesson);
        tape.setLessons(new HashSet<>());
        tape.getLessons().add(lesson);
        tapeRepo.save(tape);
    }

    @Test
    public void createLesson() throws EntityNotFoundException, EntityCreationException {
        // Given
        tape = this.tapeRepo.findTapeByNameAndYearAndReleaseYear("GK 1", 11, Year.now().getValue()).get();
        LessonCreationRequest lessonCreationRequest = new LessonCreationRequest();
        lessonCreationRequest.setDay(1);
        lessonCreationRequest.setHour(1);
        lessonCreationRequest.setTapeId(tape.getTapeId());

        // When
        List<TapeResponse> responses = this.lessonCreationService.createLesson(lessonCreationRequest);

        // Then
        Assertions.assertEquals(responses.size(), 1);
        Assertions.assertEquals(responses.get(0).getTapeId(), tape.getTapeId());

        Lesson createdLesson = this.lessonRepo.findLessonByDayAndHour(lessonCreationRequest.getDay(),
                lessonCreationRequest.getHour()).get();
        Assertions.assertEquals(createdLesson.getTape().getTapeId(), tape.getTapeId());
        Assertions.assertEquals(createdLesson.getHour(), lessonCreationRequest.getHour());
        Assertions.assertEquals(createdLesson.getDay(), lessonCreationRequest.getDay());

        tape = this.tapeRepo.findTapeByNameAndYearAndReleaseYear("GK 1", 11, Year.now().getValue()).get();
        Assertions.assertEquals(tape.getLessons().size(), 2);
        Assertions.assertTrue(tape.getLessons().stream().anyMatch(lesson1 -> Objects.equals(lesson1.getLessonId(),
                createdLesson.getLessonId())));
    }

    @Test
    public void createLessonNotAvailable() {
        // Given
        tape = this.tapeRepo.findTapeByNameAndYearAndReleaseYear("GK 1", 11, Year.now().getValue()).get();
        LessonCreationRequest lessonCreationRequest = new LessonCreationRequest();
        lessonCreationRequest.setDay(0);
        lessonCreationRequest.setHour(0);
        lessonCreationRequest.setTapeId(tape.getTapeId());

        // When
        EntityCreationException entityCreationException = Assert.assertThrows(EntityCreationException.class, () ->
                this.lessonCreationService.createLesson(lessonCreationRequest));

        // Then
        Assertions.assertEquals(entityCreationException.getMessage(), ErrorMessage.LESSON_NOT_AVAILABLE);

        Assertions.assertTrue(this.lessonRepo.existsByDayAndHourAndTape_YearAndTape_ReleaseYear(
                lessonCreationRequest.getDay(), lessonCreationRequest.getHour(), tape.getYear(), tape.getReleaseYear()));

        tape = this.tapeRepo.findTapeByNameAndYearAndReleaseYear("GK 1", 11, Year.now().getValue()).get();
        Assertions.assertEquals(tape.getLessons().size(), 1);
    }

    @Test
    public void createLessonWrongTapeId() {
        // Given
        tape = this.tapeRepo.findTapeByNameAndYearAndReleaseYear("GK 1", 11, Year.now().getValue()).get();
        LessonCreationRequest lessonCreationRequest = new LessonCreationRequest();
        lessonCreationRequest.setDay(0);
        lessonCreationRequest.setHour(0);
        lessonCreationRequest.setTapeId(tape.getTapeId() + 3);

        // When
        EntityNotFoundException entityNotFoundException = Assert.assertThrows(EntityNotFoundException.class, () ->
                this.lessonCreationService.createLesson(lessonCreationRequest));

        // Then
        Assertions.assertEquals(entityNotFoundException.getMessage(), ErrorMessage.TAPE_NOT_FOUND);

        Assertions.assertTrue(this.lessonRepo.existsByDayAndHourAndTape_YearAndTape_ReleaseYear(
                lessonCreationRequest.getDay(), lessonCreationRequest.getHour(), tape.getYear(), tape.getReleaseYear()));

        tape = this.tapeRepo.findTapeByNameAndYearAndReleaseYear("GK 1", 11, Year.now().getValue()).get();
        Assertions.assertEquals(tape.getLessons().size(), 1);
    }

    @Test
    public void deleteLesson() throws EntityNotFoundException {
        // Given
        lesson = this.lessonRepo.findLessonByDayAndHour(lesson.getDay(), lesson.getHour()).get();

        // When
        List<TapeResponse> responses = this.lessonCreationService.deleteLesson(lesson.getLessonId());

        // Then
        Assertions.assertEquals(responses.size(), 1);
        Assertions.assertEquals(responses.get(0).getTapeId(), tape.getTapeId());
        Assertions.assertFalse(this.lessonRepo.existsByDayAndHourAndTape_YearAndTape_ReleaseYear(
                lesson.getDay(), lesson.getHour(), tape.getYear(), tape.getReleaseYear()));

        tape = this.tapeRepo.findTapeByNameAndYearAndReleaseYear("GK 1", 11, Year.now().getValue()).get();
        Assertions.assertTrue(tape.getLessons().isEmpty());
    }

    @Test
    public void deleteLessonWrongId() {
        // Given
        lesson = this.lessonRepo.findLessonByDayAndHour(lesson.getDay(), lesson.getHour()).get();

        // When
        EntityNotFoundException entityNotFoundException = Assert.assertThrows(EntityNotFoundException.class, () ->
                this.lessonCreationService.deleteLesson(lesson.getLessonId() + 3));

        // Then
        Assertions.assertEquals(entityNotFoundException.getMessage(), ErrorMessage.LESSON_NOT_FOUND);

        Assertions.assertTrue(this.lessonRepo.existsByDayAndHourAndTape_YearAndTape_ReleaseYear(
                lesson.getDay(), lesson.getHour(), tape.getYear(), tape.getReleaseYear()));

        tape = this.tapeRepo.findTapeByNameAndYearAndReleaseYear("GK 1", 11, Year.now().getValue()).get();
        Assertions.assertEquals(tape.getLessons().size(), 1);
    }
}
