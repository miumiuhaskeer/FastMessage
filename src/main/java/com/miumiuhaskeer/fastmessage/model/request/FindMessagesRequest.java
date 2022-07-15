package com.miumiuhaskeer.fastmessage.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FindMessagesRequest {
    private String chatId;
    private Long fromId;
    private String content;
    private LocalDateTime sendTimeStart;
    private LocalDateTime sendTimeEnd;
    private Integer page;
    private Integer size;
}
