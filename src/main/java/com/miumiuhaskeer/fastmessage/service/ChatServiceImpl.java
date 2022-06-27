package com.miumiuhaskeer.fastmessage.service;

import com.miumiuhaskeer.fastmessage.exception.ChatAlreadyExistException;
import com.miumiuhaskeer.fastmessage.exception.ChatNotExistException;
import com.miumiuhaskeer.fastmessage.model.entity.Chat;
import com.miumiuhaskeer.fastmessage.model.entity.Message;
import com.miumiuhaskeer.fastmessage.model.entity.UserChat;
import com.miumiuhaskeer.fastmessage.properties.bundle.ErrorBundle;
import com.miumiuhaskeer.fastmessage.repository.mongodb.ChatRepository;
import com.miumiuhaskeer.fastmessage.repository.mongodb.MessageRepository;
import com.miumiuhaskeer.fastmessage.repository.mongodb.UserChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final SequenceGeneratorService sequenceGeneratorService;
    private final UserService userService;
    private final ChatRepository chatRepository;
    private final UserChatRepository userChatRepository;
    private final MessageRepository messageRepository;

    /**
     * Send text message to some user
     *
     * @param fromId user who will send message
     * @param toId user who will receive message
     * @param content string message
     * @return result of creating message
     */
    @Override
    public Message sendMessage(long fromId, long toId, String content) {
        List<Long> tags = Arrays.asList(fromId, toId);
        Chat chat = chatRepository.findFirstByTags(tags).orElseGet(() -> createUserChat(fromId, toId));
        Message message = new Message();

        message.setFromId(fromId);
        message.setChatId(chat.getId());
        message.setContent(content);

        return messageRepository.save(message);
    }

    /**
     * Get messages by user ids with limit and offset. Chat finds by user ids
     *
     * @param firstUserId user in chat
     * @param secondUserId user in chat
     * @param limit count of messages
     * @param offset skip count of messages
     * @return messages list for chat
     * @throws ChatNotExistException if chat not exist
     */
    @Override
    public List<Message> getMessages(long firstUserId, long secondUserId, int limit, int offset) {
        List<Long> tags = Arrays.asList(firstUserId, secondUserId);
        Chat chat = chatRepository.findFirstByTags(tags).orElseThrow(ChatNotExistException::new);

        return messageRepository.findAllByChatId(chat.getId(), limit, offset);
    }

    /**
     * Mark messages as read by last message id and user ids. Chat finds by user ids,
     * messages by messageId and send date (from last message). It is assumed that
     * the new message will not be after old one
     *
     * @param firstUserId user in chat
     * @param secondUserId user in chat
     * @param messageId last message id
     * @throws EntityNotFoundException if some of users not exist and if message not exist
     * @throws ChatNotExistException if chat not exist
     */
    @Override
    public void markAsRead(long firstUserId, long secondUserId, long messageId) {
        // TODO change to validation annotation
        if (!userService.isUserExist(firstUserId, secondUserId)) {
            throw new EntityNotFoundException(ErrorBundle.get("error.entityNotFoundException.user.message"));
        }

        // TODO change to validation annotation
        if (!messageRepository.existsById(messageId)) {
            throw new EntityNotFoundException(ErrorBundle.get("error.entityNotFoundException.chat-message.message"));
        }

        List<Long> tags = Arrays.asList(firstUserId, secondUserId);
        Chat chat = chatRepository.findFirstByTags(tags).orElseThrow(ChatNotExistException::new);
        UserChat userChat = userChatRepository.findFirstByChatId(chat.getId()).orElseThrow(ChatNotExistException::new);

        userChat.setLastSeenMessageId(messageId);
        userChatRepository.save(userChat);
    }

    /**
     * Create chat for two users by user ids
     *
     * @param firstUserId user in chat
     * @param secondUserId user in chat
     * @return result of creating chat
     * @throws EntityNotFoundException if some of users not exist
     * @throws ChatAlreadyExistException if chat already exist
     */
    @Override
    public Chat createUserChat(long firstUserId, long secondUserId) {
        List<Long> tags = Arrays.asList(firstUserId, secondUserId);

        if (!userService.isUserExist(firstUserId, secondUserId)) {
            throw new EntityNotFoundException(ErrorBundle.get("error.entityNotFoundException.user.message"));
        }

        if (chatRepository.existsByTags(tags)) {
            throw new ChatAlreadyExistException();
        }

        Chat chat = new Chat();
        chat.setTags(Arrays.asList(firstUserId, secondUserId));
        chat = chatRepository.insert(chat);

        createUserChatRelationship(firstUserId, chat.getId());

        return chat;
    }

    /**
     * Create relationship between chat and user id
     *
     * @param userId chat participant
     * @param chatId id for chat
     */
    private void createUserChatRelationship(long userId, String chatId) {
        UserChat userChat = new UserChat();

        userChat.setUserId(userId);
        userChat.setChatId(chatId);

        userChatRepository.insert(userChat);
    }
}
