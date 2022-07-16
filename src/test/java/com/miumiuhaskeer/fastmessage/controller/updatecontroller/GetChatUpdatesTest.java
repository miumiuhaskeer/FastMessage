package com.miumiuhaskeer.fastmessage.controller.updatecontroller;

import com.miumiuhaskeer.fastmessage.AbstractMongoTest;
import com.miumiuhaskeer.fastmessage.MockMvcQuery;
import com.miumiuhaskeer.fastmessage.model.entity.Message;
import com.miumiuhaskeer.fastmessage.model.request.GetChatUpdatesRequest;
import com.miumiuhaskeer.fastmessage.model.response.GetChatUpdatesResponse;
import com.miumiuhaskeer.fastmessage.service.ChatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GetChatUpdatesTest extends AbstractMongoTest {

    @Autowired
    private ChatService chatService;

    private MockMvcQuery query;

    @BeforeEach
    public void setMockMvcQuery() {
        query = MockMvcQuery.createGetQuery(
                "/getChatUpdates",
                adminHeader
        );
    }

    @Test
    public void noMessagesTest() throws Exception {
        chatService.createUserChat(admin.getId(), user1.getId());
        GetChatUpdatesResponse response = sendChatUpdatesResponse();

        assertEquals(0, response.getMessages().size());
    }

    @Test
    public void oneMessagesTest() throws Exception {
        testUtils.sendMessageNTimes(admin, user1, 1);
        GetChatUpdatesResponse response = sendChatUpdatesResponse();

        checkMessagesContent(1, response.getMessages());
    }

    @Test
    public void severalMessagesTest() throws Exception {
        testUtils.sendMessageNTimes(admin, user1, 5);
        GetChatUpdatesResponse response = sendChatUpdatesResponse();

        checkMessagesContent(5, response.getMessages());
    }

    @Test
    public void oneMessagesWithSeveralUsersTest() throws Exception {
        testUtils.sendMessageNTimes(admin, user1, 1);
        testUtils.sendMessageNTimes(admin, user2, 1);

        GetChatUpdatesResponse response1 = sendChatUpdatesResponse(user1.getId());
        GetChatUpdatesResponse response2 = sendChatUpdatesResponse(user2.getId());

        checkMessagesContent(1, response1.getMessages());
        checkMessagesContent(1, response2.getMessages());
    }

    @Test
    public void severalMessagesWithSeveralUsersTest() throws Exception {
        testUtils.sendMessageNTimes(admin, user1, 2);
        testUtils.sendMessageNTimes(admin, user2, 5);

        GetChatUpdatesResponse response1 = sendChatUpdatesResponse(user1.getId());
        GetChatUpdatesResponse response2 = sendChatUpdatesResponse(user2.getId());

        checkMessagesContent(2, response1.getMessages());
        checkMessagesContent(5, response2.getMessages());
    }

    @Test
    public void severalMessagesWithReadTest() throws Exception {
        testUtils.sendMessageNTimes(admin, user1, 3);
        testUtils.sendMessageNTimes(admin, user2, 4);

        Message message = chatService.sendMessage(admin.getId(), user1.getId(), "Hello World");

        testUtils.sendMessageNTimes(admin, user1, 2);
        chatService.markAsRead(admin.getId(), user1.getId(), message.getId());

        GetChatUpdatesResponse response1 = sendChatUpdatesResponse(user1.getId());
        GetChatUpdatesResponse response2 = sendChatUpdatesResponse(user2.getId());

        checkMessagesContent(2, response1.getMessages());
        checkMessagesContent(4, response2.getMessages());
    }

    @Test
    public void severalMessagesWithSeveralUsersWithReadTest() throws Exception {
        testUtils.sendMessageNTimes(admin, user1, 3);
        testUtils.sendMessageNTimes(admin, user2, 2);

        Message message1 = chatService.sendMessage(admin.getId(), user1.getId(), "Hello World");
        Message message2 = chatService.sendMessage(admin.getId(), user2.getId(), "Hello World");

        testUtils.sendMessageNTimes(admin, user1, 1);
        testUtils.sendMessageNTimes(admin, user2, 4);

        chatService.markAsRead(admin.getId(), user1.getId(), message1.getId());
        chatService.markAsRead(admin.getId(), user2.getId(), message2.getId());

        GetChatUpdatesResponse response1 = sendChatUpdatesResponse(user1.getId());
        GetChatUpdatesResponse response2 = sendChatUpdatesResponse(user2.getId());

        checkMessagesContent(1, response1.getMessages());
        checkMessagesContent(4, response2.getMessages());
    }

    @Test
    public void badUserIdTest() throws Exception {
        chatService.createUserChat(admin.getId(), user1.getId());
        GetChatUpdatesRequest request = new GetChatUpdatesRequest(0L);

        performBadRequest(query, request);
    }

    @Test
    public void unauthorizedTest() throws Exception {
        GetChatUpdatesRequest request = new GetChatUpdatesRequest(user1.getId());

        query.setAuthToken(null);
        testUtils.sendMessageNTimes(admin, user1, 1);
        performUnauthorizedRequest(query, request);
    }

    private GetChatUpdatesResponse sendChatUpdatesResponse() throws Exception {
        return sendChatUpdatesResponse(user1.getId());
    }

    private GetChatUpdatesResponse sendChatUpdatesResponse(Long userId) throws Exception {
        GetChatUpdatesRequest request = new GetChatUpdatesRequest(userId);

        return performOkRequest(query, request, GetChatUpdatesResponse.class);
    }

    // TODO change to test method in TestUtils
    private void checkMessagesContent(int expectedSize, List<GetChatUpdatesResponse.Message> messages) {
        assertEquals(expectedSize, messages.size());

        for (int i = 0; i < messages.size(); i++) {
            GetChatUpdatesResponse.Message message = messages.get(i);

            assertEquals("Test message " + (i + 1), message.getContent());
        }
    }
}
