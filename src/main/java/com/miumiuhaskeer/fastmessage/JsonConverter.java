package com.miumiuhaskeer.fastmessage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class JsonConverter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Safe convert object to json
     *
     * @param object to convert
     * @return object representation as json string or empty string if some error occurred
     */
    public String toJsonSafe(Object object) {
        try {
            return toJson(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();

            return "";
        }
    }

    /**
     * Safe convert json to object
     *
     * @param json string representation of object
     * @param requiredType type for json object
     * @return string conversion result or null if some error occurred
     */
    public <T> T fromJsonSafe(String json, Class<T> requiredType) {
        try {
            return fromJson(json, requiredType);
        } catch (JsonProcessingException e) {
            e.printStackTrace();

            return null;
        }
    }

    /**
     * Safe convert object to json
     *
     * @param object to convert
     * @return object representation as json string
     * @throws JsonProcessingException if error occurred during conversion
     */
    public String toJson(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }

    /**
     * Safe convert json to object
     *
     * @param json string representation of object
     * @param requiredType type for json object
     * @return string conversion result
     * @throws JsonProcessingException if error occurred during conversion
     */
    public <T> T fromJson(String json, Class<T> requiredType) throws JsonProcessingException {
        return objectMapper.readValue(json, requiredType);
    }
}
