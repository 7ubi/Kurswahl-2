package com.x7ubi.kurswahl.common.repository;

import com.x7ubi.kurswahl.common.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentRepo extends JpaRepository<Student, Long> {
    Optional<Student> findStudentByUser_Username(String username);

    Optional<Student> findStudentByStudentId(Long studentId);

    List<Student> findAllByStudentClass_ReleaseYear(Integer year);

    Boolean existsStudentByUser_Username(String username);

    Boolean existsStudentByStudentId(Long studentId);
}
