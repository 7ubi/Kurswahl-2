package com.x7ubi.kurswahl.repository;

import com.x7ubi.kurswahl.models.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepo  extends JpaRepository<Admin, Long> {
    Optional<Admin> findAdminByUser_Username(String username);

    Boolean existsAdminByUser_Username(String username);
}

