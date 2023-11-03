package com.x7ubi.kurswahl.repository;

import com.x7ubi.kurswahl.models.StudentClass;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentClassRepo extends JpaRepository<StudentClass, Long> {
    Optional<StudentClass> findStudentClassByStudentClassId(Long studentClassId);

    Optional<StudentClass> findStudentClassAreaByName(String name);

    Boolean existsStudentClassByNameAndReleaseYear(String name, Integer releaseYear);

    Boolean existsStudentClassAreaByName(String name);

    Boolean existsStudentClassAreaByStudentClassId(Long studentClassId);
}