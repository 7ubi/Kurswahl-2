package com.x7ubi.kurswahl.admin.user.service;

import com.x7ubi.kurswahl.admin.user.mapper.AdminMapper;
import com.x7ubi.kurswahl.admin.user.request.AdminSignupRequest;
import com.x7ubi.kurswahl.admin.user.response.AdminResponse;
import com.x7ubi.kurswahl.common.auth.utils.PasswordGenerator;
import com.x7ubi.kurswahl.common.error.ErrorMessage;
import com.x7ubi.kurswahl.common.exception.EntityNotFoundException;
import com.x7ubi.kurswahl.common.models.Admin;
import com.x7ubi.kurswahl.common.models.User;
import com.x7ubi.kurswahl.common.repository.AddresseeMessageRepo;
import com.x7ubi.kurswahl.common.repository.AdminRepo;
import com.x7ubi.kurswahl.common.repository.MessageRepo;
import com.x7ubi.kurswahl.common.repository.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AdminCreationService {

    private final Logger logger = LoggerFactory.getLogger(AdminCreationService.class);

    private final AdminRepo adminRepo;

    private final UserRepo userRepo;

    private final PasswordEncoder passwordEncoder;

    private final UsernameService usernameService;

    private final AdminMapper adminMapper;

    private final MessageRepo messageRepo;

    private final AddresseeMessageRepo addresseeMessageRepo;

    protected AdminCreationService(AdminRepo adminRepo, UserRepo userRepo, PasswordEncoder passwordEncoder,
                                   UsernameService usernameService, AdminMapper adminMapper, MessageRepo messageRepo, AddresseeMessageRepo addresseeMessageRepo) {
        this.adminRepo = adminRepo;
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.usernameService = usernameService;
        this.adminMapper = adminMapper;
        this.messageRepo = messageRepo;
        this.addresseeMessageRepo = addresseeMessageRepo;
    }

    public void registerAdmin(AdminSignupRequest signupRequest) {

        Admin admin = this.adminMapper.adminRequestToAdmin(signupRequest);
        admin.getUser().setUsername(this.usernameService.generateUsernameFromName(signupRequest));
        admin.getUser().setGeneratedPassword(PasswordGenerator.generatePassword());
        admin.getUser().setPassword(passwordEncoder.encode(admin.getUser().getGeneratedPassword()));
        this.adminRepo.save(admin);

        logger.info(String.format("Admin %s was created", admin.getUser().getUsername()));
    }

    public List<AdminResponse> getAllAdmins() {
        List<Admin> admins = this.adminRepo.findAll();

        return this.adminMapper.adminsToAdminResponseList(admins);
    }

    public void editAdmin(Long adminId, AdminSignupRequest signupRequest) throws EntityNotFoundException {
        Admin admin = getAdminFromId(adminId);

        this.adminMapper.adminRequestToAdmin(signupRequest, admin);

        this.adminRepo.save(admin);
        logger.info(String.format("Admin %s was edited", admin.getUser().getUsername()));
    }

    @Transactional
    public List<AdminResponse> deleteAdmin(Long adminId) throws EntityNotFoundException {
        deleteAdminHelper(adminId);

        return getAllAdmins();
    }

    @Transactional
    protected void deleteAdminHelper(Long adminId) throws EntityNotFoundException {
        Admin admin = getAdminFromId(adminId);
        User adminUser = admin.getUser();

        this.messageRepo.deleteAll(adminUser.getSentMessages());
        this.addresseeMessageRepo.deleteAll(adminUser.getAddresseeMessage());
        adminUser.getSentMessages().clear();
        adminUser.getAddresseeMessage().clear();

        logger.info(String.format("Deleted Admin %s", adminUser.getUsername()));

        this.adminRepo.delete(admin);
        this.userRepo.delete(adminUser);
    }

    public AdminResponse getAdmin(Long adminId) throws EntityNotFoundException {
        Admin admin = getAdminFromId(adminId);
        logger.info(String.format("Found Admin %s", admin.getUser().getUsername()));

        return this.adminMapper.adminToAdminResponse(admin);
    }

    @Transactional
    public List<AdminResponse> deleteAdmins(List<Long> adminIds) throws EntityNotFoundException {

        for (Long adminId : adminIds) {
            deleteAdminHelper(adminId);
        }

        return getAllAdmins();
    }

    private Admin getAdminFromId(Long adminId) throws EntityNotFoundException {
        Optional<Admin> adminOptional = this.adminRepo.findAdminByAdminId(adminId);

        if (adminOptional.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.ADMIN_NOT_FOUND);
        }

        return adminOptional.get();
    }
}
