package com.miumiuhaskeer.fastmessage;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Class was created for specify test queries for MockMvc.
 * All test classes that use MockMvcQuery must use method with @BeforeEach annotation
 * to clear old query object
 */
@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class MockMvcQuery {

    private final RequestMethod method;
    private final String path;
    private String authToken;

    public static MockMvcQuery createPostQuery(String path, String authToken) {
        return new MockMvcQuery(
                RequestMethod.POST,
                path,
                authToken
        );
    }

    public static MockMvcQuery createGetQuery(String path, String authToken) {
        return new MockMvcQuery(
                RequestMethod.GET,
                path,
                authToken
        );
    }
}
