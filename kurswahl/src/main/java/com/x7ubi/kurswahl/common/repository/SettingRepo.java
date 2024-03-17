package com.x7ubi.kurswahl.common.repository;

import com.x7ubi.kurswahl.common.models.Setting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SettingRepo extends JpaRepository<Setting, Long> {

    Optional<Setting> findSettingByName(String name);
}
