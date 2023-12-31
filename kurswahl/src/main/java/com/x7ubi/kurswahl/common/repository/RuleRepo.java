package com.x7ubi.kurswahl.common.repository;

import com.x7ubi.kurswahl.common.models.Rule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RuleRepo extends JpaRepository<Rule, Long> {

    Optional<Rule> findRuleByNameAndRuleSet_Year(String name, Integer year);

    Boolean existsRuleByNameAndRuleSet_Year(String name, Integer year);
}
