package com.x7ubi.kurswahl.common.repository;

import com.x7ubi.kurswahl.common.models.AddresseeMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddresseeMessageRepo extends JpaRepository<AddresseeMessage, Long> {
}
