package com.x7ubi.kurswahl.common.repository;

import com.x7ubi.kurswahl.common.models.RuleSet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RuleSetRepo extends JpaRepository<RuleSet, Long> {
    Optional<RuleSet> findRuleSetByYear(Integer year);
}
