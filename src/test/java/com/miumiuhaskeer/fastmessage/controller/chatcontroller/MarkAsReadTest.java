package com.miumiuhaskeer.fastmessage.controller.chatcontroller;

import com.miumiuhaskeer.fastmessage.AbstractMongoTest;
import com.miumiuhaskeer.fastmessage.model.entity.Message;
import com.miumiuhaskeer.fastmessage.model.request.MarkAsReadRequest;
import com.miumiuhaskeer.fastmessage.service.ChatService;
import com.miumiuhaskeer.fastmessage.service.UpdateService;
import com.miumiuhaskeer.fastmessage.util.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MarkAsReadTest extends AbstractMongoTest {

    @Autowired
    private ChatService chatService;

    @Autowired
    private UpdateService updateService;

    @Autowired
    private TestUtils testUtils;

    @BeforeEach
    public void clearMongoDB() {
        testUtils.clearAllFromMongo();
    }

    @Test
    public void simpleTest() throws Exception {
        Message message = chatService.sendMessage(admin.getId(), user1.getId(), "Hello World");

        performOkRequest(user1.getId(), message.getId());
    }

    @Test
    public void severalMessagesLastReadTest() throws Exception {
        testUtils.sendMessageNTimes(admin, user1, 3);
        Message message = chatService.sendMessage(admin.getId(), user1.getId(), "Hello World");

        performOkRequest(user1.getId(), message.getId());

        List<Message> messages = updateService.getChatUpdates(admin.getId(), user1.getId());

        assertEquals(0, messages.size());
    }

    @Test
    public void severalMessagesFirstReadTest() throws Exception {
        Message message = chatService.sendMessage(admin.getId(), user1.getId(), "Hello World");

        testUtils.sendMessageNTimes(admin, user1, 3);
        performOkRequest(user1.getId(), message.getId());

        List<Message> messages = updateService.getChatUpdates(admin.getId(), user1.getId());

        assertEquals(3, messages.size());
    }

    @Test
    public void severalMessagesMiddleReadTest() throws Exception {
        testUtils.sendMessageNTimes(admin, user1, 2);

        Message message = chatService.sendMessage(admin.getId(), user1.getId(), "Hello World");

        testUtils.sendMessageNTimes(admin, user1, 3);
        performOkRequest(user1.getId(), message.getId());

        List<Message> messages = updateService.getChatUpdates(admin.getId(), user1.getId());

        assertEquals(3, messages.size());
    }

    @Test
    public void severalMessagesSeveralReadTest() throws Exception {
        testUtils.sendMessageNTimes(admin, user1, 2);
        Message message1 = chatService.sendMessage(admin.getId(), user1.getId(), "Hello World");

        performOkRequest(user1.getId(), message1.getId());

        testUtils.sendMessageNTimes(admin, user1, 3);
        Message message2 = chatService.sendMessage(admin.getId(), user1.getId(), "Hello World");

        performOkRequest(user1.getId(), message2.getId());

        List<Message> messages = updateService.getChatUpdates(admin.getId(), user1.getId());

        assertEquals(0, messages.size());
    }

    @Test
    public void incorrectUserIdTest() throws Exception {
        Message message = chatService.sendMessage(admin.getId(), user1.getId(), "Hello World");

        performBadRequest(0L, message.getId());
    }

    @Test
    public void incorrectMessageIdTest() throws Exception {
        testUtils.sendMessageNTimes(admin, user1, 1);
        performBadRequest(user1.getId(), -1L);
    }

    @Test
    public void unauthorizedTest() throws Exception {
        Message message = chatService.sendMessage(admin.getId(), user1.getId(), "Hello World");
        MarkAsReadRequest request = new MarkAsReadRequest(user1.getId(), message.getId());

        mockMvc.perform(getRequest(request, false))
                .andExpect(status().isUnauthorized());
    }

    private void performOkRequest(Long secondUserId, Long messageId) throws Exception {
        MarkAsReadRequest request = new MarkAsReadRequest(secondUserId, messageId);

        mockMvc.perform(getRequest(request))
                .andExpect(status().isOk());
    }

    private void performBadRequest(Long secondUserId, Long messageId) throws Exception {
        MarkAsReadRequest request = new MarkAsReadRequest(secondUserId, messageId);

        mockMvc.perform(getRequest(request))
                .andExpect(status().isBadRequest());
    }

    private MockHttpServletRequestBuilder getRequest(MarkAsReadRequest request) {
        return getRequest(request, true);
    }

    private MockHttpServletRequestBuilder getRequest(MarkAsReadRequest request, boolean containsAuthHeader) {
        MockHttpServletRequestBuilder builder = post("/chat/markAsRead");

        if (containsAuthHeader) {
            builder.header(HttpHeaders.AUTHORIZATION, adminHeader);
        }

        return builder.contentType(MediaType.APPLICATION_JSON)
                .content(jsonConverter.toJsonSafe(request));
    }
}
