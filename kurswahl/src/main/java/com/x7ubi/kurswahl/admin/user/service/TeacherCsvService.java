package com.x7ubi.kurswahl.admin.user.service;

import com.x7ubi.kurswahl.admin.user.request.StudentSignupRequest;
import com.x7ubi.kurswahl.admin.user.response.TeacherResponse;
import com.x7ubi.kurswahl.common.auth.utils.PasswordGenerator;
import com.x7ubi.kurswahl.common.models.Teacher;
import com.x7ubi.kurswahl.common.models.User;
import com.x7ubi.kurswahl.common.repository.TeacherRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherCsvService {

    private final Logger logger = LoggerFactory.getLogger(TeacherCsvService.class);

    private final TeacherRepo teacherRepo;

    private final TeacherCreationService teacherCreationService;

    private final PasswordEncoder passwordEncoder;

    private final UsernameService usernameService;

    public TeacherCsvService(TeacherRepo teacherRepo, TeacherCreationService teacherCreationService,
                             PasswordEncoder passwordEncoder, UsernameService usernameService) {
        this.teacherRepo = teacherRepo;
        this.teacherCreationService = teacherCreationService;
        this.passwordEncoder = passwordEncoder;
        this.usernameService = usernameService;
    }

    public List<TeacherResponse> importCsv(String csv) {
        List<String> csvLine = csv.lines().toList();

        for (String line : csvLine) {
            String[] values = line.split(";");

            try {
                String abbreviation = values[0];
                String firstname = values[1];
                String surname = values[2];

                if (this.teacherRepo.existsTeacherByAbbreviationAndUser_FirstnameAndUser_Surname(values[0], values[1], values[2])) {
                    logger.info(String.format("Teacher with name %s %s (%s) already exists", firstname, surname, abbreviation));
                    continue;
                }

                Teacher teacher = new Teacher();
                teacher.setUser(new User());
                teacher.getUser().setFirstname(firstname);
                teacher.getUser().setSurname(surname);
                teacher.setAbbreviation(abbreviation);
                teacher.getUser().setUsername(this.usernameService.generateUsernameFromName(new StudentSignupRequest(firstname, surname)));
                teacher.getUser().setGeneratedPassword(PasswordGenerator.generatePassword());
                teacher.getUser().setPassword(passwordEncoder.encode(teacher.getUser().getGeneratedPassword()));

                this.teacherRepo.save(teacher);
            } catch (IndexOutOfBoundsException e) {
                logger.error(String.format("Import failed for line: %s", line));
            }
        }
        return this.teacherCreationService.getAllTeachers();
    }
}
