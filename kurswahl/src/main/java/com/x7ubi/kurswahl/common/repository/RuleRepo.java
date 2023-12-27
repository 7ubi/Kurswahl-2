package com.x7ubi.kurswahl.common.repository;

import com.x7ubi.kurswahl.common.models.Rule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RuleRepo extends JpaRepository<Rule, Long> {
}
