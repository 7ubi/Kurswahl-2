package com.x7ubi.kurswahl.admin.classes;

import com.x7ubi.kurswahl.KurswahlServiceTest;
import com.x7ubi.kurswahl.admin.classes.request.TapeCreationRequest;
import com.x7ubi.kurswahl.admin.classes.response.TapeResponse;
import com.x7ubi.kurswahl.admin.classes.response.TapeResponses;
import com.x7ubi.kurswahl.admin.classes.service.TapeCreationService;
import com.x7ubi.kurswahl.common.error.ErrorMessage;
import com.x7ubi.kurswahl.common.exception.EntityCreationException;
import com.x7ubi.kurswahl.common.exception.EntityNotFoundException;
import com.x7ubi.kurswahl.common.models.Class;
import com.x7ubi.kurswahl.common.models.Lesson;
import com.x7ubi.kurswahl.common.models.Tape;
import com.x7ubi.kurswahl.common.repository.ClassRepo;
import com.x7ubi.kurswahl.common.repository.LessonRepo;
import com.x7ubi.kurswahl.common.repository.TapeRepo;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Year;
import java.util.HashSet;

@KurswahlServiceTest
public class TapeCreationServiceTest {

    @Autowired
    private TapeCreationService tapeCreationService;

    @Autowired
    private TapeRepo tapeRepo;

    @Autowired
    private LessonRepo lessonRepo;

    @Autowired
    private ClassRepo classRepo;

    private Tape tape;

    private Tape otherTape;

    private Lesson lesson;

    private Class aClass;

    @BeforeEach
    public void setupTest() {
        tape = new Tape();
        tape.setName("GK 1");
        tape.setYear(11);
        tape.setReleaseYear(Year.now().getValue());
        tape.setLk(false);

        tapeRepo.save(tape);

        otherTape = new Tape();
        otherTape.setName("LK 1");
        otherTape.setYear(12);
        otherTape.setReleaseYear(Year.now().getValue());
        otherTape.setLk(true);

        tapeRepo.save(otherTape);
    }

    private void setupLesson() {
        lesson = new Lesson();
        lesson.setDay(0);
        lesson.setHour(0);
        lesson.setTape(tape);
        lessonRepo.save(lesson);
        tape.setLessons(new HashSet<>());
        tape.getLessons().add(lesson);
        tapeRepo.save(tape);
    }

    private void setupClass() {
        aClass = new Class();
        aClass.setName("name");
        aClass.setTape(tape);
        classRepo.save(aClass);

        tape.setaClass(new HashSet<>());
        tape.getaClass().add(aClass);
        tapeRepo.save(tape);
    }

    @Test
    public void testCreateTape() throws EntityCreationException {
        // Given
        TapeCreationRequest tapeCreationRequest = new TapeCreationRequest();
        tapeCreationRequest.setName("LK 1");
        tapeCreationRequest.setLk(true);
        tapeCreationRequest.setYear(11);

        // When
        this.tapeCreationService.createTape(tapeCreationRequest);

        // Then
        Tape createdTape = this.tapeRepo.
                findTapeByNameAndYearAndReleaseYear("LK 1", 11, Year.now().getValue()).get();
        Assertions.assertEquals(createdTape.getName(), tapeCreationRequest.getName());
        Assertions.assertEquals(createdTape.getLk(), tapeCreationRequest.getLk());
        Assertions.assertEquals(createdTape.getYear(), tapeCreationRequest.getYear());
        Assertions.assertEquals(createdTape.getReleaseYear(), Year.now().getValue());
    }

    @Test
    public void testCreateTapeNameExists() {
        // Given
        TapeCreationRequest tapeCreationRequest = new TapeCreationRequest();
        tapeCreationRequest.setName("LK 1");
        tapeCreationRequest.setLk(true);
        tapeCreationRequest.setYear(12);

        // When
        EntityCreationException entityCreationException = Assert.assertThrows(EntityCreationException.class, () ->
                this.tapeCreationService.createTape(tapeCreationRequest));

        // Then
        Assertions.assertEquals(entityCreationException.getMessage(), ErrorMessage.TAPE_ALREADY_EXISTS);
    }

    @Test
    public void testEditTape() throws EntityCreationException, EntityNotFoundException {
        // Given
        tape = this.tapeRepo.findTapeByNameAndYearAndReleaseYear("GK 1", 11, Year.now().getValue()).get();
        TapeCreationRequest tapeCreationRequest = new TapeCreationRequest();
        tapeCreationRequest.setName("LK 1");
        tapeCreationRequest.setLk(true);
        tapeCreationRequest.setYear(11);

        // When
        this.tapeCreationService.editTape(tape.getTapeId(), tapeCreationRequest);

        // Then
        Tape editedTape = this.tapeRepo.
                findTapeByNameAndYearAndReleaseYear("LK 1", 11, Year.now().getValue()).get();
        Assertions.assertEquals(editedTape.getName(), tapeCreationRequest.getName());
        Assertions.assertEquals(editedTape.getLk(), tapeCreationRequest.getLk());
        Assertions.assertEquals(editedTape.getYear(), tapeCreationRequest.getYear());
        Assertions.assertEquals(editedTape.getReleaseYear(), Year.now().getValue());

        Assertions.assertFalse(tapeRepo
                .existsTapeByNameAndYearAndReleaseYear("GK 1", 11, Year.now().getValue()));
    }

    @Test
    public void testEditTapeWrongId() {
        // Given
        tape = this.tapeRepo.findTapeByNameAndYearAndReleaseYear("GK 1", 11, Year.now().getValue()).get();
        TapeCreationRequest tapeCreationRequest = new TapeCreationRequest();
        tapeCreationRequest.setName("LK 1");
        tapeCreationRequest.setLk(true);
        tapeCreationRequest.setYear(11);

        // When
        EntityNotFoundException entityNotFoundException = Assert.assertThrows(EntityNotFoundException.class, () ->
                this.tapeCreationService.editTape(tape.getTapeId() + 3, tapeCreationRequest));

        // Then
        Assertions.assertEquals(entityNotFoundException.getMessage(), ErrorMessage.TAPE_NOT_FOUND);

        Tape editedTape = this.tapeRepo.
                findTapeByNameAndYearAndReleaseYear("GK 1", 11, Year.now().getValue()).get();
        Assertions.assertEquals(editedTape.getName(), tape.getName());
        Assertions.assertEquals(editedTape.getLk(), tape.getLk());
        Assertions.assertEquals(editedTape.getYear(), tape.getYear());
        Assertions.assertEquals(editedTape.getReleaseYear(), tape.getReleaseYear());

        Assertions.assertFalse(tapeRepo
                .existsTapeByNameAndYearAndReleaseYear("LK 1", 11, Year.now().getValue()));
    }

    @Test
    public void testEditTapeNameExists() {
        // Given
        tape = this.tapeRepo.findTapeByNameAndYearAndReleaseYear("GK 1", 11, Year.now().getValue()).get();
        TapeCreationRequest tapeCreationRequest = new TapeCreationRequest();
        tapeCreationRequest.setName("LK 1");
        tapeCreationRequest.setLk(true);
        tapeCreationRequest.setYear(12);

        // When
        EntityCreationException entityCreationException = Assert.assertThrows(EntityCreationException.class, () ->
                this.tapeCreationService.editTape(tape.getTapeId(), tapeCreationRequest));

        // Then
        Assertions.assertEquals(entityCreationException.getMessage(), ErrorMessage.TAPE_ALREADY_EXISTS);

        Tape editedTape = this.tapeRepo.
                findTapeByNameAndYearAndReleaseYear("GK 1", 11, Year.now().getValue()).get();
        Assertions.assertEquals(editedTape.getName(), tape.getName());
        Assertions.assertEquals(editedTape.getLk(), tape.getLk());
        Assertions.assertEquals(editedTape.getYear(), tape.getYear());
        Assertions.assertEquals(editedTape.getReleaseYear(), tape.getReleaseYear());

        Tape otherTapeEdited = this.tapeRepo.
                findTapeByNameAndYearAndReleaseYear("LK 1", 12, Year.now().getValue()).get();
        Assertions.assertEquals(otherTapeEdited.getName(), otherTape.getName());
        Assertions.assertEquals(otherTapeEdited.getLk(), otherTape.getLk());
        Assertions.assertEquals(otherTapeEdited.getYear(), otherTape.getYear());
        Assertions.assertEquals(otherTapeEdited.getReleaseYear(), otherTape.getReleaseYear());
    }

    @Test
    public void testGetTape() throws EntityNotFoundException {
        // Given
        setupLesson();
        tape = this.tapeRepo.findTapeByNameAndYearAndReleaseYear("GK 1", 11, Year.now().getValue()).get();

        // When
        TapeResponse response = this.tapeCreationService.getTape(tape.getTapeId());

        // Then
        Assertions.assertEquals(response.getName(), tape.getName());
        Assertions.assertEquals(response.getLk(), tape.getLk());
        Assertions.assertEquals(response.getYear(), tape.getYear());
        Assertions.assertEquals(response.getReleaseYear(), tape.getReleaseYear());

        Assertions.assertEquals(response.getLessonResponses().size(), 1);
        Assertions.assertEquals(response.getLessonResponses().get(0).getDay(), lesson.getDay());
        Assertions.assertEquals(response.getLessonResponses().get(0).getHour(), lesson.getHour());
    }

    @Test
    public void testGetTapeWrongId() {
        // Given
        tape = this.tapeRepo.findTapeByNameAndYearAndReleaseYear("GK 1", 11, Year.now().getValue()).get();

        // When
        EntityNotFoundException entityNotFoundException = Assert.assertThrows(EntityNotFoundException.class, () ->
                this.tapeCreationService.getTape(tape.getTapeId() + 3));

        // Then
        Assertions.assertEquals(entityNotFoundException.getMessage(), ErrorMessage.TAPE_NOT_FOUND);
    }

    @Test
    public void getAllTapes() {
        // Given
        setupLesson();

        // When
        TapeResponses responses = this.tapeCreationService.getAllTapes(11);

        // Then
        Assertions.assertEquals(responses.getTapeResponses().size(), 1);

        TapeResponse tape1 = responses.getTapeResponses().get(0);
        Assertions.assertEquals(tape1.getName(), tape.getName());
        Assertions.assertEquals(tape1.getLk(), tape.getLk());
        Assertions.assertEquals(tape1.getYear(), tape.getYear());
        Assertions.assertEquals(tape1.getReleaseYear(), tape.getReleaseYear());

        Assertions.assertEquals(tape1.getLessonResponses().size(), 1);
        Assertions.assertEquals(tape1.getLessonResponses().get(0).getDay(), lesson.getDay());
        Assertions.assertEquals(tape1.getLessonResponses().get(0).getHour(), lesson.getHour());
    }

    @Test
    public void deleteTape() throws EntityNotFoundException {
        // Given
        setupLesson();
        setupClass();
        tape = this.tapeRepo.findTapeByNameAndYearAndReleaseYear("GK 1", 11, Year.now().getValue()).get();
        lesson = this.lessonRepo.findLessonByTape_TapeId(tape.getTapeId()).get();

        // When
        this.tapeCreationService.deleteTape(tape.getTapeId());

        // Then
        Assertions.assertFalse(
                this.tapeRepo.existsTapeByNameAndYearAndReleaseYear("GK 1", 11, Year.now().getValue()));
        Assertions.assertFalse(lessonRepo.existsByLessonId(lesson.getLessonId()));
        Assertions.assertFalse(classRepo.existsClassByName(aClass.getName()));
    }

    @Test
    public void deleteTapeWrongId() {
        // Given
        setupLesson();
        setupClass();
        tape = this.tapeRepo.findTapeByNameAndYearAndReleaseYear("GK 1", 11, Year.now().getValue()).get();
        lesson = this.lessonRepo.findLessonByTape_TapeId(tape.getTapeId()).get();

        // When
        EntityNotFoundException entityNotFoundException = Assert.assertThrows(EntityNotFoundException.class, () ->
                this.tapeCreationService.deleteTape(tape.getTapeId() + 3));

        // Then
        Assertions.assertEquals(entityNotFoundException.getMessage(), ErrorMessage.TAPE_NOT_FOUND);

        Assertions.assertTrue(
                this.tapeRepo.existsTapeByNameAndYearAndReleaseYear("GK 1", 11, Year.now().getValue()));
        Assertions.assertTrue(lessonRepo.existsByLessonId(lesson.getLessonId()));
        Assertions.assertTrue(classRepo.existsClassByName(aClass.getName()));
    }
}
