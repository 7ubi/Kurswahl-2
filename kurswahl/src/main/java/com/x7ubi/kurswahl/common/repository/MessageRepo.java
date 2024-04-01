package com.x7ubi.kurswahl.common.repository;

import com.x7ubi.kurswahl.common.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepo extends JpaRepository<Message, Long> {
}
