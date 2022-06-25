package com.miumiuhaskeer.fastmessage.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.PositiveOrZero;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetMessagesRequest {

    private long secondUserId;

    @Max(20)
    @PositiveOrZero
    private int limit;

    @PositiveOrZero
    private int offset;
}
