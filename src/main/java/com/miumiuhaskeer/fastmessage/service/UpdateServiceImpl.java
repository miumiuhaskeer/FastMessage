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
import java.util.List;

@Service
@AllArgsConstructor
public class UpdateServiceImpl implements UpdateService {

    private final ChatRepository chatRepository;
    private final UserChatRepository userChatRepository;
    private final MessageRepository messageRepository;

    @Override
    public List<Message> getChatUpdates(long firstUserId, long secondUserId) {
        Chat chat = chatRepository.findFirstByTags(Arrays.asList(firstUserId, secondUserId))
                .orElseThrow(ChatNotExistException::new);
        UserChat userChat = userChatRepository.findFirstByChatId(chat.getId())
                .orElseThrow(ChatNotExistException::new);
        Message message = messageRepository.findById(userChat.getLastSeenMessageId()).orElseThrow(() ->
                new EntityNotFoundException(ErrorBundle.get("error.entityNotFoundException.chat-message.message"))
        );

        return messageRepository.findByCreationDateTimeGreaterThan(message.getCreationDateTime());
    }

    @Override
    public GetAllChatsUpdatesResponse getAllChatsUpdates(long userId) {
        List<UserChat> userChats = userChatRepository.findByUserId(userId);
        GetAllChatsUpdatesResponse response = new GetAllChatsUpdatesResponse();

        for (UserChat userChat: userChats) {
            Message message = messageRepository.findById(userChat.getLastSeenMessageId()).orElseThrow(() ->
                    new EntityNotFoundException(ErrorBundle.get("error.entityNotFoundException.chat-message.message"))
            );
            int newMessageCount = messageRepository.countByCreationDateTimeGreaterThan(message.getCreationDateTime());
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
}
