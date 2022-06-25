package com.miumiuhaskeer.fastmessage.model.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = SequenceGenerator.COLLECTION_NAME)
public class SequenceGenerator {

    public static final String COLLECTION_NAME = "sequenceGenerator";

    @Id
    private String id;
    private Long sequence;
}
