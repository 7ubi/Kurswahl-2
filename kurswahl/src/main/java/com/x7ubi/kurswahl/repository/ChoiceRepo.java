package com.x7ubi.kurswahl.repository;

import com.x7ubi.kurswahl.models.Choice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChoiceRepo extends JpaRepository<Choice, Long> {
    Optional<Choice> findChoiceByChoiceNumberAndStudent_StudentIdAndReleaseYear(Integer choiceNumber, Long student_studentId, Integer releaseYear);

}
