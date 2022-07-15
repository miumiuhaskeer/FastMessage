package com.miumiuhaskeer.fastmessage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JsonConverter {

    public static final String JSON_DEFAULT = "{}";

    private final ObjectMapper objectMapper;

    /**
     * Safe convert object to json
     *
     * @param object to convert
     * @return object representation as json string or json default string if some error occurred
     * @see JsonConverter#JSON_DEFAULT
     */
    public String toJsonSafe(Object object) {
        try {
            return toJson(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();

            return JSON_DEFAULT;
        }
    }

    /**
     * Safe convert object to json
     *
     * @param object to convert
     * @param defaultJson result string if some error occurred
     * @return object representation as json string or empty string if some error occurred
     * @see JsonConverter#JSON_DEFAULT
     */
    public String toJsonSafe(Object object, String defaultJson) {
        try {
            return toJson(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();

            return defaultJson;
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
