package com.x7ubi.kurswahl.repository;

import com.x7ubi.kurswahl.models.Class;
import com.x7ubi.kurswahl.models.Subject;
import com.x7ubi.kurswahl.models.Tape;
import com.x7ubi.kurswahl.models.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClassRepo extends JpaRepository<Class, Long> {

    Optional<Class> findClassByClassId(Long classId);

    Optional<Class> findClassByName(String name);

    Optional<Class> findTopByNameAndTeacherAndTapeAndSubjectOrderByClassIdDesc(String name, Teacher teacher, Tape tape, Subject subject);

    Optional<List<Class>> findAllByTapeYearAndTapeReleaseYear(Integer year, Integer releaseYear);

    Boolean existsClassByClassId(Long classId);

    Boolean existsClassByName(String name);
}
