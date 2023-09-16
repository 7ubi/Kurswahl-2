package com.x7ubi.kurswahl.repository;

import com.x7ubi.kurswahl.models.SubjectArea;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubjectAreaRepo extends JpaRepository<SubjectArea, Long> {
    Optional<SubjectArea> findSubjectAreaBySubjectAreaId(Long subjectAreaId);

    Boolean existsSubjectAreaByName(String name);
}
