package com.miumiuhaskeer.fastmessage.controller.chatcontroller;

import com.miumiuhaskeer.fastmessage.AbstractMongoTest;
import com.miumiuhaskeer.fastmessage.MockMvcQuery;
import com.miumiuhaskeer.fastmessage.model.entity.Message;
import com.miumiuhaskeer.fastmessage.model.request.MarkAsReadRequest;
import com.miumiuhaskeer.fastmessage.service.ChatService;
import com.miumiuhaskeer.fastmessage.service.UpdateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MarkAsReadTest extends AbstractMongoTest {

    private static final String MESSAGE = "Hello World!";

    @Autowired
    private ChatService chatService;

    @Autowired
    private UpdateService updateService;

    private MockMvcQuery query;

    @BeforeEach
    public void setMockMvcQuery() {
        query = MockMvcQuery.createPostQuery(
                "/chat/markAsRead",
                adminHeader
        );
    }

    @Test
    public void simpleTest() throws Exception {
        sendMarkAsReadRequest();
    }

    @Test
    public void severalMessagesLastReadTest() throws Exception {
        testUtils.sendMessageNTimes(admin, user1, 3);
        sendMarkAsReadRequest();

        List<Message> messages = updateService.getChatUpdates(admin.getId(), user1.getId());

        assertEquals(0, messages.size());
    }

    @Test
    public void severalMessagesFirstReadTest() throws Exception {
        Message message = chatService.sendMessage(admin.getId(), user1.getId(), MESSAGE);
        MarkAsReadRequest request = new MarkAsReadRequest(user1.getId(), message.getId());

        testUtils.sendMessageNTimes(admin, user1, 3);
        performOkRequest(query, request);

        List<Message> messages = updateService.getChatUpdates(admin.getId(), user1.getId());

        assertEquals(3, messages.size());
    }

    @Test
    public void severalMessagesMiddleReadTest() throws Exception {
        testUtils.sendMessageNTimes(admin, user1, 2);
        Message message = chatService.sendMessage(admin.getId(), user1.getId(), MESSAGE);
        MarkAsReadRequest request = new MarkAsReadRequest(user1.getId(), message.getId());

        testUtils.sendMessageNTimes(admin, user1, 3);
        performOkRequest(query, request);

        List<Message> messages = updateService.getChatUpdates(admin.getId(), user1.getId());

        assertEquals(3, messages.size());
    }

    @Test
    public void severalMessagesSeveralReadTest() throws Exception {
        testUtils.sendMessageNTimes(admin, user1, 2);
        sendMarkAsReadRequest();

        testUtils.sendMessageNTimes(admin, user1, 3);
        sendMarkAsReadRequest();

        List<Message> messages = updateService.getChatUpdates(admin.getId(), user1.getId());

        assertEquals(0, messages.size());
    }

    @Test
    public void incorrectUserIdTest() throws Exception {
        Message message = chatService.sendMessage(admin.getId(), user1.getId(), MESSAGE);
        MarkAsReadRequest request = new MarkAsReadRequest(0L, message.getId());

        performBadRequest(query, request);
    }

    @Test
    public void incorrectMessageIdTest() throws Exception {
        testUtils.sendMessageNTimes(admin, user1, 1);
        MarkAsReadRequest request = new MarkAsReadRequest(user1.getId(), -1L);

        performBadRequest(query, request);
    }

    @Test
    public void unauthorizedTest() throws Exception {
        Message message = chatService.sendMessage(admin.getId(), user1.getId(), "Hello World");
        MarkAsReadRequest request = new MarkAsReadRequest(user1.getId(), message.getId());

        query.setAuthToken(null);
        performUnauthorizedRequest(query, request);
    }

    private void sendMarkAsReadRequest() throws Exception {
        Message message = chatService.sendMessage(admin.getId(), user1.getId(), MESSAGE);
        MarkAsReadRequest request = new MarkAsReadRequest(user1.getId(), message.getId());

        performOkRequest(query, request);
    }
}
