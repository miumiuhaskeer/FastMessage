package com.miumiuhaskeer.fastmessage.controller;

import com.miumiuhaskeer.fastmessage.model.ExtendedUserDetails;
import com.miumiuhaskeer.fastmessage.model.entity.Message;
import com.miumiuhaskeer.fastmessage.model.request.FindMessagesRequest;
import com.miumiuhaskeer.fastmessage.model.request.GetMessagesRequest;
import com.miumiuhaskeer.fastmessage.model.request.MarkAsReadRequest;
import com.miumiuhaskeer.fastmessage.model.request.SendMessageRequest;
import com.miumiuhaskeer.fastmessage.model.response.GetMessagesResponse;
import com.miumiuhaskeer.fastmessage.model.response.ResponseEntityBuilder;
import com.miumiuhaskeer.fastmessage.model.response.SendMessageResponse;
import com.miumiuhaskeer.fastmessage.model.response.fms.FindMessagesResponse;
import com.miumiuhaskeer.fastmessage.service.ChatService;
import com.miumiuhaskeer.fastmessage.service.UserService;
import com.miumiuhaskeer.fastmessage.statistic.FMSRequest;
import com.miumiuhaskeer.fastmessage.statistic.FMSRestTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {

    private final FMSRestTemplate fmsRestTemplate;
    private final UserService userService;
    private final ChatService chatService;

    @PostMapping("/sendMessage")
    public ResponseEntity<SendMessageResponse> sendMessage(@RequestBody @Valid SendMessageRequest request) {
        ExtendedUserDetails user = userService.getCurrentUser();
        Message message = chatService.sendMessage(user.getId(), request.getChatId(), request.getContent());

        return ResponseEntity.ok(new SendMessageResponse(
                message.getId(),
                message.getFromId(),
                message.getChatId()
        ));
    }

    @GetMapping("/getMessages")
    public ResponseEntity<GetMessagesResponse> getMessages(@RequestBody @Valid GetMessagesRequest request) {
        ExtendedUserDetails user = userService.getCurrentUser();
        List<Message> messages = chatService.getMessages(
                user.getId(),
                request.getSecondUserId(),
                request.getLimit(),
                request.getOffset()
        );
        GetMessagesResponse response = new GetMessagesResponse();

        for (Message message: messages) {
            // TODO instead of parameters use object addition
            response.addMessage(
                    message.getId(),
                    message.getFromId(),
                    message.getContent(),
                    message.getCreationDateTime()
            );
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/findMessages")
    public ResponseEntity<FindMessagesResponse> findMessages(@RequestBody @Valid FindMessagesRequest request) {
        FindMessagesResponse response = fmsRestTemplate.getForEntity(FMSRequest.FIND_MESSAGES, request);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/markAsRead")
    public ResponseEntity<ResponseEntityBuilder.SimpleResponse> markAsRead(@RequestBody @Valid MarkAsReadRequest request) {
        ExtendedUserDetails user = userService.getCurrentUser();

        chatService.markAsRead(user.getId(), request.getSecondUserId(), request.getMessageId());

        return new ResponseEntityBuilder().create();
    }
}
