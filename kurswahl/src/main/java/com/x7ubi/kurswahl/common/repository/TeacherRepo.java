package com.x7ubi.kurswahl.common.repository;

import com.x7ubi.kurswahl.common.models.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeacherRepo extends JpaRepository<Teacher, Long> {
    Optional<Teacher> findTeacherByUser_Username(String username);

    Optional<Teacher> findTeacherByTeacherId(Long teacherId);

    Boolean existsTeacherByUser_Username(String username);

    boolean existsTeacherByAbbreviationAndUser_FirstnameAndUser_Surname(String abbreviation, String firstname, String surname);
}
