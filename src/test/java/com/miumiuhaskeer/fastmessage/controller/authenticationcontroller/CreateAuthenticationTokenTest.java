package com.miumiuhaskeer.fastmessage.controller.authenticationcontroller;

import com.miumiuhaskeer.fastmessage.AbstractTest;
import com.miumiuhaskeer.fastmessage.MockMvcQuery;
import com.miumiuhaskeer.fastmessage.model.request.LoginRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CreateAuthenticationTokenTest extends AbstractTest {

    private MockMvcQuery query;

    @BeforeEach
    public void setMockMvcQuery() {
        query = MockMvcQuery.createPostQuery(
                "/authentication/authenticate",
                null
        );
    }

    @Test
    public void creationTokenSuccessfulTest() throws Exception {
        LoginRequest request = new LoginRequest("admin@mail.ru", "12345678a");

        performOkRequest(query, request);
    }

    @Test
    public void emailNotExistTest() throws Exception {
        LoginRequest request = new LoginRequest("userNotExist@mail.ru", "12345abc");

        performBadRequest(query, request);
    }

    @Test
    public void invalidEmailTest() throws Exception {
        LoginRequest request = new LoginRequest("user", "12345abc");

        performBadRequest(query, request);
    }

    @Test
    public void passwordWithoutLettersTest() throws Exception {
        LoginRequest request = new LoginRequest("admin@mail.ru", "1234578");

        performBadRequest(query, request);
    }

    @Test
    public void shortPasswordTest() throws Exception {
        LoginRequest request = new LoginRequest("admin@mail.ru", "1");

        performBadRequest(query, request);
    }
}
