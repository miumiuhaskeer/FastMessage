package com.miumiuhaskeer.fastmessage.model.response.fms;

import com.miumiuhaskeer.fastmessage.model.entity.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FindMessagesResponse {
    private List<Message> messages;
}
