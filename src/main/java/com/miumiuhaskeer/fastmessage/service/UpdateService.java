package com.miumiuhaskeer.fastmessage.service;

import com.miumiuhaskeer.fastmessage.model.entity.Message;
import com.miumiuhaskeer.fastmessage.model.response.GetAllChatsUpdatesResponse;

import java.util.List;

public interface UpdateService {

    /** {@inheritDoc} */
    List<Message> getChatUpdates(long firstUserId, long secondUserId);

    /** {@inheritDoc} */
    GetAllChatsUpdatesResponse getAllChatsUpdates(long userId);
}
