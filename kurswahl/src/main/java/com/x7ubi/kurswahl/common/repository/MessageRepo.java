package com.x7ubi.kurswahl.common.repository;

import com.x7ubi.kurswahl.common.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MessageRepo extends JpaRepository<Message, Long> {

    Optional<Message> findMessageByMessageId(Long messageId);
}
