package com.miumiuhaskeer.fastmessage.controller.chatcontroller;

import com.miumiuhaskeer.fastmessage.AbstractMongoTest;
import com.miumiuhaskeer.fastmessage.model.request.GetMessagesRequest;
import com.miumiuhaskeer.fastmessage.model.response.GetMessagesResponse;
import com.miumiuhaskeer.fastmessage.service.ChatService;
import com.miumiuhaskeer.fastmessage.util.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GetMessagesTest extends AbstractMongoTest {

    @Autowired
    private ChatService chatService;

    @Autowired
    private TestUtils testUtils;

    @BeforeEach
    public void clearMongoDB() {
        testUtils.clearAllFromMongo();
    }

    @Test
    public void getEmptyMessageTest() throws Exception {
        chatService.createUserChat(admin.getId(), user1.getId());
        GetMessagesResponse response = performOkRequest(user1.getId(), 1, 0);

        assertEquals(response.getMessages().size(), 0);
    }

    @Test
    public void getMessageTest() throws Exception {
        testUtils.sendMessageNTimes(admin, user1, 3);
        GetMessagesResponse response = performOkRequest(user1.getId(), 5, 0);

        assertEquals(3, response.getMessages().size());
    }

    @Test
    public void getMessageOffsetTest() throws Exception {
        testUtils.sendMessageNTimes(admin, user1, 2);
        GetMessagesResponse response = performOkRequest(user1.getId(), 2, 1);

        assertEquals(1, response.getMessages().size());
    }

    @Test
    public void getMessageMaxOffsetTest() throws Exception {
        testUtils.sendMessageNTimes(admin, user1, 1);
        performOkRequest(user1.getId(), 1, Integer.MAX_VALUE);
    }

    @Test
    public void getMessageMinOffsetTest() throws Exception {
        performBadRequest(user1.getId(), 1, Integer.MIN_VALUE);
    }

    @Test
    public void getMessageMaxLimitTest() throws Exception {
        performBadRequest(user1.getId(), Integer.MAX_VALUE, 0);
    }

    @Test
    public void getMessageMinLimitTest() throws Exception {
        performBadRequest(user1.getId(), Integer.MIN_VALUE, 0);
    }

    @Test
    public void getMessageWithBadChatIdTest() throws Exception {
        performBadRequest(0L, Integer.MIN_VALUE, 0);
    }

    @Test
    public void getMessageForOtherUserTest() throws Exception {
        testUtils.sendMessageNTimes(admin, user1, 1);

        GetMessagesRequest request = new GetMessagesRequest(admin.getId(), 1, 0);
        MockHttpServletRequestBuilder builder = get("/chat/getMessages")
                .header("Authorization", user1Header)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonConverter.toJsonSafe(request));

        mockMvc.perform(builder)
                .andExpect(status().isOk());
    }

    @Test
    public void unauthorizedTest() throws Exception {
        GetMessagesRequest request = new GetMessagesRequest(user1.getId(), 0, 0);

        mockMvc.perform(getGetMessageRequest(request, false))
                .andExpect(status().isUnauthorized());
    }

    private GetMessagesResponse performOkRequest(Long secondUserId, Integer limit, Integer offset) throws Exception {
        GetMessagesRequest request = new GetMessagesRequest(secondUserId, limit, offset);

        MvcResult mvcResult = mockMvc.perform(getGetMessageRequest(request))
                .andExpect(status().isOk())
                .andReturn();
        String content = mvcResult.getResponse().getContentAsString();

        return jsonConverter.fromJson(content, GetMessagesResponse.class);
    }

    private void performBadRequest(Long secondUserId, Integer limit, Integer offset) throws Exception {
        GetMessagesRequest request = new GetMessagesRequest(secondUserId, limit, offset);

        mockMvc.perform(getGetMessageRequest(request))
                .andExpect(status().isBadRequest());
    }

    private MockHttpServletRequestBuilder getGetMessageRequest(GetMessagesRequest request) {
        return getGetMessageRequest(request, true);
    }

    // TODO take out in one method
    private MockHttpServletRequestBuilder getGetMessageRequest(GetMessagesRequest request, boolean containsAuthHeader) {
        MockHttpServletRequestBuilder builder = get("/chat/getMessages");

        if (containsAuthHeader) {
            builder.header("Authorization", adminHeader);
        }

        return builder.contentType(MediaType.APPLICATION_JSON)
                .content(jsonConverter.toJsonSafe(request));
    }
}
