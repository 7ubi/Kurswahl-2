package com.x7ubi.kurswahl.common.repository;

import com.x7ubi.kurswahl.common.models.SubjectArea;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubjectAreaRepo extends JpaRepository<SubjectArea, Long> {
    Optional<SubjectArea> findSubjectAreaBySubjectAreaId(Long subjectAreaId);

    Optional<SubjectArea> findSubjectAreaByName(String name);

    Boolean existsSubjectAreaByName(String name);

    Boolean existsSubjectAreaBySubjectAreaId(Long subjectAreaId);
}
