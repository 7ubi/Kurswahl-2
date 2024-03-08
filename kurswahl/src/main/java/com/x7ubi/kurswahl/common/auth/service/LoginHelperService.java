package com.x7ubi.kurswahl.common.auth.service;

import com.x7ubi.kurswahl.common.auth.response.Role;
import com.x7ubi.kurswahl.common.models.User;
import com.x7ubi.kurswahl.common.repository.AdminRepo;
import com.x7ubi.kurswahl.common.repository.StudentRepo;
import com.x7ubi.kurswahl.common.repository.TeacherRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginHelperService {

    private final Logger logger = LoggerFactory.getLogger(LoginHelperService.class);

    private final AdminRepo adminRepo;

    private final TeacherRepo teacherRepo;

    private final StudentRepo studentRepo;

    private final PasswordEncoder passwordEncoder;

    public LoginHelperService(AdminRepo adminRepo, TeacherRepo teacherRepo, StudentRepo studentRepo, PasswordEncoder passwordEncoder) {
        this.adminRepo = adminRepo;
        this.teacherRepo = teacherRepo;
        this.studentRepo = studentRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public Role getRoleUser(String username) {
        if (adminRepo.existsAdminByUser_Username(username)) {
            return Role.ADMIN;
        }

        if (studentRepo.existsStudentByUser_Username(username)) {
            return Role.STUDENT;
        }

        if (teacherRepo.existsTeacherByUser_Username(username)) {
            return Role.TEACHER;
        }

        return Role.NOROLE;
    }

    public boolean getChangedPassword(User user) {
        if (user.getGeneratedPassword() == null) {
            return true;
        }

        return !this.passwordEncoder.matches(user.getGeneratedPassword(), user.getPassword());
    }
}
