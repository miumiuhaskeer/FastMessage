package com.miumiuhaskeer.fastmessage.repository.mongodb;

import com.miumiuhaskeer.fastmessage.model.entity.Message;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MessageRepository extends MongoRepository<Message, Long>, MessageRepositoryCustom {

    // TODO move to custom repository
    List<Message> findByChatIdAndIdGreaterThan(String chatId, Long id);
    int countByChatIdAndIdGreaterThan(String chatId, Long id);

    @Aggregation(pipeline = {
            "{ '$match': { 'chatId': '?0'} }",
            "{ '$limit': ?1 }",
            "{ '$skip': ?2 }"
    })
    List<Message> findAllByChatId(String chatId, Integer limit, Integer offset);
    List<Message> findByChatId(String chatId);
}
