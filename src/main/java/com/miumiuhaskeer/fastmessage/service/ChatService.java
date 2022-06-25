package com.miumiuhaskeer.fastmessage.service;

import com.miumiuhaskeer.fastmessage.model.entity.Chat;
import com.miumiuhaskeer.fastmessage.model.entity.Message;

import java.util.List;

public interface ChatService {

    /** {@inheritDoc} */
    Message sendMessage(long fromId, long chatId, String content);

    /** {@inheritDoc} */
    List<Message> getMessages(long firstUserId, long secondUserId, int limit, int offset);

    /** {@inheritDoc} */
    void markAsRead(long userId, long chatId, long messageId);

    /** {@inheritDoc} */
    Chat createUserChat(long firstUserId, long secondUserId);
}
