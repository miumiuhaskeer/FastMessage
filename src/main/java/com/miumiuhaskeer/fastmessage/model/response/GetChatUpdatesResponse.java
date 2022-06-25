package com.miumiuhaskeer.fastmessage.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetChatUpdatesResponse {

    private List<Message> messages = new ArrayList<>();

    public void addMessage(Long messageId, long fromUser, String content, LocalDateTime creationDateTime) {
        messages.add(new Message(
                messageId,
                fromUser,
                content,
                creationDateTime
        ));
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static final class Message {
        private Long messageId;
        private Long fromUser;
        private String content;
        private LocalDateTime creationDateTime;
    }
}
