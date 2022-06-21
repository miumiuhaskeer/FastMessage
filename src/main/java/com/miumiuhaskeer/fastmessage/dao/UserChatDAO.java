package com.miumiuhaskeer.fastmessage.dao;

import com.miumiuhaskeer.fastmessage.model.entity.Chat;

public interface UserChatDAO {
    Chat createUserChat(long firstUserId, long secondUserId);
    boolean existByTags(long firstUserId, long secondUserId);
    boolean existById(String id);
}
