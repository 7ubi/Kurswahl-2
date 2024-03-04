package com.x7ubi.kurswahl.admin.user.service;

import com.x7ubi.kurswahl.admin.user.request.StudentCsvRequest;
import com.x7ubi.kurswahl.admin.user.response.StudentResponse;
import com.x7ubi.kurswahl.common.models.Student;
import com.x7ubi.kurswahl.common.models.StudentClass;
import com.x7ubi.kurswahl.common.models.User;
import com.x7ubi.kurswahl.common.repository.StudentClassRepo;
import com.x7ubi.kurswahl.common.repository.StudentRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class StudentCsvService {

    private final Logger logger = LoggerFactory.getLogger(StudentCsvService.class);

    private final StudentCreationService studentCreationService;

    private final StudentClassRepo studentClassRepo;

    private final StudentRepo studentRepo;

    public StudentCsvService(StudentCreationService studentCreationService, StudentClassRepo studentClassRepo, StudentRepo studentRepo) {
        this.studentCreationService = studentCreationService;
        this.studentClassRepo = studentClassRepo;
        this.studentRepo = studentRepo;
    }


    @Transactional
    public List<StudentResponse> importCsv(StudentCsvRequest studentCsvRequest) {
        List<String> csvLine = studentCsvRequest.getCsv().lines().toList();
        Set<StudentClass> studentClassesCache = new HashSet<>();

        for (String line : csvLine) {
            String[] values = line.split(";");

            try {
                Student student = new Student();
                student.setUser(new User());

                Optional<StudentClass> studentClassOptional = studentClassesCache.stream().filter(stClass ->
                        stClass.getName().equals(values[0])).findFirst();
                StudentClass studentClass;
                if (studentClassOptional.isPresent()) {
                    studentClass = studentClassOptional.get();
                } else {
                    studentClass = getOrCreateStudentClass(values[0], studentCsvRequest.getYear());
                    studentClassesCache.add(studentClass);
                }

                studentClass.getStudents().add(student);
                this.studentClassRepo.save(studentClass);

                student.setStudentClass(studentClass);

                this.studentRepo.save(student);

            } catch (IndexOutOfBoundsException e) {
                logger.error(String.format("Import failed for line: %s", line));
            }
        }

        return this.studentCreationService.getAllStudents();
    }

    private StudentClass getOrCreateStudentClass(String name, Integer year) {
        Optional<StudentClass> studentClassOptional =
                this.studentClassRepo.findStudentClassByNameAndYearAndReleaseYear(name, year, Year.now().getValue());

        if (studentClassOptional.isPresent()) {
            return studentClassOptional.get();
        } else {
            StudentClass studentClass = new StudentClass();
            studentClass.setName(name);
            studentClass.setReleaseYear(Year.now().getValue());
            studentClass.setYear(year);
            this.studentClassRepo.save(studentClass);
            return this.studentClassRepo.findStudentClassByNameAndYearAndReleaseYear(name, year, Year.now().getValue()).get();
        }
    }
}
