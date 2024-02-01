package com.x7ubi.kurswahl.common.repository;

import com.x7ubi.kurswahl.common.models.Tape;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TapeRepo extends JpaRepository<Tape, Long> {

    Optional<Tape> findTapeByTapeId(Long tapeId);

    Optional<Tape> findTapeByNameAndYearAndReleaseYear(String name, Integer year, Integer releaseYear);

    Boolean existsTapeByNameAndYearAndReleaseYear(String name, Integer year, Integer releaseYear);

    List<Tape> findAllByYearAndReleaseYear(Integer year, Integer releaseYear);
}
