package com.miumiuhaskeer.fastmessage.controller;

import com.miumiuhaskeer.fastmessage.AbstractMongoTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class HealthCheckControllerTest extends AbstractMongoTest {

    @Test
    public void healthCheckTest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(getRequest())
                .andExpect(status().isOk())
                .andReturn();
        String contentResult = mvcResult.getResponse().getContentAsString();

        // TODO change OK text to constant
        assertEquals("OK", contentResult);
    }

    private MockHttpServletRequestBuilder getRequest() {
        return get("/health");
    }
}
