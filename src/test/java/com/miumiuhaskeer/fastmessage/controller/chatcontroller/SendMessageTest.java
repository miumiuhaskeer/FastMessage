package com.miumiuhaskeer.fastmessage.controller.chatcontroller;

import com.miumiuhaskeer.fastmessage.AbstractMongoTest;
import com.miumiuhaskeer.fastmessage.MockMvcQuery;
import com.miumiuhaskeer.fastmessage.model.entity.Message;
import com.miumiuhaskeer.fastmessage.model.request.SendMessageRequest;
import com.miumiuhaskeer.fastmessage.model.response.SendMessageResponse;
import com.miumiuhaskeer.fastmessage.repository.mongodb.MessageRepository;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SendMessageTest extends AbstractMongoTest {

    private static final String MESSAGE = "Hello World!";

    @Autowired
    private MessageRepository messageRepository;

    private MockMvcQuery query;

    @BeforeEach
    public void setMockMvcQuery() {
        query = MockMvcQuery.createPostQuery(
                "/chat/sendMessage",
                adminHeader
        );
    }

    @Test
    public void correctSendMessageTest() throws Exception {
        SendMessageRequest request = new SendMessageRequest(user1.getId(), MESSAGE);

        performOkRequest(query, request);
    }

    @Test
    public void sendMaxLengthMessageTest() throws Exception {
        String message = StringUtils.repeat('a', 1025);
        SendMessageRequest request = new SendMessageRequest(user1.getId(), message);

        performBadRequest(query, request);
    }

    @Test
    public void sendEmptyMessageTest() throws Exception {
        SendMessageRequest request = new SendMessageRequest(user1.getId(), null);

        performBadRequest(query, request);
    }

    @Test
    public void sendMessageWithBadChatIdTest() throws Exception {
        SendMessageRequest request = new SendMessageRequest(0L, MESSAGE);

        performBadRequest(query, request);
    }

    @Test
    public void creationDateTimeNotNullTest() throws Exception {
        SendMessageRequest request = new SendMessageRequest(user1.getId(), MESSAGE);
        SendMessageResponse response = performOkRequest(query, request, SendMessageResponse.class);
        Message message = messageRepository.findById(response.getMessageId())
                .orElseThrow(AssertionError::new);

        assertNotNull(message.getCreationDateTime());
    }

    @Test
    public void sendEmojiMessageTest() throws Exception {
        String message = "Hi \uD83D\uDE0B";
        SendMessageRequest request = new SendMessageRequest(user1.getId(), message);

        performOkRequest(query, request);
    }

    @Test
    public void unauthorizedTest() throws Exception {
        SendMessageRequest request = new SendMessageRequest(user1.getId(), MESSAGE);

        query.setAuthToken(null);
        performUnauthorizedRequest(query, request);
    }
}
