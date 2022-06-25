package com.miumiuhaskeer.fastmessage.controller.chatcontroller;

import com.miumiuhaskeer.fastmessage.AbstractMongoTest;
import com.miumiuhaskeer.fastmessage.model.entity.Message;
import com.miumiuhaskeer.fastmessage.model.request.SendMessageRequest;
import com.miumiuhaskeer.fastmessage.model.response.SendMessageResponse;
import com.miumiuhaskeer.fastmessage.repository.mongodb.MessageRepository;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SendMessageTest extends AbstractMongoTest {

    @Autowired
    private MessageRepository messageRepository;

    @Test
    public void correctSendMessageTest() throws Exception {
        String message = "Hello World";
        SendMessageRequest request = new SendMessageRequest(user1.getId(), message);

        mockMvc.perform(getSendMessageRequest(request))
                .andExpect(status().isOk());
    }

    @Test
    public void sendMaxLengthMessageTest() throws Exception {
        String message = StringUtils.repeat('a', 1025);

        performBadRequest(user1.getId(), message);
    }

    @Test
    public void sendEmptyMessageTest() throws Exception {
        performBadRequest(user1.getId(), null);
    }

    @Test
    public void sendMessageWithBadChatIdTest() throws Exception {
        // TODO change to constant message
        performBadRequest(0L, "Hello World");
    }

    // TODO move to mongodb test for configurations
    @Test
    public void creationDateTimeNotNullTest() throws Exception {
        SendMessageResponse response = performOkRequest(user1.getId(), "Hello World");
        Message message = messageRepository.findById(response.getMessageId())
                .orElseThrow(AssertionError::new);

        assertNotNull(message.getCreationDateTime());
    }

    @Test
    public void sendEmojiMessageTest() throws Exception {
        String message = "Hi \uD83D\uDE0B";
        SendMessageRequest request = new SendMessageRequest(user1.getId(), message);

        mockMvc.perform(getSendMessageRequest(request))
                .andExpect(status().isOk());
    }

    @Test
    public void unauthorizedTest() throws Exception {
        String message = "Hello World";
        SendMessageRequest request = new SendMessageRequest(user1.getId(), message);

        mockMvc.perform(getSendMessageRequest(request, false))
                .andExpect(status().isUnauthorized());
    }

    private SendMessageResponse performOkRequest(Long userId, String content) throws Exception {
        SendMessageRequest request = new SendMessageRequest(userId, content);

        MvcResult mvcResult = mockMvc.perform(getSendMessageRequest(request))
                .andExpect(status().isOk())
                .andReturn();
        String contentResult = mvcResult.getResponse().getContentAsString();

        return jsonConverter.fromJson(contentResult, SendMessageResponse.class);
    }

    private void performBadRequest(Long userId, String content) throws Exception {
        SendMessageRequest request = new SendMessageRequest(userId, content);

        mockMvc.perform(getSendMessageRequest(request))
                .andExpect(status().isBadRequest());
    }

    private MockHttpServletRequestBuilder getSendMessageRequest(SendMessageRequest request) {
        return getSendMessageRequest(request, true);
    }

    private MockHttpServletRequestBuilder getSendMessageRequest(SendMessageRequest request, boolean containsAuthHeader) {
        MockHttpServletRequestBuilder builder = post("/chat/sendMessage");

        if (containsAuthHeader) {
            builder.header("Authorization", adminHeader);
        }

        return builder.contentType(MediaType.APPLICATION_JSON)
                .content(jsonConverter.toJsonSafe(request));
    }
}
