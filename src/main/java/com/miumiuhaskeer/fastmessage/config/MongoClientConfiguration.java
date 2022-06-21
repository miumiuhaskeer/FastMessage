package com.miumiuhaskeer.fastmessage.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;


public class MongoClientConfiguration extends AbstractMongoClientConfiguration {

    @Override
    public MongoClient mongoClient() {
        return MongoClients.create();
    }

    @Override
    protected String getDatabaseName() {
        return "fastmessage";
    }
}
