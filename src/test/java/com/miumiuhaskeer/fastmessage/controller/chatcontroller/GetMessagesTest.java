package com.miumiuhaskeer.fastmessage.controller.chatcontroller;

import com.miumiuhaskeer.fastmessage.AbstractMongoTest;
import com.miumiuhaskeer.fastmessage.MockMvcQuery;
import com.miumiuhaskeer.fastmessage.model.request.GetMessagesRequest;
import com.miumiuhaskeer.fastmessage.model.response.GetMessagesResponse;
import com.miumiuhaskeer.fastmessage.service.ChatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GetMessagesTest extends AbstractMongoTest {

    @Autowired
    private ChatService chatService;

    private MockMvcQuery query;

    @BeforeEach
    public void setMockMvcQuery() {
        query = MockMvcQuery.createGetQuery(
                "/chat/getMessages",
                adminHeader
        );
    }

    @Test
    public void getEmptyMessageTest() throws Exception {
        chatService.createUserChat(admin.getId(), user1.getId());
        GetMessagesRequest request = new GetMessagesRequest(user1.getId(), 1, 0);
        GetMessagesResponse response = performOkRequest(query, request, GetMessagesResponse.class);

        assertEquals(response.getMessages().size(), 0);
    }

    @Test
    public void getMessageTest() throws Exception {
        testUtils.sendMessageNTimes(admin, user1, 3);
        GetMessagesRequest request = new GetMessagesRequest(user1.getId(), 5, 0);
        GetMessagesResponse response = performOkRequest(query, request, GetMessagesResponse.class);

        assertEquals(3, response.getMessages().size());
    }

    @Test
    public void getMessageOffsetTest() throws Exception {
        testUtils.sendMessageNTimes(admin, user1, 2);
        GetMessagesRequest request = new GetMessagesRequest(user1.getId(), 2, 1);
        GetMessagesResponse response = performOkRequest(query, request, GetMessagesResponse.class);

        assertEquals(1, response.getMessages().size());
    }

    @Test
    public void getMessageMaxOffsetTest() throws Exception {
        testUtils.sendMessageNTimes(admin, user1, 1);
        GetMessagesRequest request = new GetMessagesRequest(user1.getId(), 1, Integer.MAX_VALUE);

        performOkRequest(query, request);
    }

    @Test
    public void getMessageMinOffsetTest() throws Exception {
        GetMessagesRequest request = new GetMessagesRequest(user1.getId(), 1, Integer.MIN_VALUE);

        performBadRequest(query, request);
    }

    @Test
    public void getMessageMaxLimitTest() throws Exception {
        GetMessagesRequest request = new GetMessagesRequest(user1.getId(), Integer.MAX_VALUE, 0);

        performBadRequest(query, request);
    }

    @Test
    public void getMessageMinLimitTest() throws Exception {
        GetMessagesRequest request = new GetMessagesRequest(user1.getId(), Integer.MIN_VALUE, 0);

        performBadRequest(query, request);
    }

    @Test
    public void getMessageWithBadChatIdTest() throws Exception {
        GetMessagesRequest request = new GetMessagesRequest(0L, Integer.MIN_VALUE, 0);

        performBadRequest(query, request);
    }

    @Test
    public void getMessageForOtherUserTest() throws Exception {
        testUtils.sendMessageNTimes(admin, user1, 1);

        GetMessagesRequest request = new GetMessagesRequest(admin.getId(), 1, 0);

        query.setAuthToken(user1Header);
        performOkRequest(query, request);
    }

    @Test
    public void unauthorizedTest() throws Exception {
        GetMessagesRequest request = new GetMessagesRequest(user1.getId(), 0, 0);

        query.setAuthToken(null);
        performUnauthorizedRequest(query, request);
    }
}
