package com.miumiuhaskeer.fastmessage.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetChatUpdatesRequest {
    private long secondUserId;
}
