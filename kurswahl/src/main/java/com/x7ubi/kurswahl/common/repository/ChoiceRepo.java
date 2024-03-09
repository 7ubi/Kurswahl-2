package com.x7ubi.kurswahl.common.repository;

import com.x7ubi.kurswahl.common.models.Choice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChoiceRepo extends JpaRepository<Choice, Long> {
    Optional<Choice> findChoiceByChoiceNumberAndStudent_StudentIdAndReleaseYear(Integer choiceNumber, Long studentId, Integer releaseYear);

    Optional<Choice> findChoiceByChoiceId(Long choiceId);

    List<Choice> findAllByStudent_StudentIdAndReleaseYear(Long studentId, Integer releaseYear);
}
