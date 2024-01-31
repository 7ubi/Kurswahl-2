package com.x7ubi.kurswahl.common.repository;

import com.x7ubi.kurswahl.common.models.ChoiceClass;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChoiceClassRepo extends JpaRepository<ChoiceClass, Long> {

    Optional<ChoiceClass> findChoiceClassByChoiceClassId(Long choiceClassId);

    List<ChoiceClass> findAllByChoice_ChoiceId(Long choiceId);
}
