package com.miumiuhaskeer.fastmessage.repository.mongodb;

import com.miumiuhaskeer.fastmessage.model.entity.Message;

import java.util.List;

public interface MessageRepositoryCustom {
    int findNewMessagesCount(String chatId, Long id);
    Message findNewMessage(String chatId, Long id);
    List<Message> findNewMessages(String chatId, Long id);
}
