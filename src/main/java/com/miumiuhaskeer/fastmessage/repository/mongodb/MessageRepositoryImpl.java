package com.miumiuhaskeer.fastmessage.repository.mongodb;

import com.miumiuhaskeer.fastmessage.model.entity.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

// TODO for all strings add constants
@Repository
@RequiredArgsConstructor
public class MessageRepositoryImpl implements MessageRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    @Override
    public int findNewMessagesCount(String chatId, Long id) {
        return findNewMessages(chatId, id).size();
    }

    @Override
    public Message findNewMessage(String chatId, Long id) {
        List<Message> messages = findNewMessages(chatId, id);

        if (messages.size() > 0) {
            return messages.get(messages.size() - 1);
        }

        return null;
    }

    @Override
    public List<Message> findNewMessages(String chatId, Long id) {
        Criteria criteria = Criteria.where("chatId")
                .is(chatId)
                .and("id")
                .gt(id);

        return mongoTemplate.find(
                Query.query(criteria).with(Sort.by("id")),
                Message.class
        );
    }
}
