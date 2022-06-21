package com.miumiuhaskeer.fastmessage.model.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Document(collection = Chat.COLLECTION_NAME)
public class Chat {

    public static final String COLLECTION_NAME = "chat";

    @Id
    private String id;

    private List<Long> tags;

    @CreatedDate
    private LocalDateTime creationDateTime;
}
