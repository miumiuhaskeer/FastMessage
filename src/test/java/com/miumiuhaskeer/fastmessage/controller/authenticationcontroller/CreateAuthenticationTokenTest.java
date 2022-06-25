package com.miumiuhaskeer.fastmessage.controller.authenticationcontroller;

import com.miumiuhaskeer.fastmessage.AbstractTest;
import com.miumiuhaskeer.fastmessage.model.request.LoginRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CreateAuthenticationTokenTest extends AbstractTest {

    @Test
    public void creationTokenSuccessfulTest() throws Exception {
        String email = "admin@mail.ru";
        String password = "12345678a";

        mockMvc.perform(getRegisterUserRequest(email, password))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void emailNotExistTest() throws Exception {
        String email = "userNotExist@mail.ru";
        String password = "12345abc";

        mockMvc.perform(getRegisterUserRequest(email, password))
                .andExpect(status().isBadRequest());
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
        String email = "admin@mail.ru";
        String password = "1234578";

        mockMvc.perform(getRegisterUserRequest(email, password))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shortPasswordTest() throws Exception {
        String email = "user";
        String password = "1";

        mockMvc.perform(getRegisterUserRequest(email, password))
                .andExpect(status().isBadRequest());
    }

    private MockHttpServletRequestBuilder getRegisterUserRequest(String email, String password) {
        LoginRequest request = new LoginRequest(email, password);

        return post("/authentication/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonConverter.toJsonSafe(request));
    }
}
