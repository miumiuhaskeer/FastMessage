package com.miumiuhaskeer.fastmessage.repository.mongodb;

import com.miumiuhaskeer.fastmessage.model.entity.UserChat;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserChatRepository extends MongoRepository<UserChat, String> {
    Optional<UserChat> findFirstByChatId(String chatId);
    List<UserChat> findByUserId(long userId);
}
