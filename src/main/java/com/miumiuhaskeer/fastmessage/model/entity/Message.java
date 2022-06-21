package com.miumiuhaskeer.fastmessage.model.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@Document(collection = Message.COLLECTION_NAME)
public class Message {

    public static final String COLLECTION_NAME = "message";

    @Id
    private String id;
    private String chatId;
    private Long fromId;
    private String content;

    @CreatedDate
    private LocalDateTime creationDateTime;
}
