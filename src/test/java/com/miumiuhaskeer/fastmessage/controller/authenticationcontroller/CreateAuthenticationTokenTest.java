package com.miumiuhaskeer.fastmessage.controller.authenticationcontroller;

import com.miumiuhaskeer.fastmessage.JsonConverter;
import com.miumiuhaskeer.fastmessage.config.TestPersistenceConfig;
import com.miumiuhaskeer.fastmessage.model.request.LoginRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestPersistenceConfig.class)
public class CreateAuthenticationTokenTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JsonConverter jsonConverter;

    @Test
    public void creationTokenSuccessfulTest() throws Exception {
        String email = "admin@mail.ru";
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
        String email = "user";
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
