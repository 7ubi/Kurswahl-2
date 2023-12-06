package com.x7ubi.kurswahl.common.repository;

import com.x7ubi.kurswahl.common.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findUserByUserId(Long userId);

    Boolean existsByUsername(String username);

    Boolean existsUserByUserId(Long userId);
}
