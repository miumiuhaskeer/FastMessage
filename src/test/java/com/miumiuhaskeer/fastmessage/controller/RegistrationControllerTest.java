package com.miumiuhaskeer.fastmessage.controller;

import com.miumiuhaskeer.fastmessage.AbstractTest;
import com.miumiuhaskeer.fastmessage.MockMvcQuery;
import com.miumiuhaskeer.fastmessage.model.request.RegistrationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RegistrationControllerTest extends AbstractTest {

    private MockMvcQuery query;

    @BeforeEach
    public void setMockMvcQuery() {
        query = MockMvcQuery.createPostQuery(
                "/sign-up",
                null
        );
    }

    @Test
    public void registrationSuccessfulTest() throws Exception {
        RegistrationRequest request = new RegistrationRequest("user@mail.ru", "12345abc");

        performOkRequest(query, request);
    }

    @Test
    public void invalidEmailTest() throws Exception {
        RegistrationRequest request = new RegistrationRequest("user", "12345abc");

        performBadRequest(query, request);
    }

    @Test
    public void passwordWithoutLettersTest() throws Exception {
        RegistrationRequest request = new RegistrationRequest("user@mail.ru", "12345678");

        performBadRequest(query, request);
    }

    @Test
    public void shortPasswordTest() throws Exception {
        RegistrationRequest request = new RegistrationRequest("user@mail.ru", "1");

        performBadRequest(query, request);
    }

    @Test
    public void userAlreadyExistTest() throws Exception {
        RegistrationRequest request = new RegistrationRequest("admin@mail.ru", "12345abc");

        performBadRequest(query, request);
    }
}
