package com.miumiuhaskeer.fastmessage.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// TODO change primitive types to Objects
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarkAsReadRequest {
    private long secondUserId;
    private long messageId;
}
