package com.x7ubi.kurswahl.service.admin.user;

import com.x7ubi.kurswahl.repository.UserRepo;
import com.x7ubi.kurswahl.request.auth.SignupRequest;
import org.springframework.stereotype.Service;

@Service
public class UsernameService {

    private final UserRepo userRepo;

    public UsernameService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public String generateUsernameFromName(SignupRequest signupRequest) {
        String firstname = signupRequest.getFirstname().toLowerCase();
        firstname = firstname.replace(' ', '.');
        String surname = signupRequest.getSurname().toLowerCase();
        surname = surname.replace(' ', '.');
        String username = String.format("%s.%s", firstname, surname);
        Integer number = 1;

        while (this.userRepo.existsByUsername(username)) {
            username = String.format("%s%s.%s", firstname, number, surname);
            number++;
        }

        return username;
    }
}
