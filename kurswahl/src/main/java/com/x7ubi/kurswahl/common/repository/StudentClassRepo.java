package com.x7ubi.kurswahl.common.repository;

import com.x7ubi.kurswahl.common.models.StudentClass;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentClassRepo extends JpaRepository<StudentClass, Long> {
    Optional<StudentClass> findStudentClassByStudentClassId(Long studentClassId);

    Optional<StudentClass> findStudentClassByName(String name);

    Boolean existsStudentClassByNameAndReleaseYear(String name, Integer releaseYear);

    Boolean existsStudentClassByName(String name);

    Boolean existsStudentClassAreaByStudentClassId(Long studentClassId);
}
