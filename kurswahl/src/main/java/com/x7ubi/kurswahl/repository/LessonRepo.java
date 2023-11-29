package com.x7ubi.kurswahl.repository;

import com.x7ubi.kurswahl.models.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonRepo extends JpaRepository<Lesson, Long> {

    Boolean existsByDayAndHourAndTape_YearAndTape_ReleaseYear(Integer day, Integer hour, Integer year, Integer releaseYear);

}
