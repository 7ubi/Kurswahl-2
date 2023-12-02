package com.x7ubi.kurswahl.admin.classes;

import com.x7ubi.kurswahl.KurswahlServiceTest;
import com.x7ubi.kurswahl.error.ErrorMessage;
import com.x7ubi.kurswahl.models.Class;
import com.x7ubi.kurswahl.models.Lesson;
import com.x7ubi.kurswahl.models.Tape;
import com.x7ubi.kurswahl.repository.ClassRepo;
import com.x7ubi.kurswahl.repository.LessonRepo;
import com.x7ubi.kurswahl.repository.TapeRepo;
import com.x7ubi.kurswahl.request.admin.TapeCreationRequest;
import com.x7ubi.kurswahl.response.admin.classes.TapeResponse;
import com.x7ubi.kurswahl.response.admin.classes.TapeResponses;
import com.x7ubi.kurswahl.response.admin.classes.TapeResultResponse;
import com.x7ubi.kurswahl.response.common.ResultResponse;
import com.x7ubi.kurswahl.service.admin.classes.TapeCreationService;
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
    public void testCreateTape() {
        // Given
        TapeCreationRequest tapeCreationRequest = new TapeCreationRequest();
        tapeCreationRequest.setName("LK 1");
        tapeCreationRequest.setLk(true);
        tapeCreationRequest.setYear(11);

        // When
        ResultResponse response = this.tapeCreationService.createTape(tapeCreationRequest);

        // Then
        Assertions.assertTrue(response.getErrorMessages().isEmpty());

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
        ResultResponse response = this.tapeCreationService.createTape(tapeCreationRequest);

        // Then
        Assertions.assertEquals(response.getErrorMessages().size(), 1);
        Assertions.assertEquals(response.getErrorMessages().get(0).getMessage(),
                ErrorMessage.Administration.TAPE_ALREADY_EXISTS);
    }

    @Test
    public void testEditTape() {
        // Given
        tape = this.tapeRepo.findTapeByNameAndYearAndReleaseYear("GK 1", 11, Year.now().getValue()).get();
        TapeCreationRequest tapeCreationRequest = new TapeCreationRequest();
        tapeCreationRequest.setName("LK 1");
        tapeCreationRequest.setLk(true);
        tapeCreationRequest.setYear(11);

        // When
        ResultResponse response = this.tapeCreationService.editTape(tape.getTapeId(), tapeCreationRequest);

        // Then
        Assertions.assertTrue(response.getErrorMessages().isEmpty());

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
        ResultResponse response = this.tapeCreationService.editTape(tape.getTapeId() + 3, tapeCreationRequest);

        // Then
        Assertions.assertEquals(response.getErrorMessages().size(), 1);
        Assertions.assertEquals(response.getErrorMessages().get(0).getMessage(),
                ErrorMessage.Administration.TAPE_NOT_FOUND);

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
        ResultResponse response = this.tapeCreationService.editTape(tape.getTapeId(), tapeCreationRequest);

        // Then
        Assertions.assertEquals(response.getErrorMessages().size(), 1);
        Assertions.assertEquals(response.getErrorMessages().get(0).getMessage(),
                ErrorMessage.Administration.TAPE_ALREADY_EXISTS);

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
    public void testGetTape() {
        // Given
        setupLesson();
        tape = this.tapeRepo.findTapeByNameAndYearAndReleaseYear("GK 1", 11, Year.now().getValue()).get();

        // When
        TapeResultResponse response = this.tapeCreationService.getTape(tape.getTapeId());

        // Then
        Assertions.assertTrue(response.getErrorMessages().isEmpty());

        Assertions.assertEquals(response.getTapeResponse().getName(), tape.getName());
        Assertions.assertEquals(response.getTapeResponse().getLk(), tape.getLk());
        Assertions.assertEquals(response.getTapeResponse().getYear(), tape.getYear());
        Assertions.assertEquals(response.getTapeResponse().getReleaseYear(), tape.getReleaseYear());

        Assertions.assertEquals(response.getTapeResponse().getLessonResponses().size(), 1);
        Assertions.assertEquals(response.getTapeResponse().getLessonResponses().get(0).getDay(), lesson.getDay());
        Assertions.assertEquals(response.getTapeResponse().getLessonResponses().get(0).getHour(), lesson.getHour());
    }

    @Test
    public void testGetTapeWrongId() {
        // Given
        tape = this.tapeRepo.findTapeByNameAndYearAndReleaseYear("GK 1", 11, Year.now().getValue()).get();

        // When
        TapeResultResponse response = this.tapeCreationService.getTape(tape.getTapeId() + 3);

        // Then
        Assertions.assertEquals(response.getErrorMessages().size(), 1);
        Assertions.assertEquals(response.getErrorMessages().get(0).getMessage(),
                ErrorMessage.Administration.TAPE_NOT_FOUND);

        Assertions.assertNull(response.getTapeResponse());
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
    public void deleteTape() {
        // Given
        setupLesson();
        setupClass();
        tape = this.tapeRepo.findTapeByNameAndYearAndReleaseYear("GK 1", 11, Year.now().getValue()).get();
        lesson = this.lessonRepo.findLessonByTape_TapeId(tape.getTapeId()).get();

        // When
        ResultResponse response = this.tapeCreationService.deleteTape(tape.getTapeId());

        // Then
        Assertions.assertTrue(response.getErrorMessages().isEmpty());

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
        ResultResponse response = this.tapeCreationService.deleteTape(tape.getTapeId() + 3);

        // Then
        Assertions.assertEquals(response.getErrorMessages().size(), 1);
        Assertions.assertEquals(response.getErrorMessages().get(0).getMessage(),
                ErrorMessage.Administration.TAPE_NOT_FOUND);

        Assertions.assertTrue(
                this.tapeRepo.existsTapeByNameAndYearAndReleaseYear("GK 1", 11, Year.now().getValue()));
        Assertions.assertTrue(lessonRepo.existsByLessonId(lesson.getLessonId()));
        Assertions.assertTrue(classRepo.existsClassByName(aClass.getName()));
    }
}
