package com.miumiuhaskeer.fastmessage.repository.mongodb;

import com.miumiuhaskeer.fastmessage.model.entity.Chat;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRepository extends MongoRepository<Chat, String> {
    boolean existsByTags(List<Long> tags);
    Optional<Chat> findFirstByTags(List<Long> tags);
}
