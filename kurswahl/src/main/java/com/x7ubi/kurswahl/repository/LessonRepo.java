package com.x7ubi.kurswahl.repository;

import com.x7ubi.kurswahl.models.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LessonRepo extends JpaRepository<Lesson, Long> {

    Optional<Lesson> findLessonByLessonId(Long lessonId);

    Optional<Lesson> findLessonByTape_TapeId(Long tapeId);

    Optional<Lesson> findLessonByDayAndHour(Integer day, Integer hour);

    Boolean existsByDayAndHourAndTape_YearAndTape_ReleaseYear(Integer day, Integer hour, Integer year, Integer releaseYear);

    Boolean existsByLessonId(Long lessonId);
}
