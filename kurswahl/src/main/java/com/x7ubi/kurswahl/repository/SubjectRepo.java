package com.x7ubi.kurswahl.repository;

import com.x7ubi.kurswahl.models.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubjectRepo extends JpaRepository<Subject, Long> {
    Optional<Subject> findSubjectBySubjectId(Long subjectId);

    Optional<Subject> findSubjectByName(String name);

    Boolean existsSubjectByName(String name);

    Boolean existsSubjectAreaBySubjectId(Long subjectId);
}
