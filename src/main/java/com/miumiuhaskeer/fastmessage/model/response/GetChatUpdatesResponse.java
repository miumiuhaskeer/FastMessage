package com.miumiuhaskeer.fastmessage.model.response;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class GetChatUpdatesResponse {

    private final List<Message> messages = new ArrayList<>();

    public void addMessage(String messageId, long fromUser, String content, LocalDateTime creationDateTime) {
        messages.add(new Message(
                messageId,
                fromUser,
                content,
                creationDateTime
        ));
    }

    @Data
    @RequiredArgsConstructor
    private static final class Message {
        private final String messageId;
        private final Long fromUser;
        private final String content;
        private final LocalDateTime creationDateTime;
    }
}
