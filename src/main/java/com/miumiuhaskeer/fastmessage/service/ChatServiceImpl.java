package com.miumiuhaskeer.fastmessage.service;

import com.miumiuhaskeer.fastmessage.exception.ChatNotExistException;
import com.miumiuhaskeer.fastmessage.model.entity.Chat;
import com.miumiuhaskeer.fastmessage.model.entity.Message;
import com.miumiuhaskeer.fastmessage.model.entity.UserChat;
import com.miumiuhaskeer.fastmessage.properties.bundle.ErrorBundle;
import com.miumiuhaskeer.fastmessage.repository.mongodb.ChatRepository;
import com.miumiuhaskeer.fastmessage.repository.mongodb.MessageRepository;
import com.miumiuhaskeer.fastmessage.repository.mongodb.UserChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final UserService userService;
    private final ChatRepository chatRepository;
    private final UserChatRepository userChatRepository;
    private final MessageRepository messageRepository;

    @Override
    public Message sendMessage(long fromId, long toId, String content) {
        List<Long> tags = Arrays.asList(fromId, toId);
        Chat chat = chatRepository.findFirstByTags(tags).orElse(createUserChat(fromId, toId));
        Message message = new Message();

        message.setFromId(fromId);
        message.setChatId(chat.getId());
        message.setContent(content);

        return messageRepository.save(message);
    }

    @Override
    public List<Message> getMessages(long firstUserId, long secondUserId, int limit, int offset) {
        List<Long> tags = Arrays.asList(firstUserId, secondUserId);
        Chat chat = chatRepository.findFirstByTags(tags).orElseThrow(ChatNotExistException::new);
        Pageable pageable = PageRequest.of(offset, limit, Sort.by("creation_date_time"));

        return messageRepository.findByChatId(chat.getId(), pageable);
    }

    @Override
    public void markAsRead(long firstUserId, long secondUserId, String messageId) {
        List<Long> tags = Arrays.asList(firstUserId, secondUserId);
        Chat chat = chatRepository.findFirstByTags(tags).orElseThrow(ChatNotExistException::new);
        UserChat userChat = userChatRepository.findFirstByChatId(chat.getId()).orElseThrow(ChatNotExistException::new);

        userChat.setLastSeenMessageId(messageId);
    }

    @Override
    public Chat createUserChat(long firstUserId, long secondUserId) {
        if (!userService.isUserExist(firstUserId, secondUserId)) {
            throw new EntityNotFoundException(ErrorBundle.get("error.entityNotFoundException.user.message"));
        }

        Chat chat = new Chat();
        chat.setTags(Arrays.asList(firstUserId, secondUserId));
        chat = chatRepository.insert(chat);

        createUserChatRelationship(firstUserId, chat.getId());

        return chat;
    }

    private void createUserChatRelationship(long userId, String chatId) {
        UserChat userChat = new UserChat();

        userChat.setUserId(userId);
        userChat.setChatId(chatId);

        userChatRepository.insert(userChat);
    }
}
