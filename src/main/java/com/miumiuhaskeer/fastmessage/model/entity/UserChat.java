package com.miumiuhaskeer.fastmessage.model.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = UserChat.COLLECTION_NAME)
public class UserChat {

    public static final String COLLECTION_NAME = "userChat";

    @Id
    private String id;
    private String chatId;
    private Long userId;
    private String lastSeenMessageId;
}
