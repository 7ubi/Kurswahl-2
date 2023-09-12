package com.x7ubi.kurswahl.repository;

import com.x7ubi.kurswahl.models.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepo extends JpaRepository<Admin, Long> {
    Optional<Admin> findAdminByUser_Username(String username);

    Optional<Admin> findAdminByAdminId(Long adminId);

    Boolean existsAdminByUser_Username(String username);

    Boolean existsAdminByAdminId(Long adminId);
}

