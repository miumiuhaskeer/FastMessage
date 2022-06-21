package com.miumiuhaskeer.fastmessage.dao;

import com.miumiuhaskeer.fastmessage.model.entity.Chat;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Arrays;

@Repository
@RequiredArgsConstructor
public class UserChatDAOImpl implements UserChatDAO {

    private final MongoOperations mongoOperations;
    private final Query query = new Query(Criteria.where("id").is(Chat.COLLECTION_NAME));

    @Override
    public Chat createUserChat(long firstUserId, long secondUserId) {
        Chat chat = new Chat();

        chat.setTags(Arrays.asList(firstUserId, secondUserId));

        return null;
    }

    @Override
    public boolean existByTags(long firstUserId, long secondUserId) {
        return false;
    }

    @Override
    public boolean existById(String id) {
        return false;
    }
}
