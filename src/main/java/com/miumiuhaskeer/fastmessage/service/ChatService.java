package com.miumiuhaskeer.fastmessage.service;

import com.miumiuhaskeer.fastmessage.model.entity.Chat;
import com.miumiuhaskeer.fastmessage.model.entity.Message;

import java.util.List;

public interface ChatService {
    Message sendMessage(long fromId, long chatId, String content);
    List<Message> getMessages(long firstUserId, long secondUserId, int limit, int offset);
    void markAsRead(long userId, long chatId, String messageId);
    Chat createUserChat(long firstUserId, long secondUserId);
}
