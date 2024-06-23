package com.x7ubi.kurswahl.common.repository;

import com.x7ubi.kurswahl.common.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentRepo extends JpaRepository<Student, Long> {
    Optional<Student> findStudentByUser_Username(String username);

    Optional<Student> findStudentByUser_FirstnameAndUser_SurnameAndStudentClass_ReleaseYear(String firstname, String surname, Integer releaseYear);

    Optional<Student> findStudentByStudentId(Long studentId);

    List<Student> findAllByStudentClass_ReleaseYear(Integer year);

    List<Student> findAllByStudentClass_ReleaseYearAndStudentClass_Year(Integer releaseYear, Integer year);

    List<Student> findAllByStudentIdIn(List<Long> studentIds);

    Boolean existsStudentByUser_Username(String username);
}
