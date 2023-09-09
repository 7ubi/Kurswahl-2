package com.x7ubi.kurswahl.repository;

import com.x7ubi.kurswahl.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepo extends JpaRepository<Student, Long> {
    Optional<Student> findStudentByUser_Username(String username);

    Boolean existsStudentByUser_Username(String username);
}
