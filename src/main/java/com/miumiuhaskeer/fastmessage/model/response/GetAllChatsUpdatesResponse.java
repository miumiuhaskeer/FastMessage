package com.miumiuhaskeer.fastmessage.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetAllChatsUpdatesResponse {

    private List<Chat> chats = new ArrayList<>();

    public void addChat(String chatId, long newMessageCount, @NotNull String lastMessage) {
        // TODO add as constant property
        if (lastMessage.length() > 128) {
            lastMessage = lastMessage.substring(0, 128);
        }

        chats.add(new Chat(chatId, newMessageCount, lastMessage));
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static final class Chat {

        private String chatId;
        private long newMessagesCount;

        /**
         * Last message for chat. Max size must be 128 symbols
         */
        private String lastMessage;
    }
}
