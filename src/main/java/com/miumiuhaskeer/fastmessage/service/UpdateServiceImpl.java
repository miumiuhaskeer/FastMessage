package com.miumiuhaskeer.fastmessage.service;

import com.miumiuhaskeer.fastmessage.exception.ChatNotExistException;
import com.miumiuhaskeer.fastmessage.model.entity.Chat;
import com.miumiuhaskeer.fastmessage.model.entity.Message;
import com.miumiuhaskeer.fastmessage.model.entity.UserChat;
import com.miumiuhaskeer.fastmessage.model.response.GetAllChatsUpdatesResponse;
import com.miumiuhaskeer.fastmessage.properties.bundle.ErrorBundle;
import com.miumiuhaskeer.fastmessage.repository.mongodb.ChatRepository;
import com.miumiuhaskeer.fastmessage.repository.mongodb.MessageRepository;
import com.miumiuhaskeer.fastmessage.repository.mongodb.UserChatRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class UpdateServiceImpl implements UpdateService {

    private final ChatRepository chatRepository;
    private final UserChatRepository userChatRepository;
    private final MessageRepository messageRepository;

    /**
     * Get updates for chat. Chat finds by user ids
     *
     * @param firstUserId user in chat
     * @param secondUserId user in chat
     * @return new messages list for chat
     * @throws ChatNotExistException if chat not exist
     */
    @Override
    public List<Message> getChatUpdates(long firstUserId, long secondUserId) {
        Chat chat = chatRepository.findFirstByTags(Arrays.asList(firstUserId, secondUserId))
                .orElseThrow(ChatNotExistException::new);
        UserChat userChat = userChatRepository.findFirstByChatId(chat.getId())
                .orElseThrow(ChatNotExistException::new);
        Long lastSeenMessageId = userChat.getLastSeenMessageId();
        String chatId = userChat.getChatId();

        if (lastSeenMessageId == null) {
            // TODO create Date util class
            Long messageId = -1L;

            return messageRepository.findByChatIdAndIdGreaterThan(chatId, messageId);
        }

        Message message = messageRepository.findById(lastSeenMessageId).orElseThrow(() ->
                new EntityNotFoundException(ErrorBundle.get("error.entityNotFoundException.chat-message.message"))
        );

        return messageRepository.findByChatIdAndIdGreaterThan(chatId, message.getId());
    }

    // TODO change implementation
    /**
     * Get all chats updates by userId. Found user chats by userId, then find new messages count and last message
     * for getting content
     *
     * @param userId id for user to getting all chats
     * @return response for controller method
     */
    @Override
    public GetAllChatsUpdatesResponse getAllChatsUpdates(long userId) {
        List<UserChat> userChats = userChatRepository.findByUserId(userId);
        GetAllChatsUpdatesResponse response = new GetAllChatsUpdatesResponse();

        for (UserChat userChat: userChats) {
            Long messageId = getLastMessageId(userChat);
            String chatId = userChat.getChatId();
            int newMessageCount = messageRepository.countByChatIdAndIdGreaterThan(chatId, messageId);
            Message message = messageRepository.findNewMessage(chatId, messageId);

            if (message == null) {
                continue;
            }

            String content = message.getContent();
            content = (content.length() > 128) ? content.substring(0, 128) : content;

            response.addChat(
                    userChat.getChatId(),
                    newMessageCount,
                    content
            );
        }

        return response;
    }

    /**
     * Get creation date time from last message
     *
     * @param userChat to get last seen message id
     * @return id for last message or -1
     * @throws EntityNotFoundException if message not found
     * @see Date
     */
    private Long getLastMessageId(UserChat userChat) {
        if (userChat.getLastSeenMessageId() != null) {
            Message message = messageRepository.findById(userChat.getLastSeenMessageId()).orElseThrow(() ->
                    new EntityNotFoundException(ErrorBundle.get("error.entityNotFoundException.chat-message.message"))
            );

            return message.getId();
        }

        return -1L;
    }
}
