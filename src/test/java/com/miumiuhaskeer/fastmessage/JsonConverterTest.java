package com.miumiuhaskeer.fastmessage;

import lombok.Data;
import org.junit.jupiter.api.Test;

import java.io.Serializable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class JsonConverterTest {

    private final JsonConverter jsonConverter = new JsonConverter();

    @Test
    public void toJsonSafeSuccessTest() {
        String answer = "{\"field1\":1824781234,\"field2\":\"Text example\",\"fields\":[true,false]}";
        String result = jsonConverter.toJsonSafe(new SampleData());

        assertEquals(result, answer);
    }

    @Test
    public void toJsonSafeFailureTest() {
        String answer = JsonConverter.JSON_DEFAULT;
        String result = jsonConverter.toJsonSafe(new Sample());

        assertEquals(result, answer);
    }

    @Test
    public void fromJsonSafeSuccessTest() {
        String json = "{\"field1\":1824781234,\"field2\":\"Text example\",\"fields\":[true,false]}";
        SampleData result = jsonConverter.fromJsonSafe(json, SampleData.class);

        assertEquals(result, new SampleData());
    }

    @Test
    public void fromJsonSafeFailureTest() {
        String json = "{\"field1\":1824781234,\"field2\":\"Text example\",\"fields\":[true,false]}";
        Sample result = jsonConverter.fromJsonSafe(json, Sample.class);

        assertNull(result);
    }

    private static final class Sample implements Serializable {

    }

    @Data
    private static final class SampleData implements Serializable {

        private final int field1 = 1824781234;
        private final String field2 = "Text example";
        private final boolean[] fields = {
                true,
                false
        };
    }
}
