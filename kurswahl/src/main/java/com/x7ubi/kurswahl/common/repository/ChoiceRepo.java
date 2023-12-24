package com.x7ubi.kurswahl.common.repository;

import com.x7ubi.kurswahl.common.models.Choice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChoiceRepo extends JpaRepository<Choice, Long> {
    Optional<Choice> findChoiceByChoiceNumberAndStudent_StudentIdAndReleaseYear(Integer choiceNumber, Long studentId, Integer releaseYear);

    Optional<List<Choice>> findAllByStudent_StudentIdAndReleaseYearAndChoiceNumberOrChoiceNumber(Long studentId, Integer releaseYear, Integer choiceNumber1, Integer choiceNumber2);

    Optional<Choice> findChoiceByChoiceId(Long choiceId);
}
