package com.miumiuhaskeer.fastmessage.controller;

import com.miumiuhaskeer.fastmessage.model.ExtendedUserDetails;
import com.miumiuhaskeer.fastmessage.model.entity.Message;
import com.miumiuhaskeer.fastmessage.model.request.GetChatUpdatesRequest;
import com.miumiuhaskeer.fastmessage.model.response.GetAllChatsUpdatesResponse;
import com.miumiuhaskeer.fastmessage.model.response.GetChatUpdatesResponse;
import com.miumiuhaskeer.fastmessage.service.UpdateService;
import com.miumiuhaskeer.fastmessage.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class UpdateController {

    private final UpdateService updateService;
    private final UserService userService;

    @GetMapping("/getChatUpdates")
    public ResponseEntity<GetChatUpdatesResponse> updateChat(@RequestBody @Valid GetChatUpdatesRequest request) {
        ExtendedUserDetails user = userService.getCurrentUser();
        List<Message> messages = updateService.getChatUpdates(user.getId(), request.getSecondUserId());
        GetChatUpdatesResponse response = new GetChatUpdatesResponse();

        for (Message message: messages) {
            response.addMessage(
                    message.getId(),
                    message.getFromId(),
                    message.getContent(),
                    message.getCreationDateTime()
            );
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/getAllChatUpdates")
    public ResponseEntity<GetAllChatsUpdatesResponse> updateAllChats() {
        ExtendedUserDetails user = userService.getCurrentUser();

        return ResponseEntity.ok(updateService.getAllChatsUpdates(user.getId()));
    }
}
