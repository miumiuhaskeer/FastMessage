package com.miumiuhaskeer.fastmessage.controller.updatecontroller;

import com.miumiuhaskeer.fastmessage.AbstractMongoTest;
import com.miumiuhaskeer.fastmessage.MockMvcQuery;
import com.miumiuhaskeer.fastmessage.model.entity.Message;
import com.miumiuhaskeer.fastmessage.model.response.GetAllChatsUpdatesResponse;
import com.miumiuhaskeer.fastmessage.service.ChatService;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GetAllChatUpdatesTest extends AbstractMongoTest {

    @Autowired
    private ChatService chatService;

    private MockMvcQuery query;

    @BeforeEach
    public void setMockMvcQuery() {
        query = MockMvcQuery.createGetQuery(
                "/getAllChatUpdates",
                adminHeader
        );
    }

    @Test
    public void emptyUpdatesTest() throws Exception {
        GetAllChatsUpdatesResponse response = performOkRequest(query, null, GetAllChatsUpdatesResponse.class);

        assertEquals(0, response.getChats().size());
    }

    @Test
    public void updatesTest() throws Exception {
        testUtils.sendMessageNTimes(admin, user1, 3);

        GetAllChatsUpdatesResponse response = performOkRequest(query, null, GetAllChatsUpdatesResponse.class);
        List<GetAllChatsUpdatesResponse.Chat> chats = response.getChats();

        assertEquals(1, chats.size());
        assertEquals(3, chats.get(0).getNewMessagesCount());
    }

    @Test
    public void severalChatsUpdatesTest() throws Exception {
        testUtils.sendMessageNTimes(admin, user1, 2);
        testUtils.sendMessageNTimes(admin, user2, 3);

        GetAllChatsUpdatesResponse response = performOkRequest(query, null, GetAllChatsUpdatesResponse.class);

        assertEquals(2, response.getChats().size());
    }

    @Test
    public void severalMessageContainingTest() throws Exception {
        testUtils.sendMessageNTimes(admin, user1, 2);
        testUtils.sendMessageNTimes(admin, user2, 3);

        GetAllChatsUpdatesResponse response = performOkRequest(query, null, GetAllChatsUpdatesResponse.class);
        List<GetAllChatsUpdatesResponse.Chat> chats = response.getChats();
        GetAllChatsUpdatesResponse.Chat chat1 = chats.get(0);
        GetAllChatsUpdatesResponse.Chat chat2 = chats.get(1);

        if (chat1.getNewMessagesCount() > chat2.getNewMessagesCount()) {
            // TODO change to constant string
            assertEquals("Test message 3", chat1.getLastMessage());
            assertEquals("Test message 2", chat2.getLastMessage());
        } else {
            assertEquals("Test message 2", chat1.getLastMessage());
            assertEquals("Test message 3", chat2.getLastMessage());
        }
    }

    @Test
    public void messageContainingTest() throws Exception {
        testUtils.sendMessageNTimes(admin, user1, 1);

        GetAllChatsUpdatesResponse response = performOkRequest(query, null, GetAllChatsUpdatesResponse.class);
        GetAllChatsUpdatesResponse.Chat chat = response.getChats().get(0);

        assertEquals("Test message 1", chat.getLastMessage());
    }

    @Test
    public void messageContainingMaxSymbolsTest() throws Exception {
        chatService.sendMessage(admin.getId(), user2.getId(), StringUtils.repeat('a', 130));

        GetAllChatsUpdatesResponse response = performOkRequest(query, null, GetAllChatsUpdatesResponse.class);
        GetAllChatsUpdatesResponse.Chat chat = response.getChats().get(0);

        assertEquals(StringUtils.repeat('a', 128), chat.getLastMessage());
    }

    @Test
    public void severalMessagesReadTest() throws Exception {
        Message message = chatService.sendMessage(admin.getId(), user1.getId(), "Hello World");

        chatService.markAsRead(admin.getId(), user1.getId(), message.getId());

        GetAllChatsUpdatesResponse response = performOkRequest(query, null, GetAllChatsUpdatesResponse.class);

        assertEquals(0, response.getChats().size());
    }

    @Test
    public void oneMessagesReadTest() throws Exception {
        testUtils.sendMessageNTimes(admin, user1, 3);
        Message message = chatService.sendMessage(admin.getId(), user1.getId(), "Hello World");

        chatService.markAsRead(admin.getId(), user1.getId(), message.getId());

        GetAllChatsUpdatesResponse response = performOkRequest(query, null, GetAllChatsUpdatesResponse.class);

        assertEquals(0, response.getChats().size());
    }

    @Test
    public void severalMessagesReadSeveralNotReadTest() throws Exception {
        testUtils.sendMessageNTimes(admin, user1, 2);
        Message message = chatService.sendMessage(admin.getId(), user1.getId(), "Hello World");
        testUtils.sendMessageNTimes(admin, user1, 3);

        chatService.markAsRead(admin.getId(), user1.getId(), message.getId());

        GetAllChatsUpdatesResponse response = performOkRequest(query, null, GetAllChatsUpdatesResponse.class);
        List<GetAllChatsUpdatesResponse.Chat> chat = response.getChats();

        assertEquals(1, chat.size());
        assertEquals(3, chat.get(0).getNewMessagesCount());
        assertEquals("Test message 3", chat.get(0).getLastMessage());
    }

    @Test
    public void unauthorizedTest() throws Exception {
        query.setAuthToken(null);
        performUnauthorizedRequest(query, null);
    }
}
