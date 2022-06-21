package com.miumiuhaskeer.fastmessage.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetMessagesRequest {

    private long secondUserId;

    @Max(20)
    private int limit;

    private int offset;
}
