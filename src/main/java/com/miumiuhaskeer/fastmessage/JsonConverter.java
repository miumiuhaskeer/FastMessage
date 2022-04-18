package com.miumiuhaskeer.fastmessage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.stereotype.Component;

@Component
public class JsonConverter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public String toJsonSafe(Object object) {
        try {
            return toJson(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();

            // TODO change to error 500
            return "";
        }
    }

    public <T> T fromJsonSafe(String json, Class<T> requiredType) {
        try {
            return fromJson(json, requiredType);
        } catch (JsonProcessingException e) {
            e.printStackTrace();

            return null;
        }
    }

    public String toJson(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }

    public <T> T fromJson(String json, Class<T> requiredType) throws JsonProcessingException {
        return objectMapper.readValue(json, requiredType);
    }
}
