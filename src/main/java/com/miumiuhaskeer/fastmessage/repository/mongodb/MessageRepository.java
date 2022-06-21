package com.miumiuhaskeer.fastmessage.repository.mongodb;

import com.miumiuhaskeer.fastmessage.model.entity.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface MessageRepository extends MongoRepository<Message, String> {
    List<Message> findByCreationDateTimeGreaterThan(LocalDateTime creationDateTime);
    int countByCreationDateTimeGreaterThan(LocalDateTime creationDateTime);
    List<Message> findByChatId(String chatId);
    List<Message> findByChatId(String chatId, Pageable pageable);
}
