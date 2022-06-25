package com.miumiuhaskeer.fastmessage.controller;

import com.miumiuhaskeer.fastmessage.AbstractTest;
import com.miumiuhaskeer.fastmessage.model.request.RegistrationRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RegistrationControllerTest extends AbstractTest {

    @Test
    public void registrationSuccessfulTest() throws Exception {
        String email = "user@mail.ru";
        String password = "12345abc";

        mockMvc.perform(getRegisterUserRequest(email, password))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void invalidEmailTest() throws Exception {
        String email = "user";
        String password = "12345abc";

        mockMvc.perform(getRegisterUserRequest(email, password))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void passwordWithoutLettersTest() throws Exception {
        String email = "user@mail.ru";
        String password = "1234578";

        mockMvc.perform(getRegisterUserRequest(email, password))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shortPasswordTest() throws Exception {
        String email = "user@mail.ru";
        String password = "1";

        mockMvc.perform(getRegisterUserRequest(email, password))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void userAlreadyExistTest() throws Exception {
        String email = "admin@mail.ru";
        String password = "12345abc";

        mockMvc.perform(getRegisterUserRequest(email, password))
                .andExpect(status().isBadRequest());
    }

    private MockHttpServletRequestBuilder getRegisterUserRequest(String email, String password) {
        RegistrationRequest request = new RegistrationRequest(email, password);

        return post("/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonConverter.toJsonSafe(request));
    }
}
