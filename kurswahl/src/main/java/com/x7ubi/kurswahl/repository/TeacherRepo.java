package com.x7ubi.kurswahl.repository;

import com.x7ubi.kurswahl.models.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeacherRepo extends JpaRepository<Teacher, Long> {
    Optional<Teacher> findTeacherByUser_Username(String username);

    Boolean existsTeacherByUser_Username(String username);
}
