package com.x7ubi.kurswahl.repository;

import com.x7ubi.kurswahl.models.Tape;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TapeRepo extends JpaRepository<Tape, Long> {

    Optional<Tape> findTapeByTapeId(Long tapeId);

    Optional<Tape> findTapeByName(String name);

    Boolean existsTapeByTapeId(Long tapeId);

    Boolean existsTapeByName(String name);
}
