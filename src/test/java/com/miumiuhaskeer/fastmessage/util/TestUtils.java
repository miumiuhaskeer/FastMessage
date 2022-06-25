package com.miumiuhaskeer.fastmessage.util;

import com.miumiuhaskeer.fastmessage.model.ExtendedUserDetails;
import com.miumiuhaskeer.fastmessage.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class TestUtils {

    private final ChatService chatService;
    private final MongoTemplate mongoTemplate;

    public void sendMessageNTimes(ExtendedUserDetails user1, ExtendedUserDetails user2, int n) {
        for (int i = 1; i <= n; i++) {
            chatService.sendMessage(user1.getId(), user2.getId(), "Test message " + i);
        }
    }

    public void clearAllFromMongo() {
        Set<String> collections = mongoTemplate.getCollectionNames();

        for (String collection: collections) {
            mongoTemplate.findAllAndRemove(new Query(), collection);
        }
    }
}
