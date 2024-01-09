package com.x7ubi.kurswahl.common.repository;

import com.x7ubi.kurswahl.common.models.Class;
import com.x7ubi.kurswahl.common.models.Subject;
import com.x7ubi.kurswahl.common.models.Tape;
import com.x7ubi.kurswahl.common.models.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClassRepo extends JpaRepository<Class, Long> {

    Optional<Class> findClassByClassId(Long classId);

    Optional<Class> findClassByName(String name);

    Optional<Class> findTopByNameAndTeacherAndTapeAndSubjectOrderByClassIdDesc(String name, Teacher teacher, Tape tape, Subject subject);

    List<Class> findAllByTapeYearAndTapeReleaseYear(Integer year, Integer releaseYear);

    Boolean existsClassByClassId(Long classId);

    Boolean existsClassByName(String name);
}
